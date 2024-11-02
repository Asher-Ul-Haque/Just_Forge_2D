package Just_Forge_2D.WindowSystem;

import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.GLFW.*;

public class WindowSystemManager
{
    private static boolean isGLFWInitialized = false;

    public static void initialize()
    {
        if (isGLFWInitialized)
        {
            Logger.FORGE_LOG_WARNING("Window System is already online");
            return;
        }

        // - - - setup error callbacks
        Logger.FORGE_LOG_DEBUG("Assigning Error Callback for Window System");
        GLFWErrorCallback.createPrint(System.err).set();

        // - - - initialize GLFW
        Logger.FORGE_LOG_INFO("Initializing Window System");
        if (!glfwInit())
        {
            Logger.FORGE_LOG_FATAL("Unable to initialize Window System");
            return;
        }

        isGLFWInitialized = true;
        Logger.FORGE_LOG_INFO("Window System online");
    }

    public static void terminate()
    {
        if (!isGLFWInitialized)
        {
            Logger.FORGE_LOG_ERROR("Window System is already offline");
            return;
        }

        // - - - terminate glfw and free the error callback
        glfwTerminate();
        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if (callback != null)
        {
            callback.free();
        }

        isGLFWInitialized = false;
        Logger.FORGE_LOG_INFO("Window System Offline");
    }

    public static boolean isOnline()
    {
        return isGLFWInitialized;
    }

    public static Vector2i getMonitorSize()
    {
        if (!isOnline())
        {
            initialize();
        }
        long primaryMonitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(primaryMonitor);
        if (vidMode == null)
        {
            Logger.FORGE_LOG_FATAL("Error in getting Monitor Size. Window System Panic");
            return new Vector2i(Settings.DEFAULT_WINDOW_WIDTH(), Settings.DEFAULT_WINDOW_HEIGHT());
        }
        return new Vector2i(vidMode.width(), vidMode.height());
    }

    public static int[] getDecorationSize(Window WINDOW)
    {
        int[] top = new int[1];
        int[] left = new int[1];
        int[] bottom = new int[1];
        int[] right = new int[1];
        org.lwjgl.glfw.GLFW.glfwGetWindowFrameSize(WINDOW.getGlfwWindowPtr(), top, left, bottom, right);
        return new int[]{top[0], left[0], bottom[0], right[0]};
    }

}
