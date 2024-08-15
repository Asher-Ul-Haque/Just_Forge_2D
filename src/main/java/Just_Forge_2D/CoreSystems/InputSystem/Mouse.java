package Just_Forge_2D.CoreSystems.InputSystem;

import Just_Forge_2D.CoreSystems.SceneSystem.Camera;
import Just_Forge_2D.CoreSystems.ForgeDynamo;
import Just_Forge_2D.Utils.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


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

    // - - - Wrold Positions
    private double xWorldPosition, yWorldPosition;
    private double xWorldPrevious, yWorldPrevious;

    // - - - Mouse Buttons
    private final int buttonCount = 9;
    private int mouseButtonDownCount = 0;
    private final boolean[] isMouseButtonPressed = new boolean[buttonCount]; // left, middle, right

    // - - - Relations
    private Vector2f gameViewportPos = new Vector2f();
    private Vector2f grameViewportSize = new Vector2f();


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
        // - - - check if we need to clear the buffer
        if (!ForgeDynamo.getEditor().getGameViewport().getWantCaptureMouse())
        {
            clear();
        }

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
        currentX = (currentX / get().grameViewportSize.x) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        Camera camera = ForgeDynamo.getCurrentScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseViewMatrix().mul(camera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        get().xWorldPosition = temp.x;
    }


    public static void calcWorldY()
    {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().grameViewportSize.y) * 2.0f - 1.0f);
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        Camera camera = ForgeDynamo.getCurrentScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseViewMatrix().mul(camera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        get().yWorldPosition = temp.y;
    }


    // - - - Screen Space - - -

    public static float getScreenX()
    {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().grameViewportSize.x) * 1980;
        return currentX;
    }

    public static float getScreenY()
    {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = 720f - ((currentY / get().grameViewportSize.y) * 720f);
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


    // - - - View port - - -

    public static void setGameViewport(Vector2f GAME_VIEWPORT_POS, Vector2f GAME_VIEWPORT_SIZE)
    {
        get().gameViewportPos.set(GAME_VIEWPORT_POS);
        get().grameViewportSize.set(GAME_VIEWPORT_SIZE);
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
        for (boolean b : get().isMouseButtonPressed)
        {
            b = false;
        }
    }
}