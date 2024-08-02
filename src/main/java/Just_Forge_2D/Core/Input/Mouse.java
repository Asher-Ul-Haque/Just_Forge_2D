package Just_Forge_2D.Core.Input;

import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.justForgeLogger;
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

    // - - - Mouse Buttons
    private final int buttonCount = 9;
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
            justForgeLogger.FORGE_LOG_INFO("Mouse Input System Online");
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

        get().isDragging = get().isMouseButtonPressed[0] || get().mouse.isMouseButtonPressed[1] || Mouse.mouse.isMouseButtonPressed[2];
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
                get().isDragging = false;
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


    // - - - Orthographic - - -

    public static float getOrthoX()
    {
        float currentX = getX() - get().gameViewportPos.x;
        currentX = (currentX / get().grameViewportSize.x) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        Camera camera = Window.getCurrentScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseViewMatrix().mul(camera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        currentX = temp.x;
        return currentX;
    }

    public static float getOrthoY()
    {
        float currentY = getY() - get().gameViewportPos.y;
        currentY = -((currentY / get().grameViewportSize.y) * 2.0f - 1.0f);
        //float currentY = get().gameViewportPos.y - getY();
        //float currentY = -get().gameViewportPos.y + Window.getHeight() - getY();
//        currentY = (currentY / get().grameViewportSize.y) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        Camera camera = Window.getCurrentScene().getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseViewMatrix().mul(camera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        currentY = temp.y;
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
}