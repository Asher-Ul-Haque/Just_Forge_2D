package Just_Forge_2D;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

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
    private float r, g, b, a;

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
        boolean color = false;
        while (!glfwWindowShouldClose(glfwWindow))
        {
            // Poll events
            glfwPollEvents();

            // Render
            if (color)
            {
                r = (float) Math.sin(r - 0.01f) / 2 + 0.5f;
                g = (float) Math.sin(g - 0.01f + (3.14 / 3)) / 2 + 0.5f;
                b = (float) Math.sin(b - 0.01f + (3.14 * 2 / 3)) / 2 + 0.5f;
                glClearColor(r, g, b, a);
            }
            else
            {
                glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            }
            glClear(GL_COLOR_BUFFER_BIT);

            if (justForgeKeyboard.isKeyPressed(GLFW_KEY_SPACE))
            {
                color = true;
            }


            // Swap buffer for next frame
            glfwSwapBuffers(glfwWindow);
        }
    }
}
