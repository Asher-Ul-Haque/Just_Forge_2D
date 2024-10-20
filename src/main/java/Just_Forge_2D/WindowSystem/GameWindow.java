package Just_Forge_2D.WindowSystem;

// - - - Imports | - - -

// - - - Internal

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.Windows.ComponentsWindow;
import Just_Forge_2D.EditorSystem.Windows.MainWindowConfig;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.GameSystem.GameCodeLoader;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.PhysicsSystem.PhysicsSystemManager;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.RenderingSystem.Renderer;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneScript;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.TimeKeeper;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL11.*;


// - - - | Class | - - -


public class GameWindow extends Window
{
    // - - - | Private Variables | - - -

    // - - - Window variables - - -
    private boolean isInitialized = false;

    // - - - Systems

    // - - - Singleton
    private static GameWindow window = null;

    // - - - Editor


    // - - - | Functions | - - -


    // - - - Private Constructor for Singleton
    GameWindow(WindowConfig CONFIG)
    {
        super(CONFIG);
        Logger.FORGE_LOG_INFO("Started Just Forge 2D");
    }

    // - - - Systems function to change the scene
    public static void changeScene(SceneScript INITIALIZER)
    {
        String path;
        if (get().currentScene != null)
        {
            Logger.FORGE_LOG_INFO("Clearing Scene Catch from previous run");
            SceneSystemManager.destroy(get().currentScene);
            path = getCurrentScene().getSavePath();
        }
        else
        {
            path = INITIALIZER.savePath;
        }
        get().currentScene = new Scene(INITIALIZER, "Main Scene");
        getCurrentScene().setSavePath(path);
        ComponentsWindow.setActiveGameObject(null);
        SceneSystemManager.load(get().currentScene);
        get().currentScene.init();
        get().currentScene.start();
    }

    // - - - Get the window
    public static GameWindow get()
    {
        if (GameWindow.window == null)
        {
            Logger.FORGE_LOG_ERROR("No Window config specified");
            GameWindow.window = new GameWindow(new MainWindowConfig());
        }
        return GameWindow.window;
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
        glViewport(0, 0, WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
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
        switch (EditorSystemManager.getCurrentState())
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

                    glViewport(0, 0, WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
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
                    GameWindow.changeScene(GameWindow.getCurrentScene().getScript().getClass().getDeclaredConstructor().newInstance());
                }
                catch (Exception e)
                {
                    Logger.FORGE_LOG_FATAL(e.getCause());
                }
                break;

            case ForgeStop:
                Logger.FORGE_LOG_INFO("Ending Game");
                ComponentsWindow.clearSelection();
                EditorSystemManager.isRuntimePlaying = false;
                try
                {
                    GameWindow.changeScene(GameWindow.getCurrentScene().getScript().getClass().getDeclaredConstructor().newInstance());
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
                    GameWindow.changeScene(GameWindow.getCurrentScene().getScript().getClass().getDeclaredConstructor().newInstance());
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