package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.SceneSystem.Camera;
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

    public CameraControlComponent()
    {
    }

    public void clearPoints()
    {
        points.clear();
    }


    @Override
    public void update(float DELTA_TIME)
    {
        if (points.isEmpty()) return;
        camera.position = calculateAveragePosition();
//        camera.setZoom(calculateZoom());
    }


    // Remove a point from tracking
    public void removePoint(Vector2f point)
    {
        points.remove(point);
        totalPoints--;
        Logger.FORGE_LOG_TRACE(totalPoints);
    }

    public void addPoint(Vector2f POINT)
    {
        points.add(POINT);
        totalPoints++;
    }

    // Calculate the average position of the points
    private Vector2f calculateAveragePosition()
    {
        Vector2f sum = new Vector2f(camera.getProjectionSize()).negate().div(2);
        for (Vector2f point : points)
        {
            sum.add(point);
        }
        return sum.div(points.size());
    }

    // Calculate the appropriate zoom based on the spread of points
    private float calculateZoom()
    {
        if (points.size() < 2)
        {
            return minZoom;
        }

        // Find the furthest distance between points
        float maxDistance = 0f;
        for (int i = 0; i < points.size(); i++)
        {
            for (int j = i + 1; j < points.size(); j++)
            {
                float distance = points.get(i).distance(points.get(j));
                if (distance > maxDistance)
                {
                    maxDistance = distance;
                }
            }
        }

        float targetZoom = maxZoom - (maxDistance / 10f);  // 100f can be adjusted based on world scale
        targetZoom = Math.max(minZoom, Math.min(targetZoom, maxZoom));

        return targetZoom;
    }

    // Helper function to linearly interpolate floats
    private float lerp(float start, float end, float factor) {
        return start + factor * (end - start);
    }

    @Override
    public void destroy()
    {
        removePoint(this.gameObject.transform.position);
    }
}
