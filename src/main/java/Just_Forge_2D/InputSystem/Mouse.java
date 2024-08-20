package Just_Forge_2D.InputSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.Forge;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Logger;
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

    // - - - Mouse Buttons
    private final int buttonCount = GLFW_MOUSE_BUTTON_LAST;
    private int mouseButtonDownCount = 0;
    private final boolean[] isMouseButtonPressed = new boolean[buttonCount]; // left, middle, right



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
        get().xPrevious = get().xPosition;
        get().yPrevious = get().yPosition;

        get().xScroll = 0f;
        get().yScroll = 0f;
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

    public static boolean isMouseButtonDown(MouseButtons BUTTON)
    {
        if (BUTTON.buttonCode >= get().isMouseButtonPressed.length)
        {
            return false;
        }

        return get().isMouseButtonPressed[BUTTON.buttonCode];
    }


    // - - - View port - - -

    // - - - clear
    public static void clear()
    {
        get().xScroll = 0.0f;
        get().yScroll = 0.0f;
        get().xPosition = 0.0f;
        get().yPosition = 0.0f;
        get().xPrevious = 0.0f;
        get().yPrevious = 0.0f;
        get().mouseButtonDownCount = 0;
        get().isDragging = false;
        Arrays.fill(get().isMouseButtonPressed, false);
    }
}