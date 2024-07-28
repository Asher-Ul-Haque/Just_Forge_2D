package Just_Forge_2D.Core.Input;

import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.justForgeLogger;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


// - - - Mouse Input class
public class justForgeMouse
{
    // - - - Private Variables - - -

    // - - - Singleton
    private static justForgeMouse mouse;

    // - - - Mouse Scrolling
    private double xScroll, yScroll;
    private boolean isDraggin;

    // - - - Mouse Positions
    private double xPosition, yPosition;
    private double xPrevious, yPrevious;

    // - - - Mouse BUttons
    private boolean isMouseButtonPressed[] = new boolean[9]; // left, middle, right


    // - - - | Functions | - - -

    // - - - Singleton Constructor
    private justForgeMouse()
    {
        this.xScroll   = 0.0;
        this.yScroll   = 0.0;

        this.xPosition = 0.0f;
        this.yPosition = 0.0f;

        this.xPrevious = 0.0f;
        this.yPrevious = 0.0f;
    }

    // - - - Setup mouse
    public static justForgeMouse get()
    {
        if (justForgeMouse.mouse == null)
        {
            justForgeMouse.mouse = new justForgeMouse();
            justForgeLogger.FORGE_LOG_INFO("Mouse Input System Online");
        }
        return justForgeMouse.mouse;
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

        get().isDraggin = get().isMouseButtonPressed[0] || get().mouse.isMouseButtonPressed[1] || justForgeMouse.mouse.isMouseButtonPressed[2];
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
                break;

            case GLFW_RELEASE:
                get().isMouseButtonPressed[BUTTON] = false;
                get().isDraggin = false;
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
    }

    // - - - Getters - - -

    // - - - Positions
    public static float getX()
    {
        return (float) get().xPosition;
    }

    public static float getY()
    {
        return (float) get().yPosition;
    }


    // - - - Net Movement
    public static float getDeltaX()
    {
        return (float) (get().xPrevious - get().xPosition);
    }

    public static float getDeltaY()
    {
        return (float) (get().yPrevious - get().yPosition);
    }


    // - - - Ortho graphic
    public static float getOrthoX()
    {
        float currentX = getX();
        currentX = (currentX / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        temp.mul(Window.getCurrentScene().getCamera().getInverseProjectionMatrix()).mul(Window.getCurrentScene().getCamera().getInverseViewMatrix());
        currentX = temp.x;
        return -1;
    }

    public static float getOrthoY()
    {
        float currentY = getY();
        currentY = (currentY / (float) Window.getWidth()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        temp.mul(Window.getCurrentScene().getCamera().getInverseProjectionMatrix()).mul(Window.getCurrentScene().getCamera().getInverseViewMatrix());
        currentY = temp.y;
        justForgeLogger.FORGE_LOG_TRACE(currentY);

        return -1;
    }


    // - - - Scroll
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
        return get().isDraggin;
    }


    // - - - Mouse buttons
    public static boolean isMouseButtonDown(int BUTTON)
    {
        if (BUTTON >= get().isMouseButtonPressed.length)
        {
            return false;
        }

        return get().isMouseButtonPressed[BUTTON];
    }
}