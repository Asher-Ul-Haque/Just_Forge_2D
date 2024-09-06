package Just_Forge_2D.EditorSystem;

// - - - Imports | - - -

// - - - Internal

import Just_Forge_2D.EditorSystem.GameSystem.GameCodeLoader;
import Just_Forge_2D.EditorSystem.Windows.MainWindowConfig;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.EditorSystem.Windows.ComponentsWindow;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.PhysicsSystem.PhysicsSystemManager;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.RenderingSystem.Renderer;
import Just_Forge_2D.Utils.TimeKeeper;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.glfw.GLFW.*;


// - - - | Class | - - -


public class MainWindow extends Window
{
    // - - - | Private Variables | - - -

    // - - - Window variables - - -
    private boolean isInitialized = false;

    // - - - Systems

    // - - - Singleton
    private static MainWindow window = null;

    // - - - Editor


    // - - - | Functions | - - -


    // - - - Private Constructor for Singleton
    MainWindow(WindowConfig CONFIG)
    {
        super(CONFIG);
        Logger.FORGE_LOG_INFO("Started Just Forge 2D");
    }

    // - - - Systems function to change the scene
    public static void changeScene(SceneInitializer INITIALIZER)
    {
        if (get().currentScene != null)
        {
            Logger.FORGE_LOG_INFO("Clearing Scene Catch from previous run");
            SceneSystemManager.destroy(get().currentScene);
        }
        ComponentsWindow.setActiveGameObject(null);

        get().currentScene = new Scene(INITIALIZER, "Main Scene");
        SceneSystemManager.load(get().currentScene);
        get().currentScene.init();
        get().currentScene.start();
    }

    // - - - Get the window
    public static MainWindow get()
    {
        if (MainWindow.window == null)
        {
            Logger.FORGE_LOG_ERROR("No Window config specified");
            MainWindow.window = new MainWindow(new MainWindowConfig());
        }
        return MainWindow.window;
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
        close();
    }

    // - - - Initialize the window
    public void init()
    {

        isInitialized = true;

        EditorSystemManager.setFramebuffer();
        glViewport(0, 0, 1980, 720);
        Logger.FORGE_LOG_INFO("Framebuffer created and assigned for offscreen rendering");

        Logger.FORGE_LOG_INFO("Editor linked with window");


        // - - - compile shaders

        EditorSystemManager.compileShaders();


        beginTime = (float) TimeKeeper.getTime();
        Logger.FORGE_LOG_INFO("Time keeping system Online");

        Mouse.setCamera(new Camera(new Vector2f(0, 0)));
    }

    // - - - Loop the game
    @Override
    public void gameLoop()
    {
        switch (EditorSystemManager.currentState)
        {
            case isEditor:
                warnFPSSpike();

                // - - - Poll events
                glfwPollEvents();


                // - - - Render passes - - -

                if (!EditorSystemManager.isRelease)
                {

                    // - - - 1: renderer to object picker
                    glDisable(GL_BLEND);
                    ObjectSelector.enableWriting();

                    glViewport(0, 0, 1980, 720);
                    clear();
                    Renderer.bindShader(EditorSystemManager.selectorShader);
                    currentScene.render(dt);
                    ObjectSelector.disableWriting();

                    // - - - 2: render to monitor

                    glEnable(GL_BLEND);
                    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
                    // - - - Debug Drawing
                    DebugPencil.beginFrame();

                    // - - - Framebuffer
                    EditorSystemManager.getFramebuffer().bind();

                }
                this.clear();
                if (dt >= 0.0d)
                {
                    Renderer.bindShader(EditorSystemManager.defaultShader);
                    if (EditorSystemManager.isRuntimePlaying)
                    {
                        currentScene.update(dt);
                    }
                    else if (!EditorSystemManager.isRelease)
                    {
                        currentScene.editorUpdate(dt);
                    }
                    GameCodeLoader.loop(dt);
                    currentScene.render(dt);
                    if (!EditorSystemManager.isRelease) DebugPencil.draw();
                }

                // - - - Finish drawing to texture so that imgui should be rendered to the window
                if (!EditorSystemManager.isRelease)
                {
                    EditorSystemManager.getFramebuffer().unbind();

                // - - - Update the editor
                    ImGUIManager.update(dt, currentScene);

                // - - - finish input frames
                }
                finishInputFrames();
                keepTime();
                break;

            case isSplashScreen:

                warnFPSSpike();

                // - - - Poll events
                glfwPollEvents();

                this.clear();

                ImGUIManager.update(dt, currentScene);

                // - - - finish input frames
                finishInputFrames();
                keepTime();
                break;
        }

    }


    // - - - | Getters Setters and Decoration | - - -

    // - - - scene
    public static Scene getCurrentScene()
    {
        return get().getScene();
    }


    @Override
    public void onNotify(GameObject OBJECT, Event EVENT)
    {
        switch (EVENT.type)
        {
            case ForgeStart:
                Logger.FORGE_LOG_INFO("Starting Game");
                EditorSystemManager.isRuntimePlaying = true;
                SceneSystemManager.save(currentScene);
                try
                {
                    MainWindow.changeScene(EditorSystemManager.currentSceneInitializer.getDeclaredConstructor().newInstance());
                }
                catch (Exception e)
                {
                    Logger.FORGE_LOG_FATAL(e.getCause());
                }
                break;

            case ForgeStop:
                Logger.FORGE_LOG_INFO("Ending Game");
                EditorSystemManager.isRuntimePlaying = false;
                try
                {
                    MainWindow.changeScene(EditorSystemManager.currentSceneInitializer.getDeclaredConstructor().newInstance());
                }
                catch (Exception e)
                {
                    Logger.FORGE_LOG_FATAL(e.getCause());
                }
                break;

            case SaveLevel:
                Logger.FORGE_LOG_INFO("Saving Scene: " + currentScene);
                SceneSystemManager.save(get().currentScene);
                break;

            case LoadLevel:
                try
                {
                    MainWindow.changeScene(EditorSystemManager.currentSceneInitializer.getDeclaredConstructor().newInstance());
                }
                catch (Exception e)
                {
                    Logger.FORGE_LOG_FATAL(e.getCause());
                }
                break;
        }
    }

    public static PhysicsSystemManager getPhysicsSystem()
    {
        return getCurrentScene().getPhysics();
    }
}