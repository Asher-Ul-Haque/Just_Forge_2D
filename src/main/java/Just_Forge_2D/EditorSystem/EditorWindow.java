package Just_Forge_2D.EditorSystem;

// - - - Imports | - - -

// - - - Internal

import Just_Forge_2D.AudioSsstem.AudioSystemManager;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.SceneSystem.Scene;

import Just_Forge_2D.EventSystem.Events.Event;


import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.PhysicsSystem.PhysicsSystem;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.RenderingSystem.Framebuffer;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import org.lwjgl.openal.ALCCapabilities;
import Just_Forge_2D.RenderingSystem.Renderer;
import org.lwjgl.openal.ALCapabilities;
import Just_Forge_2D.Utils.TimeKeeper;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Logger;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.AL;

import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;


// - - - | Class | - - -


public class EditorWindow extends Window
{
    // - - - | Private Variables | - - -


    // - - - Window variables - - -
    private boolean isInitialized = false;


    private Framebuffer framebuffer;
    private boolean isRuntimePlaying = false;

    // - - - Systems
    private static Scene currentScene;

    // - - - Singleton
    private static EditorWindow window = null;

    // - - - Editor
    private justForgeImGui editorLayer;
    private ObjectSelector selector;

    // - - - shaders
    Shader defaultShader;
    Shader selectorShader;



    // - - - | Functions | - - -


    // - - - Private Constructor for Singleton
    private EditorWindow()
    {
        super(new WindowConfig());
        Logger.FORGE_LOG_INFO("Started Just Forge 2D");
    }

    // - - - Systems function to change the scene
    public static void changeScene(SceneInitializer INITIALIZER)
    {
        if (currentScene != null)
        {
            Logger.FORGE_LOG_INFO("Clearing Scene Catch from previous run");
            SceneSystemManager.destroy(currentScene);
        }
        getEditor().getPropertiesWindow().setActiveGameObject(null);

        currentScene = new Scene(INITIALIZER, "Editor Scene");
        SceneSystemManager.load(currentScene);
        currentScene.init();
        currentScene.start();
    }

    // - - - Get the window
    public static EditorWindow get()
    {
        if (EditorWindow.window == null)
        {
            EditorWindow.window = new EditorWindow();
            Logger.FORGE_LOG_INFO("Window system restarted");
        }
        return EditorWindow.window;
    }

    // - - - Run the game
    public void run()
    {
        Logger.FORGE_LOG_WARNING("Running Game Engine in automatic mode");
        if (!isInitialized)
        {
            init();
        }
        while (!get().shouldClose())
        {
            window.gameLoop();
        }
        finish();
        close();
    }

    // - - - Initialize the window
    public void init()
    {
        AudioSystemManager.initialize();

        isInitialized = true;

        this.framebuffer = new Framebuffer(1980, 720);
        this.selector = new ObjectSelector(1980, 720);
        glViewport(0, 0, 1980, 720);
        Logger.FORGE_LOG_INFO("Framebuffer created and assigned for offscreen rendering");

        this.editorLayer = new justForgeImGui(this.glfwWindowPtr, this.selector);
        this.editorLayer.initImGui();
        Logger.FORGE_LOG_INFO("Editor linked with window");


        // - - - compile shaders
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        AssetPool.addShader("Selector", "Assets/Shaders/selector.glsl");
        AssetPool.addShader("Debug", "Assets/Shaders/debug.glsl");
        this.defaultShader = AssetPool.getShader("Default");
        this.selectorShader = AssetPool.getShader("Selector");

        beginTime = (float) TimeKeeper.getTime();
        Logger.FORGE_LOG_INFO("Time keeping system Online");

        changeScene(new EditorSceneInitializer());
        Mouse.setCamera(getCurrentScene().getCamera());
    }

    // - - - Cleanup after game
    public void finish()
    {
        AudioSystemManager.terminate();
    }

    // - - - Loop the game
    public void gameLoop()
    {
        if (Math.abs(fps - (int) (1.0d / dt)) >= 600)
        {
            fps = (int) (1.0d / dt);
            Logger.FORGE_LOG_WARNING("Experienced fps spike. FPS: " + fps);
        }

        // - - - Poll events
        glfwPollEvents();


        // - - - Renderpasses - - -

        // - - - 1: renderer to object picker
        glDisable(GL_BLEND);
        selector.enableWriting();

        glViewport(0, 0, 1980, 720);
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Renderer.bindShader(selectorShader);
        currentScene.render(dt);
        selector.disableWriting();

        // - - - 2: render to monitor

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        // - - - Debug Drawing
        DebugPencil.beginFrame();

        // - - - Framebuffer
        this.framebuffer.bind();

        this.clear();

        if (dt >= 0.0d)
        {
            Renderer.bindShader(defaultShader);
            if (isRuntimePlaying)
            {
                currentScene.update(dt);
            }
            else
            {
                currentScene.editorUpdate(dt);
            }
            currentScene.render(dt);
            DebugPencil.draw();
        }

        // - - - Finish drawing to texture so that imgui should be rendered to the window
        this.framebuffer.unbind();

        // - - - Update the editor
        this.editorLayer.update((float) dt, currentScene);


        // - - - finish input frames
        finishInputFrames();
    }


    // - - - | Getters Setters and Decoration | - - -

    // - - - scene
    public static Scene getCurrentScene()
    {
        return get().currentScene;
    }

    // - - - framebuffer
    public static Framebuffer getFramebuffer()
    {
        return get().framebuffer;
    }

    // - - - editor realted - - -
    public static justForgeImGui getEditor()
    {
        return get().editorLayer;
    }

    @Override
    public void onNotify(GameObject OBJECT, Event EVENT)
    {
        switch (EVENT.type)
        {
            case ForgeStart:
                Logger.FORGE_LOG_INFO("Starting Game");
                this.isRuntimePlaying = true;
                SceneSystemManager.save(currentScene);
                EditorWindow.changeScene(new EditorSceneInitializer());
                break;

            case ForgeStop:
                Logger.FORGE_LOG_INFO("Ending Game");
                this.isRuntimePlaying = false;
                EditorWindow.changeScene(new EditorSceneInitializer());
                break;

            case SaveLevel:
                Logger.FORGE_LOG_INFO("Saving Scene: " + currentScene);
                SceneSystemManager.save(currentScene);
                break;

            case LoadLevel:
                EditorWindow.changeScene(new EditorSceneInitializer());
                break;
        }
    }

    public static PhysicsSystem getPhysicsSystem()
    {
        return getCurrentScene().getPhysics();
    }
}