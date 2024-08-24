package Just_Forge_2D.WindowSystem;

import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.RenderingSystems.Renderer;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public class WindowSystemManager
{
    private static final HashMap<Window, Renderer> windowRendererHashMap = new HashMap<>();
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

    public static void assignRenderer(Window WINDOW, Renderer RENDERER)
    {
        Logger.FORGE_LOG_INFO("Linking " + WINDOW.getTitle() + " to Renderer " + RENDERER);
        if (DefaultValues.IS_RELEASE && WINDOW.getGlfwWindowPtr() != EditorManager.editorScreen.getGlfwWindowPtr())
        {
            Logger.FORGE_LOG_FATAL("All renderers are getting pipleined to the editor screen for rendering");
            windowRendererHashMap.put(WINDOW, getRenderer(EditorManager.editorScreen));
            return;
        }
        windowRendererHashMap.put(WINDOW, RENDERER);
    }

    public static Renderer getRenderer(Window WINDOW)
    {
        Renderer renderer = windowRendererHashMap.get(WINDOW);
        if (renderer == null)
        {
            Logger.FORGE_LOG_WARNING("No renderer has been assigned to : " + WINDOW);
            Renderer newRenderer = new Renderer(WINDOW.getTitle());
            assignRenderer(WINDOW, newRenderer);
            return newRenderer;
        }
        return renderer;
    }
}
