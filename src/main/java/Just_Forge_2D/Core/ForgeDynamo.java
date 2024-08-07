package Just_Forge_2D.Core;

// - - - Imports | - - -

// - - - Internal

import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.EventSystem.EventSystem;
import Just_Forge_2D.Core.EventSystem.Events.Event;
import Just_Forge_2D.Core.EventSystem.Observer;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.Scene.EditorSceneInitializer;
import Just_Forge_2D.Core.Scene.Scene;
import Just_Forge_2D.Core.Scene.SceneInitializer;
import Just_Forge_2D.Editor.ObjectSelector;
import Just_Forge_2D.Editor.justForgeImGui;
import Just_Forge_2D.Renderer.DebugPencil;
import Just_Forge_2D.Renderer.Framebuffer;
import Just_Forge_2D.Renderer.Renderer;
import Just_Forge_2D.Renderer.Shader;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Utils.justForgeTime;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


// - - - | Class | - - -


public class ForgeDynamo implements Observer
{
    // - - - | Private Variables | - - -


    // - - - Window variables - - -

    // - - - basic size and title
    private int width = 1980;
    private int height= 720;
    private final String title;

    // - - - window pointer
    private long glfwWindow;
    private boolean isInitialized = false;

    // - - - advanced window configuration
    private int transparent = GLFW_FALSE;
    private int maximized = GLFW_FALSE;
    private int visible = GLFW_FALSE;
    private int decorated = GLFW_TRUE;
    private int resizable = GLFW_TRUE;

    // - - - Rendering variables
    private int fps = 0;
    public float r, g, b, a;
    private boolean enableVsync = true;
    private Framebuffer framebuffer;
    private boolean isRuntimePlaying = false;

    // - - - Systems
    private static Scene currentScene;

    // - - - Singleton
    private static ForgeDynamo forgeDynamo = null;

    // - - - Editor
    private justForgeImGui editorLayer;
    private ObjectSelector selector;

    // - - - time keeping
    float beginTime = 0;
    float endTime = 0;
    float dt = -1;

    // - - - shaders
    Shader defaultShader;
    Shader selectorShader;


    // - - - | Functions | - - -


    // - - - Private Constructor for Singleton
    private ForgeDynamo()
    {
        float targetAspectRatio = 16f / 9f;
        this.title = "Just Forge Tester";

        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;

        EventSystem.addObserver(this);

        justForgeLogger.FORGE_LOG_INFO("Started Just Forge 2D");
    }

