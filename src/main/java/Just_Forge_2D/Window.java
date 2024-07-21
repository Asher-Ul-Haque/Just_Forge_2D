package Just_Forge_2D;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window
{
    private int width;
    private int height;
    private String title;
    private long glfwWindow;

    private static Window window = null;


    private Window()
    {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
    }

    public static Window get()
    {
        if (Window.window == null)
        {
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run()
    {
        System.out.println("Hello LWJDL " + Version.getVersion());
        init();
        gameLoop();
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

    public void gameLoop()
    {
        while (!glfwWindowShouldClose(glfwWindow))
        {
            // Poll events
            glfwPollEvents();

            // Render
            glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            // Swap buffer for next frame
            glfwSwapBuffers(glfwWindow);
        }
    }
}
