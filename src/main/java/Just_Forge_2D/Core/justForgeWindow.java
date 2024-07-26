package Just_Forge_2D.Core;

// - - - Imports | - - -

// - - - Internal
import Just_Forge_2D.Core.Input.*;
import Just_Forge_2D.Core.Scene.*;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Utils.justForgeTime;

// - - - External
import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWErrorCallback;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;


// - - - | Class | - - -


public class justForgeWindow
{
    // - - - | Private Variables | - - -


    // - - - Window variables
    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    // - - - Rendering varaibles
    private int fps = 0;
    public float r, g, b, a;

    // - - - Systems
    private static justForgeScene currentScene;

    // - - - Singleton
    private static justForgeWindow window = null;


    // - - - | Functions | - - -


    // - - - Private Constructor for Singleton
    private justForgeWindow()
    {
        this.width = 1920;
        this.height = 780;

        this.title = "Just Forge Tester";

        this.r = 0.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;

        justForgeLogger.FORGE_LOG_INFO("Started Just Forge 2D");
    }


    // - - - Systems function to change the scene
    public static void changeScene(int newScene)
    {
        justForgeLogger.FORGE_LOG_INFO("Switching Scenes");
        switch(newScene)
        {
            case 0:
                currentScene = new justForgeLevelEditorScene();
                currentScene.init();
                break;

            case 1:
                currentScene = new justForgeLevelScene();
                break;

            default:
                justForgeLogger.FORGE_LOG_ERROR("Unkown Scene: " + newScene);
                assert false : "Unknown Scene " + newScene;
                break;
        }
        //currentScene.init();
    }


    // - - - Get the window
    public static justForgeWindow get()
    {
        if (justForgeWindow.window == null)
        {
            justForgeWindow.window = new justForgeWindow();
        }
        return justForgeWindow.window;
    }

    // - - - Run the game
    public void run()
    {
        init();
        gameLoop();
        finish();
    }

    // - - - Initialize the window
    public void init()
    {
        // Setup error callback for GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit())
        {
            justForgeLogger.FORGE_LOG_FATAL("Unable to initialize window creation!!");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        justForgeLogger.FORGE_LOG_TRACE("Window Configuration Read");

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
        {
            justForgeLogger.FORGE_LOG_FATAL("Unable to create window!!");
        }

        // Setup the mouse
        justForgeMouse.init();
        glfwSetCursorPosCallback(glfwWindow, justForgeMouse::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, justForgeMouse::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, justForgeMouse::mouseScrollCallback);
        justForgeLogger.FORGE_LOG_TRACE("Mouse Input linked with window");

        // Setup the keyboard
        justForgeKeyboard.init();
        glfwSetKeyCallback(glfwWindow, justForgeKeyboard::keyCallback);
        justForgeLogger.FORGE_LOG_TRACE("Keyboard Input linked with window");


        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable vsync
        justForgeLogger.FORGE_LOG_DEBUG("V-sync enabled. Game engine will attempt to match monitor refresh rate");
        glfwSwapInterval(1);

        // Show the window
        glfwShowWindow(glfwWindow);

        // Create capabilities
        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        justForgeLogger.FORGE_LOG_INFO("Window System Online");

        changeScene(0);

    }

    // - - - Cleanup after game
    public void finish()
    {
        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        justForgeLogger.FORGE_LOG_INFO("Cleaning up window memory");

        // Terminate GLFW nad free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
        justForgeLogger.FORGE_LOG_INFO("Input and window system decoupled");

        justForgeLogger.FORGE_LOG_INFO("All systems offline");
    }

    // - - - Loop the game
    public void gameLoop()
    {
        double beginTime = justForgeTime.getTime();
        double endTime;
        double dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow))
        {
            if (Math.abs(fps - (int) (1.0d / dt)) >= 30)
            {
                fps = (int) (1.0d / dt);
                justForgeLogger.FORGE_LOG_WARNING("Experienced fps spike. FPS: " + fps);
            }

            // - - - Poll events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if (dt >= 0.0d)
            {
                currentScene.update(dt);
            }

            // Swap buffer for next frame
            glfwSwapBuffers(glfwWindow);

            // Keep time
            endTime = justForgeTime.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    public static justForgeScene getCurrentScene()
    {
        return get().currentScene;
    }
}
