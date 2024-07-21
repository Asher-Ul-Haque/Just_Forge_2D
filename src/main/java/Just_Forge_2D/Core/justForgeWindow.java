package Just_Forge_2D.Core;

import Just_Forge_2D.Core.Input.*;
import Just_Forge_2D.Core.Scene.*;
import Just_Forge_2D.Utils.justForgeTime;

import org.lwjgl.opengl.GL;
import org.lwjgl.glfw.GLFWErrorCallback;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class justForgeWindow
{
    private int width;
    private int height;
    private String title;
    private long glfwWindow;
    private int fps = 0;
    public float r, g, b, a;

    private static justForgeScene currentScene;

    private static justForgeWindow window = null;


    private justForgeWindow()
    {
        this.width = 1920;
        this.height = 1080;

        this.title = "Mario";

        this.r = 1.0f;
        this.g = 1.0f;
        this.b = 1.0f;
        this.a = 1.0f;

        changeScene(0);
    }

    public static void changeScene(int newScene)
    {
        switch(newScene)
        {
            case 0:
                currentScene = new justForgeLevelEditorScene();
                // currentScene.init()
                break;

            case 1:
                currentScene = new justForgeLevelScene();
                break;

            default:
                assert false : "Unknown Scene " + newScene;
                break;
        }
    }

    public static justForgeWindow get()
    {
        if (justForgeWindow.window == null)
        {
            justForgeWindow.window = new justForgeWindow();
        }
        return justForgeWindow.window;
    }

    public void run()
    {
        init();
        gameLoop();
        finish();
    }

    public void init()
    {
        // Setup error callback for GLFW
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to create glfw window");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL)
        {
            throw new IllegalStateException("Failed to create the GLFW window");
        }

        // Setup the mouse
        justForgeMouse.init();
        glfwSetCursorPosCallback(glfwWindow, justForgeMouse::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, justForgeMouse::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, justForgeMouse::mouseScrollCallback);

        // Setup the keyboard
        justForgeKeyboard.init();
        glfwSetKeyCallback(glfwWindow, justForgeKeyboard::keyCallback);


        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        //Enable vsync
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
    }

    public void finish()
    {
        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW nad free the error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void gameLoop()
    {
        double beginTime = justForgeTime.getTime();
        double endTime;
        double dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow))
        {
            if (fps != (int) (1.0d / dt))
            {
                fps = (int) (1.0d / dt);
                //System.out.println("Current fps: " + fps);
            }
            // Poll events
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
}
