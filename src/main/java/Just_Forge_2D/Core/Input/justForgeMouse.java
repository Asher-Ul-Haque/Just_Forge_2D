package Just_Forge_2D.Core.Input;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class justForgeMouse
{
    private static justForgeMouse mouse;
    private double xScroll, yScroll;
    private double xPosition, yPosition;
    private double xPrevious, yPrevious;
    private boolean isMouseButtonPressed[] = new boolean[3]; // left, middle, right
    private boolean isDraggin;

    private justForgeMouse()
    {
        this.xScroll   = 0.0;
        this.yScroll   = 0.0;
        this.xPosition = 0.0f;
        this.yPosition = 0.0f;
        this.xPrevious = 0.0f;
        this.yPrevious = 0.0f;
    }

    public static void init()
    {
        if (justForgeMouse.mouse == null)
        {
            justForgeMouse.mouse = new justForgeMouse();
        }
    }

    public static void mousePositionCallback(long WINDOW, double X_POSITION, double Y_POSITION)
    {
        // make sure the mouse exists
        assert justForgeMouse.mouse != null;

        // save off mouse state
        justForgeMouse.mouse.xPrevious = justForgeMouse.mouse.xPosition;
        justForgeMouse.mouse.yPrevious = justForgeMouse.mouse.yPosition;

        // save the new mouse state
        justForgeMouse.mouse.yPosition = Y_POSITION;
        justForgeMouse.mouse.xPosition = X_POSITION;

        justForgeMouse.mouse.isDraggin = justForgeMouse.mouse.isMouseButtonPressed[0] || justForgeMouse.mouse.isMouseButtonPressed[1] || justForgeMouse.mouse.isMouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long WINDOW, int BUTTON, int ACTION, int MODIFIER)
    {
        // make sure the mouse exists
        assert justForgeMouse.mouse != null;

        if (BUTTON >= justForgeMouse.mouse.isMouseButtonPressed.length)
        {
            return;
        }

        switch(ACTION)
        {
            case GLFW_PRESS:
                justForgeMouse.mouse.isMouseButtonPressed[BUTTON] = true;
                break;

            case GLFW_RELEASE:
                justForgeMouse.mouse.isMouseButtonPressed[BUTTON] = false;
                justForgeMouse.mouse.isDraggin = false;
                break;

            default:
                break;
        }
    }

    public static void mouseScrollCallback(long WINDOW, double X_OFFSET, double Y_OFFSET)
    {
        assert justForgeMouse.mouse != null;

        justForgeMouse.mouse.xScroll = X_OFFSET;
        justForgeMouse.mouse.yScroll = Y_OFFSET;
    }

    public static void endFrame()
    {
        assert justForgeMouse.mouse != null;

        justForgeMouse.mouse.xScroll = 0.0f;
        justForgeMouse.mouse.yScroll = 0.0f;

        justForgeMouse.mouse.xPrevious = justForgeMouse.mouse.xPosition;
        justForgeMouse.mouse.yPrevious = justForgeMouse.mouse.yPosition;
    }

    public static double getX()
    {
        assert justForgeMouse.mouse != null;
        return justForgeMouse.mouse.xPosition;
    }

    public static double getY()
    {
        assert justForgeMouse.mouse != null;
        return justForgeMouse.mouse.yPosition;
    }


    public static double getDeltaX()
    {
        assert justForgeMouse.mouse != null;
        return (justForgeMouse.mouse.xPrevious - justForgeMouse.mouse.xPosition);
    }

    public static double getDeltaY()
    {
        assert justForgeMouse.mouse != null;
        return (justForgeMouse.mouse.yPrevious - justForgeMouse.mouse.yPosition);
    }

    public static double getScrollX()
    {
        assert justForgeMouse.mouse != null;
        return justForgeMouse.mouse.xScroll;
    }

    public static double getScrollY()
    {
        assert justForgeMouse.mouse != null;
        return justForgeMouse.mouse.yScroll;
    }

    public static boolean isDragging()
    {
        assert justForgeMouse.mouse != null;
        return justForgeMouse.mouse.isDraggin;
    }

    public static boolean isMouseButtonDown(int BUTTON)
    {
        assert justForgeMouse.mouse != null;
        if (BUTTON >= justForgeMouse.mouse.isMouseButtonPressed.length)
        {
            return false;
        }

        return justForgeMouse.mouse.isMouseButtonPressed[BUTTON];
    }
}