    // - - - Systems function to change the scene
    public static void changeScene(SceneInitializer INITIALIZER)
    {
        if (currentScene != null)
        {
            justForgeLogger.FORGE_LOG_INFO("Clearing Scene Catch from previous run");
            currentScene.destroy();
        }
        getEditor().getPropertiesWindow().setActiveGameObject(null);

        currentScene = new Scene(INITIALIZER);
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    // - - - Get the window
    public static ForgeDynamo get()
    {
        if (ForgeDynamo.forgeDynamo == null)
        {
            ForgeDynamo.forgeDynamo = new ForgeDynamo();
            justForgeLogger.FORGE_LOG_INFO("Window system restarted");
        }
        return ForgeDynamo.forgeDynamo;
    }

    // - - - Run the game
    public void run()
    {
        justForgeLogger.FORGE_LOG_WARNING("Running Game Engine in automatic mode");
        if (!isInitialized)
        {
            init();
        }
        changeScene(new EditorSceneInitializer());
        while (!ForgeDynamo.shouldClose())
        {
            forgeDynamo.gameLoop();
        }
        finish();
        close();
    }

    // - - - Initialize the window
    public void init()
    {
        justForgeLogger.FORGE_LOG_INFO("Turning on Windowing System");
        // - - - Setup error callback for GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        justForgeLogger.FORGE_LOG_DEBUG("Error callback assigned for GLFW");

        // - - - Initialize GLFW
        if (!glfwInit())
        {
            justForgeLogger.FORGE_LOG_FATAL("Unable to initialize window creation!!");
        }
        justForgeLogger.FORGE_LOG_DEBUG("GLFW initialized successfully");

        // - - - Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, visible);
        glfwWindowHint(GLFW_RESIZABLE, resizable);
        glfwWindowHint(GLFW_MAXIMIZED, maximized);
        glfwWindowHint(GLFW_DECORATED, decorated);
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, transparent);
        justForgeLogger.FORGE_LOG_DEBUG("Window Configuration Read");

        // TODO: Maybe let this stay
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        boolean isMeddledWith = false;
        if (!isMeddledWith)
        {
            this.width = videoMode.width();
            this.height = videoMode.height();
        }

        // - - - Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
        {
            justForgeLogger.FORGE_LOG_FATAL("Unable to create window!!");
        }
        justForgeLogger.FORGE_LOG_INFO("Window successfully created");

        // - - - Set up the mouse
        Mouse.get();
        glfwSetCursorPosCallback(glfwWindow, Mouse::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, Mouse::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, Mouse::mouseScrollCallback);
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            ForgeDynamo.setWidth(newWidth);
            ForgeDynamo.setHeight(newHeight);
        });
        justForgeLogger.FORGE_LOG_INFO("Mouse Input linked with window");

        // - - - Set up the keyboard
        Keyboard.get();
        glfwSetKeyCallback(glfwWindow, Keyboard::keyCallback);
        justForgeLogger.FORGE_LOG_INFO("Keyboard Input linked with window");

        // - - - Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        justForgeLogger.FORGE_LOG_DEBUG("OpenGL initialized");

        // - - - Enable vsync
        if (enableVsync)
        {
            justForgeLogger.FORGE_LOG_WARNING("V-sync enabled. Game engine will attempt to match monitor refresh rate");
            glfwSwapInterval(1);
        }

        // - - - Show the window
        glfwShowWindow(glfwWindow);

        // - - -Create capabilities
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        isInitialized = true;
        justForgeLogger.FORGE_LOG_INFO("Window System Online");

        this.framebuffer = new Framebuffer(1980, 720);
        this.selector = new ObjectSelector(1980, 720);
        glViewport(0, 0, 1980, 720);
        justForgeLogger.FORGE_LOG_INFO("Framebuffer created and assigned for offscreen rendering");

        this.editorLayer = new justForgeImGui(glfwWindow, this.selector);
        this.editorLayer.initImGui();
        justForgeLogger.FORGE_LOG_INFO("Editor linked with window");


        // - - - compile shaders
        this.defaultShader = AssetPool.getShader("Assets/Shaders/default.glsl");
        this.selectorShader = AssetPool.getShader("Assets/Shaders/selector.glsl");

        beginTime = (float) justForgeTime.getTime();
        justForgeLogger.FORGE_LOG_INFO("Time keeping system Online");
    }

    // - - - Cleanup after game
    public void finish()
    {
        // - - - Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        justForgeLogger.FORGE_LOG_INFO("Cleaning up window memory");

        // - - - Terminate GLFW nad free the error callback
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();

        // - - - Final Logs
        justForgeLogger.FORGE_LOG_INFO("Input and window system decoupled");
        justForgeLogger.FORGE_LOG_INFO("All systems offline");
        justForgeLogger.FORGE_LOG_INFO("Game Engine Shutdown");
    }

    // - - - Loop the game
    public void gameLoop()
    {
        if (Math.abs(fps - (int) (1.0d / dt)) >= 600)
        {
            fps = (int) (1.0d / dt);
            justForgeLogger.FORGE_LOG_WARNING("Experienced fps spike. FPS: " + fps);
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

        glClearColor(r, g, b, a);
        glClear(GL_COLOR_BUFFER_BIT);

        if (dt >= 0.0d)
        {
            DebugPencil.draw();
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
        }

        // - - - Finish drawing to texture so that imgui should be rendered to the window
        this.framebuffer.unbind();

        // - - - Update the editor
        this.editorLayer.update((float) dt, currentScene);

        // - - - Swap buffer for next frame
        glfwSwapBuffers(glfwWindow);

        // - - - finish input
        Mouse.endFrame();

        // - - - Keep time
        endTime = (float) justForgeTime.getTime();
        dt = endTime - beginTime;
        beginTime = endTime;
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


    // - - - width, height and title - - -

    public static int getWidth()
    {
        return get().width;
    }

    public static int getHeight()
    {
        return get().height;
    }

    public static void setWidth(int NEW_WIDTH)
    {
        get().width = NEW_WIDTH;
    }

    public static void setHeight(int NEW_HEIGHT)
    {
        get().height = NEW_HEIGHT;
    }

    public static String getWindowTitle()
    {
        return glfwGetWindowTitle(get().glfwWindow);
    }

    public static void setWindowTitle(String TITLE)
    {
        glfwSetWindowTitle(get().glfwWindow, TITLE);
    }

    public static float getAspectRatio()
    {
        return 16f/9f;
    }

    public static void setAspectRatio(int NUMERATOR, int DENOMINATOR)
    {
        glfwSetWindowAspectRatio(get().glfwWindow, NUMERATOR, DENOMINATOR);
    }


    // - - - look and feel - - -

    public static float getWindowOpacity()
    {
        return glfwGetWindowOpacity(get().glfwWindow);
    }

    public static void setWindowOpacity(float OPACITY)
    {
        glfwSetWindowOpacity(get().glfwWindow, OPACITY);
    }

    public static void setWindowPosition(int X, int Y)
    {
        glfwSetWindowPos(get().glfwWindow, X, Y);
    }

    public static void setTransparent(boolean DO)
    {
        if (!get().isInitialized)
        {
            return;
        }
        if (DO)
        {
            glfwSetWindowAttrib(get().glfwWindow, GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        }
        else
        {
            glfwSetWindowAttrib(get().glfwWindow, GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_FALSE);
        }
    }

    public static void setBorderless(boolean DO)
    {
        if (get().decorated == GLFW_FALSE && DO)
        {
            get().decorated = GLFW_TRUE;
        }
        if (DO)
        {
            glfwSetWindowAttrib(get().glfwWindow, GLFW_DECORATED, GLFW_TRUE);
        }
        else
        {
            glfwSetWindowAttrib(get().glfwWindow, GLFW_DECORATED, GLFW_FALSE);
        }
    }

    public static void setClearColor(float R, float G, float B, float A)
    {
        get().g = G;
        get().r = R;
        get().b = B;
        get().a = A;
    }


    // - - - hide, show maximise, minimize, close etc - - -

    public static void close()
    {
        glfwSetWindowShouldClose(get().glfwWindow, true);
    }

    public static boolean shouldClose()
    {
        return glfwWindowShouldClose(get().glfwWindow);
    }

    public static void maximize()
    {
        if (get().maximized == GLFW_FALSE)
        {
            get().maximized = GLFW_TRUE;
        }
        glfwMaximizeWindow(get().glfwWindow);
    }

    public static void minimize()
    {
        glfwIconifyWindow(get().glfwWindow);
    }

    public static void setVisible(boolean DO)
    {
        if (DO)
        {
            glfwShowWindow(get().glfwWindow);
        }
        else
        {
            glfwHideWindow(get().glfwWindow);
        }
    }


    // - - - fps related - - -

    public static void setVsync(boolean ENABLE)
    {
        get().enableVsync  = ENABLE;
        if (!ENABLE)
        {
            glfwSwapInterval(1);
        }
    }

    public static float getFPS()
    {
        return get().fps;
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
                justForgeLogger.FORGE_LOG_INFO("Starting Game");
                this.isRuntimePlaying = true;
                currentScene.save();
                ForgeDynamo.changeScene(new EditorSceneInitializer());
                break;

            case ForgeStop:
                justForgeLogger.FORGE_LOG_INFO("Ending Game");
                this.isRuntimePlaying = false;
                ForgeDynamo.changeScene(new EditorSceneInitializer());
                break;

            case SaveLevel:
                justForgeLogger.FORGE_LOG_INFO("Saving Scene: " + currentScene);
                currentScene.save();
                break;

            case LoadLevel:
                ForgeDynamo.changeScene(new EditorSceneInitializer());
                break;
        }
    }
}