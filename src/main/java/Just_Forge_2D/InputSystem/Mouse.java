package Just_Forge_2D.InputSystem;

import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;


// - - - Mouse Input class
public class Mouse
{
    // - - - Private Variables - - -

    // - - - Singleton
    private static Mouse mouse;

    // - - - Mouse Scrolling
    private double xScroll, yScroll;
    private boolean isDragging;

    // - - - Mouse Positions
    private double xPosition, yPosition;
    private double xPrevious, yPrevious;

    // - - - World Positions
    private double xWorldPosition, yWorldPosition;
    private double xWorldPrevious, yWorldPrevious;

    // - - - Mouse Buttons
    private final int buttonCount = GLFW_MOUSE_BUTTON_LAST + 1;
    private int mouseButtonDownCount = 0;
    private final boolean[] isMouseButtonPressed = new boolean[buttonCount]; // left, middle, right

    // - - - Relations
    private final Vector2f gameViewportPos = new Vector2f();
    private final Vector2f gameViewportSize = new Vector2f(Settings.DEFAULT_WINDOW_WIDTH(), Settings.DEFAULT_WINDOW_HEIGHT());

    private static Camera worldCamera;


    // - - - | Functions | - - -

    // - - - Singleton Constructor
    private Mouse()
    {
        this.xScroll   = 0.0;
        this.yScroll   = 0.0;

        this.xPosition = 0.0f;
        this.yPosition = 0.0f;

        this.xPrevious = 0.0f;
        this.yPrevious = 0.0f;
    }

    // - - - Setup mouse
    public static Mouse get()
    {
        if (Mouse.mouse == null)
        {
            Mouse.mouse = new Mouse();
            Logger.FORGE_LOG_WARNING("Going with editor camera in absence of a supplied camera");
            Logger.FORGE_LOG_INFO("Mouse Input System Online");
        }
        return Mouse.mouse;
    }


    // - - - Callbacks for GLFW - - -

    // - - - Update Mouse Move
    public static void mousePositionCallback(long WINDOW, double X_POSITION, double Y_POSITION)
    {
        // - - - save off mouse state
        get().xPrevious = get().xPosition;
        get().yPrevious = get().yPosition;

        // - - - save the new mouse state
        get().yPosition = Y_POSITION;
        get().xPosition = X_POSITION;

        // - - - save the world position
        get().xWorldPrevious = get().xWorldPosition;
        get().yWorldPrevious = get().yWorldPosition;

        // - - - redo world calculations
        calcWorldY();
        calcWorldX();

        // - - - check drag
        if (get().mouseButtonDownCount > 0) get().isDragging = true;
    }

    // - - - Update Clicks
    public static void mouseButtonCallback(long WINDOW, int BUTTON, int ACTION, int MODIFIER)
    {
        if (BUTTON >= get().isMouseButtonPressed.length)
        {
            return;
        }

        switch(ACTION)
        {
            case GLFW_PRESS:
                get().isMouseButtonPressed[BUTTON] = true;
                get().mouseButtonDownCount++;
                break;

            case GLFW_RELEASE:
                get().isMouseButtonPressed[BUTTON] = false;
                get().isDragging = false;
                get().mouseButtonDownCount--;
                break;

            default:
                break;
        }
    }

    // - - - Update scroll wheel
    public static void mouseScrollCallback(long WINDOW, double X_OFFSET, double Y_OFFSET)
    {
        get().xScroll = X_OFFSET;
        get().yScroll = Y_OFFSET;
    }

    // - - - Cleanup
    public static void endFrame()
    {
        get().xScroll = 0.0f;
        get().yScroll = 0.0f;

        get().xPrevious = get().xPosition;
        get().yPrevious = get().yPosition;

        get().xWorldPrevious = get().xWorldPosition;
        get().yWorldPrevious = get().yWorldPosition;
    }


    // - - - | Getters | - - -


    // - - - Positions - - -

    public static float getX()
    {
        return (float) get().xPosition;
    }

