package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CameraControlComponent extends Component
{

    private static Camera camera;
    private static List<Vector2f> points = new ArrayList<>();
    private static float positionLerpFactor = 0.05f;    // Controls how smoothly the camera moves
    private static float zoomLerpFactor = 0.1f; // Controls how smoothly the camera zooms
    private static float minZoom = 0.5f;        // Minimum zoom level (most zoomed in)
    private static float maxZoom = 2.0f;        // Maximum zoom level (most zoomed out)
    private static float totalPoints = 0f;
    private static transient Vector2f absoluteControlPoint = null;
    private static float shakeDuration = 1f;
    private static float shakeIntensity = 0.5f;
    private static float shakeElapsedTime = 0f;
    private static final Random randomizer = new Random();
    private static boolean hasAbsoluteControl = false;

    @Override
    public void start()
    {
        camera = GameWindow.getCurrentScene().getCamera();
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
        clamp();
        Vector2f targetPosition = calculateAveragePosition();
        Vector2f currentPosition = camera.getPosition();
        currentPosition.lerp(targetPosition, positionLerpFactor);
        camera.setPosition(currentPosition);

        float targetZoom = calculateZoom();
        float currentZoom = camera.getZoom();
        camera.setZoom(ForgeMath.lerp(currentZoom, targetZoom, zoomLerpFactor));
        shakeElapsedTime += DELTA_TIME;
    }


    public void removePoint(Vector2f point)
    {
        if (points.remove(point)) totalPoints--;
        Logger.FORGE_LOG_TRACE(totalPoints);
    }

    public void addPoint(Vector2f POINT)
    {
        if (!points.contains(POINT))
        {
            points.add(POINT);
            totalPoints++;
        }
    }

    private Vector2f calculateAveragePosition()
    {
        Vector2f sum = ForgeMath.calculateCentroid(points);
        if (hasAbsoluteControl && absoluteControlPoint != null) return absoluteControlPoint;
        Vector2f point = new Vector2f(sum.sub(new Vector2f(camera.getProjectionSize()).mul(camera.getZoom() * 0.5f)));
        if (shakeElapsedTime < shakeDuration)
        {
            point.add(generateShakeOffset());
        }
        return point;
    }

    public void triggerShake(float DURATION, float INTENSITY)
    {
        shakeDuration = DURATION;
        shakeIntensity = INTENSITY;
        shakeElapsedTime = 0f;
    }

    private Vector2f generateShakeOffset()
    {
        return new Vector2f((randomizer.nextFloat() * 2 - 1), (randomizer.nextFloat() * 2 - 1)).mul(shakeIntensity);
    }

    public void takeAbsoluteControl(Vector2f POINT)
    {
        hasAbsoluteControl = true;
        absoluteControlPoint = POINT;
    }

    public void takeAbsoluteControl()
    {
        takeAbsoluteControl(this.gameObject.transform.position);
    }

    private float calculateZoom()
    {
        if (hasAbsoluteControl) return camera.getZoom();
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

    private void clamp()
    {
        positionLerpFactor = Math.max(0, Math.min(1.0f, positionLerpFactor));
        zoomLerpFactor = Math.max(0f, Math.min(1.0f, zoomLerpFactor));
    }

    @Override
    public void destroy()
    {
        removePoint(this.gameObject.transform.position);
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        clamp();
        shakeElapsedTime += DELTA_TIME;
        super.editorUpdate(DELTA_TIME);
    }

    @Override
    public void debugDraw()
    {
        for (Vector2f point : points)
        {
            DebugPencil.addBox(point, new Vector2f(0.1f));
            DebugPencil.addCircle(point, 0.005f);
        }
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();
        if (ImGui.button("Screen Shake"))
        {
            this.triggerShake(shakeDuration, shakeIntensity);
        }
    }
}