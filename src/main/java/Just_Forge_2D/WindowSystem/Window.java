package Just_Forge_2D.WindowSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import Just_Forge_2D.Utils.TimeKeeper;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glClearColor;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL11C.glViewport;

public class Window implements Observer
{
    // - - - private variables - - -

    protected final WindowConfig config;
    protected float fps = 0;
    protected float beginTime = 0;
    protected float endTime = 0;
    protected float dt = 0.1f;
    protected long glfwWindowPtr;
    protected boolean shouldClose;
    protected Scene currentScene;

    // - - - constructor
    public Window(WindowConfig CONFIG)
    {
        if (!WindowSystemManager.isOnline())
        {
            WindowSystemManager.initialize();
        }

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
        EventManager.addObserver(this);
        Logger.FORGE_LOG_INFO("Creating new Window: " + this.config.title);


        // - - - configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, this.config.visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, this.config.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, this.config.maximized ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, this.config.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, this.config.transparent ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_FLOATING, this.config.alwaysOnTop ? GLFW_TRUE : GLFW_FALSE);

        // - - - mac os specific
        if (System.getProperty("os.name").contains("mac"))
        {
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);
        }

        // - - - Create the window
        this.glfwWindowPtr = glfwCreateWindow(100, 100, this.config.title, 0, 0);
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
        glfwSetWindowSizeCallback(this.glfwWindowPtr, (w, newWidth, newHeight) ->
        {
            this.config.width = newWidth;
            this.config.height = newHeight;
            EventManager.notify(null, new Event(EventTypes.ForgeResize));
        });

        Logger.FORGE_LOG_DEBUG("Linking " + this.config.title + " with Keyboard");
        glfwSetKeyCallback(this.glfwWindowPtr, Keyboard::keyCallback);

        // - - - Initializing openGL
        Logger.FORGE_LOG_DEBUG("Initializing OpenGL");
        glfwMakeContextCurrent(this.glfwWindowPtr);

        // - - - handle vsync
        Logger.FORGE_LOG_INFO("Setting Vsync for " + this.config.title + " to : " + this.config.vsync);
        if (this.config.vsync)
        {
            glfwSwapInterval(1);
        }
        else
        {
            glfwSwapInterval(0);
        }
        this.shouldClose = false;

        // - - - set position
        Logger.FORGE_LOG_DEBUG("Setting position for " + this.config.title + " to : " + this.config.x + " : " + this.config.y);
        glfwSetWindowPos(this.glfwWindowPtr, this.config.x, this.config.y);

        // - - - createIcon
        setIcon(null);

