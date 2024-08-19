package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.Forge;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.SceneSystem.Scene;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class EditorSystemManager
{
    public static EditorWindow editorWindow;
    public static Scene editorScene;
    public static final String editorSceneName = "Editor Scene";


    // - - - Relations
    public static Vector2f gameViewportPos = new Vector2f();
    public static Vector2f grameViewportSize = new Vector2f();

    // - - - Wrold Positions
    public static double xWorldPosition, yWorldPosition = 0.0f;
    public static double xWorldPrevious, yWorldPrevious = 0.0f;


    protected static void mouseCallback(long WINDOW, double X_POSITION, double Y_POSITION)
    {
        if (!Forge.editorLayer.getGameViewport().getWantCaptureMouse())
        {
            Mouse.clear();
            xWorldPosition = 0.0f;
            yWorldPosition = 0.0f;
            xWorldPrevious = 0.0f;
            yWorldPrevious = 0.0f;
        }

        Mouse.mousePositionCallback(WINDOW, X_POSITION, Y_POSITION);

        EditorSystemManager.xWorldPrevious = EditorSystemManager.xWorldPosition;
        EditorSystemManager.yWorldPrevious = EditorSystemManager.yWorldPosition;

        EditorSystemManager.calcWorldY();
        EditorSystemManager.calcWorldX();
    }

    public static void endFrame()
    {
        Mouse.endFrame();
        xWorldPosition = xWorldPrevious;
        yWorldPosition = yWorldPrevious;
    }



    // - - - World Space - - -

    // - - - get coordinates
    public static float getWorldX()
    {
        return (float) xWorldPosition;
    }

    public static float getWorldY()
    {
        return (float) yWorldPosition;
    }

    // - - - delta
    public static float getWorldDeltaX()
    {
        return (float) (xWorldPrevious - xWorldPosition);
    }

    public static float getWorldDeltaY()
    {
        return (float) (yWorldPrevious - yWorldPosition);
    }

    // - - - set coordinates
    private static void calcWorldX()
    {
        float currentX = Mouse.getX() - gameViewportPos.x;
        currentX = (currentX / grameViewportSize.x) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        Camera camera = EditorSystemManager.editorScene.getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseViewMatrix().mul(camera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        xWorldPosition = temp.x;
    }


    public static void calcWorldY()
    {
        float currentY = Mouse.getY() - gameViewportPos.y;
        currentY = -((currentY / grameViewportSize.y) * 2.0f - 1.0f);
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        Camera camera = EditorSystemManager.editorScene.getCamera();
        Matrix4f viewProjection = new Matrix4f();
        camera.getInverseViewMatrix().mul(camera.getInverseProjectionMatrix(), viewProjection);
        temp.mul(viewProjection);
        yWorldPosition = temp.y;
    }

    public static void setGameViewport(Vector2f GAME_VIEWPORT_POS, Vector2f GAME_VIEWPORT_SIZE)
    {
        gameViewportPos.set(GAME_VIEWPORT_POS);
        grameViewportSize.set(GAME_VIEWPORT_SIZE);
    }

    // - - - Screen Space - - -

    public static float getScreenX()
    {
        float currentX = Mouse.getX() - gameViewportPos.x;
        currentX = (currentX / grameViewportSize.x) * editorWindow.getWidth();
        return currentX;
    }

    public static float getScreenY()
    {
        float currentY = Mouse.getY() - gameViewportPos.y;
        currentY = 720f - ((currentY / grameViewportSize.y) * editorWindow.getHeight());
        return currentY;
    }
}