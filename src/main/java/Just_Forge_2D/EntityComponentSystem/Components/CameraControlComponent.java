package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CameraControlComponent extends Component
{

    private static Camera camera;
    private static List<Vector2f> points = new ArrayList<>();
    private static float lerpFactor = 0.05f;    // Controls how smoothly the camera moves
    private static float zoomLerpFactor = 0.1f; // Controls how smoothly the camera zooms
    private static float minZoom = 0.5f;        // Minimum zoom level (most zoomed in)
    private static float maxZoom = 2.0f;        // Maximum zoom level (most zoomed out)
    private static float totalPoints = 0f;

    @Override
    public void start()
    {
        camera = MainWindow.getCurrentScene().getCamera();
        addPoint(this.gameObject.transform.position);
    }


    public void clearPoints()
    {
        points.clear();
    }


    @Override
    public void update(float DELTA_TIME)
    {
        if (points.isEmpty()) return;
        Vector2f targetPosition = calculateAveragePosition();
        Vector2f currentPosition = camera.getPosition();
        currentPosition.lerp(targetPosition, lerpFactor);
        camera.setPosition(currentPosition);

        float targetZoom = calculateZoom();
        float currentZoom = camera.getZoom();
        camera.setZoom(ForgeMath.lerp(currentZoom, targetZoom, zoomLerpFactor));
    }

    public void removePoint(Vector2f point)
    {
        if (points.remove(point)) totalPoints--;
        Logger.FORGE_LOG_TRACE(totalPoints);
    }

    public void addPoint(Vector2f POINT)
    {
        if (points.add(POINT)) totalPoints++;
    }

    private Vector2f calculateAveragePosition()
    {
        Vector2f sum = ForgeMath.calculateCentroid(points);
        return sum.sub(new Vector2f(camera.getProjectionSize()).mul(camera.getZoom() * 0.5f));
    }

    private float calculateZoom()
    {
        if (points.size() < 2)
        {
            return minZoom;
        }

        float maxDistance = 0f;
        for (int i = 0; i < points.size(); i++)
        {
            for (int j = i + 1; j < points.size(); j++)
            {
                float distance = points.get(i).distance(camera.getPosition());
                maxDistance = Math.max(maxDistance, distance);
            }
        }

        float requiredZoom = maxDistance / Math.max(camera.getProjectionSize().x, camera.getProjectionSize().y);
        return Math.min(maxZoom, Math.max(minZoom, requiredZoom));
    }


    @Override
    public void destroy()
    {
        removePoint(this.gameObject.transform.position);
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        for (Vector2f point : points)
        {
            DebugPencil.addBox(point, new Vector2f(0.1f));
            DebugPencil.addCircle(point, 0.005f);
        }
    }
}
