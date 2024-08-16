package Just_Forge_2D.WindowSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.RenderingSystems.Renderer;
import Just_Forge_2D.RenderingSystems.Shader;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.TimeKeeper;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;

public class Window implements Observer
{
    // - - - private variables - - -

    private final WindowConfig config;
    private static Renderer sceneRenderer;
    float fps = 0;
    float beginTime = 0;
    float endTime = 0;
    float dt = -1;
    private final long glfwWindowPtr;

    public Window(WindowConfig CONFIG)
    {
        EventManager.addObserver(this);

        if (CONFIG == null)
        {
            Logger.FORGE_LOG_WARNING("No window configurations specified. Resorting to default configurations");
            this.config = new WindowConfig();
        }
        else
        {
            this.config = CONFIG;
        }

        // - - - Initialization - - -

        Logger.FORGE_LOG_INFO("Creating new Window: " + this.config.title);

        // - - - Setup error callbacks for GLFW
        GLFWErrorCallback.createPrint(System.err).set();
        Logger.FORGE_LOG_DEBUG("Error callback assigned for GLFW");

        // - - - Initialize GLFW for the window
        Logger.FORGE_LOG_DEBUG("Initializing GLFW");
        if (!glfwInit())
        {
            Logger.FORGE_LOG_FATAL("Unable to initialize window : " + this.config.title);
        }

        // - - - configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, this.config.visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, this.config.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, this.config.maximized ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, this.config.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, this.config.transparent ? GLFW_TRUE : GLFW_FALSE);

        // - - - Create the window
        this.glfwWindowPtr = glfwCreateWindow(this.config.width, this.config.height, this.config.title, 0, 0);
        if (this.glfwWindowPtr == 0)
        {
            Logger.FORGE_LOG_FATAL("Unable to use GLFW to create Window!!!");
        }
        Logger.FORGE_LOG_INFO("Created window: " + this.config.title);


        // - - - assign callbacks to input systems
        Logger.FORGE_LOG_DEBUG("Linking " + this.config.title + " with Mouse");
        glfwSetCursorPosCallback(this.glfwWindowPtr, Mouse::mousePositionCallback);
        glfwSetMouseButtonCallback(this.glfwWindowPtr, Mouse::mouseButtonCallback);
        glfwSetScrollCallback(this.glfwWindowPtr, Mouse::mouseScrollCallback);
        glfwSetWindowSizeCallback(this.glfwWindowPtr, (w, newWidth, newHeight) -> {
            this.config.width = newWidth;
            this.config.height = newHeight;
            resize();
        });

        Logger.FORGE_LOG_DEBUG("Linking " + this.config.title + " with Keyboard");
        glfwSetKeyCallback(this.glfwWindowPtr, Keyboard::keyCallback);

        // - - - Initializing openGL
        Logger.FORGE_LOG_DEBUG("Initializing OpenGL");
        glfwMakeContextCurrent(this.glfwWindowPtr);

        // - - - handle vsync
        if (this.config.vsync)
        {
            Logger.FORGE_LOG_INFO("V-sync enabled for " + this.config.title);
            glfwSwapInterval(1);
        }

        // - - - create capabilites
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        Logger.FORGE_LOG_INFO(this.config.title + " Online");
    }

    public void close()
    {
        Logger.FORGE_LOG_INFO("Closing " + this.config.title);
        glfwFreeCallbacks(this.glfwWindowPtr);
        glfwDestroyWindow(this.glfwWindowPtr);
    }

    public void loop()
    {
        if (Math.abs(this.fps - (int) ( 1.0f / dt)) >= Configurations.DEFAULT_FPS)
        {
            this.fps = (int) (1.0f / dt);
            Logger.FORGE_LOG_WARNING(this.config.title + " Experiencing lag spike. Current fps: " + this.fps);
        }

        // - - - Poll events
        glfwPollEvents();

        // - - - clear the screen
        glClearColor(this.config.clearColor.x, this.config.clearColor.y, this
                .config.clearColor.z, this.config.clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);


        // - - - finish input frames
        Mouse.endFrame();
        Keyboard.endFrame();

        // - - - swap buffer for next frame
        glfwSwapBuffers(this.glfwWindowPtr);

        // - - - Keep time
        endTime = (float) TimeKeeper.getTime();
        dt = endTime - beginTime;
        beginTime = endTime;
    }

    @Override
    public void onNotify(GameObject OBJECT, Event EVENT)
    {

    }

    public void resize(){}
}
