package Just_Forge_2D.WindowSystem;

import Just_Forge_2D.Utils.Logger;
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
        return new Vector2i(vidMode.width(), vidMode.height());
    }

}
