package Just_Forge_2D.WindowSystem;

// - - - Imports | - - -

// - - - Internal

import Just_Forge_2D.EditorSystem.Forge;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.Windows.ComponentsWindow;
import Just_Forge_2D.EditorSystem.Windows.GameWindowConfig;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.GameCodeLoader;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.PhysicsSystem.PhysicsSystemManager;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.RenderingSystem.Framebuffer;
import Just_Forge_2D.RenderingSystem.Renderer;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneScript;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.TimeKeeper;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetWindowOpacity;
import static org.lwjgl.opengl.GL11.*;


// - - - | Class | - - -


public class GameWindow extends Window
{
    // - - - | Private Variables | - - -

    // - - - Window variables - - -
    private boolean isInitialized = false;
    private Framebuffer framebuffer;
    private boolean ignore = false;

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
        framebuffer = new Framebuffer(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
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
            GameWindow.window = new GameWindow(new GameWindowConfig());
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

        if (framebuffer == null) framebuffer = new Framebuffer(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
        glViewport(0, 0, framebuffer.getSize().x, framebuffer.getSize().y);
        Logger.FORGE_LOG_DEBUG("Framebuffer created and assigned for offscreen rendering");
        Logger.FORGE_LOG_DEBUG("Editor linked with window");

        beginTime = (float) TimeKeeper.getTime();
        Logger.FORGE_LOG_INFO("Time keeping system Online");

        Mouse.setCamera(new Camera(new Vector2f(0, 0)));
    }

    // - - - Loop the game
    @Override
    public void gameLoop()
    {
        switch (Forge.getCurrentState())
        {
            case isEditor:
                warnFPSSpike();

                // - - - Poll events
                glfwPollEvents();


                // - - - Render passes - - -

                glViewport(0, 0, framebuffer.getSize().x, framebuffer.getSize().y);
                if (!Forge.isRelease)
                {

                    // - - - 1: renderer to object picker
                    glDisable(GL_BLEND);
                    ObjectSelector.enableWriting();

                    clear();
                    Renderer.bindShader(Forge.selectorShader);
                    currentScene.render(dt);
                    ObjectSelector.disableWriting();

                    // - - - 2: render to monitor

                    glEnable(GL_BLEND);
                    glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

                    // - - - Debug Drawing

                    // - - - Framebuffer
                    framebuffer.bind();

                }
                this.clear();
                DebugPencil.beginFrame();
                if (dt >= 0.0d)
                {
                    Renderer.bindShader(Renderer.defaultShader);
                    if (Forge.isRuntimePlaying)
                    {
                        currentScene.update(dt);
                        GameCodeLoader.loop(dt);
                    }
                    else if (!Forge.isRelease)
                    {
                        currentScene.editorUpdate(dt);
                    }
                    currentScene.render(dt);
                    DebugPencil.draw();
                }

                // - - - Finish drawing to texture so that imgui should be rendered to the window
                if (!Forge.isRelease)
                {
                    framebuffer.unbind();

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
                Forge.isRuntimePlaying = true;
                if (!Forge.isRelease) SceneSystemManager.save(currentScene);
                GameCodeLoader.init();
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
                Forge.isRuntimePlaying = false;
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

            case ForgeResize:
                if (ignore)
                {
                    ignore = false;
                    return;
                }
                if (this.config.resizable)
                {
                    this.framebuffer = new Framebuffer(getWidth(), getHeight());

                    if (Forge.isRelease)
                    {
                        float aspectWidth = getWidth();
                        float aspectHeight = aspectWidth / ((float) GameWindow.getFrameBuffer().getSize().x / GameWindow.getFrameBuffer().getSize().y);
                        float scaleDown = getHeight() / aspectHeight;
                        if (aspectHeight > getHeight())
                        {
                            // - - - switch to pillar mode
                            aspectHeight = getHeight();
                            aspectWidth = aspectHeight * ((float) GameWindow.getFrameBuffer().getSize().x / GameWindow.getFrameBuffer().getSize().y);
                        }
                        Mouse.setGameViewport(new Vector2f(0,  -scaleDown), new Vector2f(aspectWidth, aspectHeight));
                    }
                }
                break;
        }
    }

    public static PhysicsSystemManager getPhysicsSystem()
    {
        return getCurrentScene().getPhysics();
    }

    public static Framebuffer getFrameBuffer() {return get().framebuffer;}

    @Override
    public void setOpacity(float OPACITY)
    {
        float clamped = Math.max(0f, Math.min(1f, OPACITY));
        config.opacity = clamped;
        Logger.FORGE_LOG_DEBUG("Setting opacity of " + this.config.title + " to : " + clamped);
        if (Forge.isRelease) glfwSetWindowOpacity(this.glfwWindowPtr, clamped);
    }

    @Override
    public void setSize(int WIDTH, int HEIGHT)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            Logger.FORGE_LOG_WARNING("Cannot resize " + this.config.title + " to : " + WIDTH + " : " + HEIGHT);
            return;
        }
        if (this.config.resizable)
        {
            this.framebuffer = new Framebuffer(WIDTH, HEIGHT);
            if (Forge.getCurrentState().equals(Forge.state.isSplashScreen))
            {
                super.setSize(WIDTH, HEIGHT);
            }
            if (Forge.isRelease)
            {
                super.setSize(WIDTH, HEIGHT);
                float aspectWidth = WIDTH;
                float aspectHeight = aspectWidth / ((float) GameWindow.getFrameBuffer().getSize().x / GameWindow.getFrameBuffer().getSize().y);
                float scaleDown = HEIGHT / aspectHeight;
                if (aspectHeight > HEIGHT)
                {
                    // - - - switch to pillar mode
                    aspectHeight = HEIGHT;
                    aspectWidth = aspectHeight * ((float) GameWindow.getFrameBuffer().getSize().x / GameWindow.getFrameBuffer().getSize().y);
                }
                Mouse.setGameViewport(new Vector2f(0,  -scaleDown), new Vector2f(aspectWidth, aspectHeight));
            }
            ignore = true;
            EventManager.notify(null, new Event(EventTypes.ForgeResize));
        }
    }

    @Override
    public void close()
    {
        if (!Forge.isRelease) SceneSystemManager.save(currentScene);
        EventManager.notify(null, new Event(EventTypes.ForgeStop));
        GameCodeLoader.terminate();
        super.close();
    }

    public void resetTitleBar()
    {
        this.setTitle("Just Forge 2D");
    }
}