        // - - - create capabilities
        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        Logger.FORGE_LOG_INFO(this.config.title + " Online");
        glfwSetWindowSize(this.glfwWindowPtr, this.config.width, this.config.height);
        glViewport(0, 0, this.config.width, this.config.height);
    }


    // - - - Handle Window Maximize, Minimize, Restore - - -

    public void maximize()
    {
        Logger.FORGE_LOG_DEBUG("Maximizing " + this.config.title);
        setSize(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        if (isWindows) setPosition(0, WindowSystemManager.getDecorationSize(this)[1]);
        else setPosition(0, 0);
        glfwMaximizeWindow(getGlfwWindowPtr());
    }

    public void minimize()
    {
        Logger.FORGE_LOG_DEBUG("Minimizing " + this.config.title);
        glfwIconifyWindow(this.glfwWindowPtr);
    }

    public void restore()
    {
        Logger.FORGE_LOG_DEBUG("Restoring " + this.config.title);
        glfwRestoreWindow(this.glfwWindowPtr);
    }

    public void setAlwaysOnTop(boolean DO)
    {
        Logger.FORGE_LOG_DEBUG("Setting always on top status of " + this.config.title + " to : " + DO);
        if (DO == this.config.alwaysOnTop)
        {
            Logger.FORGE_LOG_WARNING("Always on top status of " + this.config.title + " is already set to " + DO);
            return;
        }
        this.config.alwaysOnTop = DO;
        glfwSetWindowAttrib(this.glfwWindowPtr, GLFW_FLOATING, DO ? GLFW_TRUE : GLFW_FALSE);
    }


    // - - - Handle Window closing - - -

    // - - - close it
    public void close()
    {
        Logger.FORGE_LOG_INFO("Closing " + this.config.title);
        glfwSetWindowShouldClose(this.glfwWindowPtr, true);
        this.shouldClose = true;
    }

    public void finish()
    {
        glfwFreeCallbacks(this.glfwWindowPtr);
        Logger.FORGE_LOG_INFO("Cleaning up window memory");

        glfwDestroyWindow(this.glfwWindowPtr);
    }

    // - - - tell if it should be closed
    public boolean shouldClose()
    {
        return glfwWindowShouldClose(this.glfwWindowPtr);
    }


    // - - - Manage Window size - - -

    public void setResizable(boolean DO)
    {
        Logger.FORGE_LOG_DEBUG("Setting + " + this.config.title + " to : " + DO);
        if (this.config.resizable == DO)
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " already has resizable status set to : " + DO);
            return;
        }
        this.config.resizable = DO;
    }

    public void setSize(int WIDTH, int HEIGHT)
    {
        if (WIDTH <= 0 || HEIGHT <= 0)
        {
            Logger.FORGE_LOG_WARNING("Cannot resize " + this.config.title + " to : " + WIDTH + " : " + HEIGHT);
            return;
        }
        if (this.config.resizable)
        {
            Logger.FORGE_LOG_INFO(this.config.title + " resizing to " + WIDTH + " : " + HEIGHT);
            this.config.width = WIDTH;
            this.config.height = HEIGHT;
            glfwSetWindowSize(this.glfwWindowPtr, WIDTH, HEIGHT);
            glViewport(0, 0, this.config.width, this.config.height);
            return;
        }
        Logger.FORGE_LOG_WARNING("Cannot resize " + this.config.title + ". The window is not resizable");
    }

    // - - - getters

    public int getWidth()
    {
        return this.config.width;
    }

    public int getHeight()
    {
        return this.config.height;
    }


    // - - - Handle Window Position - - -

    public void setPosition(int X, int Y)
    {
        Logger.FORGE_LOG_DEBUG("Setting position of " + this.config.title + " to : " + X + " : " + Y);
        if (X == this.config.x && Y == this.config.y)
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " is already at position " + X + " : " + Y);
            return;
        }
        this.config.x = X;
        this.config.y = Y;
        glfwSetWindowPos(this.glfwWindowPtr, X, Y);
    }

    // - - - Getters

    public int getXPosition()
    {
        return this.config.x;
    }

    public int getYPosition()
    {
        return this.config.y;
    }


    // - - - Handle Aspect Ratio - - -

    public void setAspectRatio(float RATIO)
    {
        Logger.FORGE_LOG_DEBUG("Setting aspect ratio of " + this.config.title + " to : " + RATIO);
        if (RATIO == this.config.aspectRatio)
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " already has aspect ratio set to : " + RATIO);
            return;
        }
        this.config.aspectRatio = RATIO;
        glfwSetWindowAspectRatio(this.glfwWindowPtr, (int) RATIO * 1000, 1000);
    }

    public float getAspectRatio()
    {
        return (float) this.getWidth() / this.getHeight();
    }

    // - - - Handle Icon and Title - - -

    // - - - icon
    public void setIcon(String IMAGE_PATH)
    {
        Logger.FORGE_LOG_DEBUG("Setting Icon for " + this.config.title + " to : " + IMAGE_PATH);
        if (System.getProperty("os.name").toLowerCase().contains("mac"))
        {
            Logger.FORGE_LOG_ERROR("Mac OS doesnt allow for window icons");
            return;
        }
        if (IMAGE_PATH == null)
        {
            Logger.FORGE_LOG_WARNING("No Icon Path specified, going with default icon for " + this.config.title);
        }
        else
        {
            this.config.iconPath = IMAGE_PATH;
        }

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);

        // - - - Load the image
        ByteBuffer image = STBImage.stbi_load(this.config.iconPath, width, height, channels, 4);
        if (image == null)
        {
            Logger.FORGE_LOG_ERROR("Failed to load icon image : " + this.config.iconPath + " for " + this.config.title);
            return;
        }

        // - - - create the image
        GLFWImage icon = GLFWImage.malloc();
        icon.set(width.get(), height.get(), image);

        // - - - set the icon
        GLFWImage.Buffer icons = GLFWImage.malloc(1);
        icons.put(0, icon);
        glfwSetWindowIcon(this.glfwWindowPtr, icons);

        // - - - Free the resources
        STBImage.stbi_image_free(image);
        icon.free();
        icons.free();
    }

    // - - - title
    public String getTitle()
    {
        return this.config.title;
    }

    public void setTitle(String TITLE)
    {
        Logger.FORGE_LOG_DEBUG("Setting title of " + this.config.title + " to : " + TITLE);
        this.config.title = TITLE;
        glfwSetWindowTitle(this.glfwWindowPtr, TITLE);
    }


    // - - - Handle Window Visibility - - -

    // - - - opacity
    public float getOpacity()
    {
        return config.opacity;
        //return glfwGetWindowOpacity(this.glfwWindowPtr);
    }

    public void setOpacity(float OPACITY)
    {
        float clamped = Math.max(0f, Math.min(1f, OPACITY));
        config.opacity = clamped;
        Logger.FORGE_LOG_DEBUG("Setting opacity of " + this.config.title + " to : " + clamped);
        glfwSetWindowOpacity(this.glfwWindowPtr, clamped);
    }

    // - - - hide or show
    public void setVisible(boolean DO)
    {
        Logger.FORGE_LOG_DEBUG("Setting visibility of " + this.config.title + " to : " + DO);
        if (DO == this.config.visible)
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " already has visibility status : " + DO);
            return;
        }
        this.config.visible = DO;
        if (DO)
        {
            glfwShowWindow(this.glfwWindowPtr);
        }
        else
        {
            glfwHideWindow(this.glfwWindowPtr);
        }
    }

    public boolean isVisible()
    {
        return this.config.visible;
    }

    // - - - clear color
    public Vector4f getClearColor()
    {
        return this.config.getClearColor();
    }

    public void setClearColor(Vector4f COLOR)
    {
        Logger.FORGE_LOG_DEBUG("Setting clear color of " + this.config.title + " to : " + COLOR.x + " : " + COLOR.y + " : " + COLOR.z + " : " + COLOR.w);
        if (COLOR.equals(this.config.clearColor))
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " already has the clear color set to : " + COLOR.x + " : " + COLOR.y + " : " + COLOR.z + " : " + COLOR.w);
            return;
        }
        this.config.clearColor.set(COLOR);
    }


    // - - - Handle Window Decoration - - -

    public boolean isDecorated()
    {
        return this.config.decorated;
    }

    public void setDecorated(boolean DO)
    {
        Logger.FORGE_LOG_DEBUG("Setting decoration status of " + this.config.title + " to : " + DO);
        if (this.config.decorated == DO)
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " already has decoration status: " + this.config.decorated);
            return;
        }
        glfwSetWindowAttrib(this.glfwWindowPtr, GLFW_DECORATED, DO ? GLFW_TRUE : GLFW_FALSE);
        this.config.decorated = DO;
    }


    // - - - Handle Window Transparency Status - - -

    public boolean isTransparent()
    {
        return this.config.transparent;
    }

    // - - - Handle FPS - - -

    public void setVsync(boolean DO)
    {
        Logger.FORGE_LOG_DEBUG("Setting vsync for " + this.config.title + " to : " + DO);
        if (this.config.vsync == DO)
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " already has vsync set to : " + DO);
        }
        this.config.vsync = DO;
        if (DO)
        {
            glfwSwapInterval(1);
        }
        else
        {
            glfwSwapInterval(0);
        }
    }

    public boolean isVsync()
    {
        return this.config.vsync;
    }

    public float getFPS()
    {
        return this.fps;
    }


    // - - - Basic Usage - - -

    public void gameLoop()
    {
        warnFPSSpike();
        manageInput();
        if (currentScene != null)
        {
            currentScene.update(dt);
            currentScene.render(dt);
        }
        clear();
        finishInputFrames();
        keepTime();
    }

    @Override
    public void onNotify(GameObject OBJECT, Event EVENT) {}

    protected void warnFPSSpike()
    {
        if (Math.abs(this.fps - (int) ( 1.0f / dt)) >= Settings.DEFAULT_FPS())
        {
            Logger.FORGE_LOG_WARNING(this.config.title + " Experiencing lag spike. Current fps: " + this.fps);
        }
    }

    protected void clear()
    {
        Vector4f clearColor = this.config.getClearColor();
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    protected void finishInputFrames()
    {
        Mouse.endFrame();
        glfwSwapBuffers(this.glfwWindowPtr);
    }

    protected void keepTime()
    {
        endTime = (float) TimeKeeper.getTime();
        dt = endTime - beginTime;
        beginTime = endTime;
        fps = (int) (1.0f / dt);
    }

    protected void manageInput()
    {
        glfwPollEvents();
    }

    public float getDeltaTime() {return this.dt;}

    @Override
    public String toString()
    {
        return this.config.title;
    }

    public long getGlfwWindowPtr()
    {
        return this.glfwWindowPtr;
    }

    // - - - scene
    public void setCurrentScene(Scene SCENE)
    {
        this.currentScene = SCENE;
    }

    public Scene getScene()
    {
        return this.currentScene;
    }
}