    public static float getY()
    {
        return (float) get().yPosition;
    }


    // - - - Net Movement - - -

    public static float getDeltaX()
    {
        return (float) (get().xPrevious - get().xPosition);
    }

    public static float getDeltaY()
    {
        return (float) (get().yPrevious - get().yPosition);
    }


    // - - - World Space - - -

    // - - - get coordinates
    public static float getWorldX()
    {
        return (float) get().xWorldPosition;
    }

    public static float getWorldY()
    {
        return (float) get().yWorldPosition;
    }

    // - - - delta
    public static float getWorldDeltaX()
    {
        return (float) (get().xWorldPrevious - get().xWorldPosition);
    }

    public static float getWorldDeltaY()
    {
        return (float) (get().yWorldPrevious - get().yWorldPosition);
    }

    // - - - set coordinates
    private static void calcWorldX()
    {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        Matrix4f viewProjection = new Matrix4f();
        worldCamera.getInverseViewMatrix().mul(worldCamera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        get().xWorldPosition = temp.x;
    }


    public static void calcWorldY()
    {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().gameViewportSize.y) * 2.0f - 1.0f);
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        Matrix4f viewProjection = new Matrix4f();
        worldCamera.getInverseViewMatrix().mul(worldCamera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        get().yWorldPosition = temp.y;
    }


    // - - - Screen Space - - -

    public static float getScreenX(int SCREEN_WIDTH)
    {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().gameViewportSize.x) * SCREEN_WIDTH;
        return currentX;
    }

    public static float getScreenY(int SCREEN_HEIGHT)
    {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = SCREEN_HEIGHT - ((currentY / get().gameViewportSize.y) * SCREEN_HEIGHT);
        return currentY;
    }

    // - - - Scroll - - -

    public static float getScrollX()
    {
        return (float) get().xScroll;
    }

    public static float getScrollY()
    {
        return (float) get().yScroll;
    }

    public static boolean isDragging()
    {
        return get().isDragging;
    }


    // - - - Mouse buttons - - -

    public static boolean isMouseButtonDown(int BUTTON)
    {
        if (BUTTON >= get().isMouseButtonPressed.length)
        {
            return false;
        }

        return get().isMouseButtonPressed[BUTTON];
    }

    public static boolean isMouseButtonDown(MouseButtons MOUSE_BUTTON)
    {
        int BUTTON = MOUSE_BUTTON.buttonCode;
        return isMouseButtonDown(BUTTON);
    }

    public static boolean isAnyMouseButtonDown()
    {
        for (boolean b : mouse.isMouseButtonPressed)
        {
            if (b) return b;
        }
        return false;
    }


    // - - - View port - - -

    public static void setGameViewport(Vector2f GAME_VIEWPORT_POS, Vector2f GAME_VIEWPORT_SIZE)
    {
        get().gameViewportPos.set(GAME_VIEWPORT_POS);
        get().gameViewportSize.set(GAME_VIEWPORT_SIZE);
    }


    // - - - clear
    public static void clear()
    {
        get().xScroll = 0.0f;
        get().yScroll = 0.0f;
        get().xPosition = 0.0f;
        get().yPosition = 0.0f;
        get().xPrevious = 0.0f;
        get().yPrevious = 0.0f;
        get().xWorldPosition = 0.0f;
        get().yWorldPosition = 0.0f;
        get().xWorldPrevious = 0.0f;
        get().yWorldPrevious = 0.0f;
        get().mouseButtonDownCount = 0;
        get().isDragging = false;
        Arrays.fill(get().isMouseButtonPressed, false);
    }

    // - - - set camera
    public static void setCamera(Camera CAMERA)
    {
        Logger.FORGE_LOG_DEBUG("Switching world camera");
        if (CAMERA == null)
        {
            Logger.FORGE_LOG_ERROR("Cannot assign null as world camera");
            worldCamera = new Camera(new Vector2f());
            return;
        }
        worldCamera = CAMERA;
    }

    public static Camera getWorldCamera()
    {
        return worldCamera;
    }
}