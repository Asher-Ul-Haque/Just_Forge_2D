package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CameraControlComponent extends Component
{
    protected class CameraData
    {
        public float positionLerpFactor = 0.1f;
        public float zoomLerpFactor = 0.1f;
        public float minZoom = 0.5f;
        public float maxZoom = 2.0f;
        public float shakeIntensity = 0.5f;
        public float shakeDuration = 1f;
        public Vector2f xLimits = new Vector2f();
        public Vector2f yLimits = new Vector2f();
        public Set<Vector2f> points = new HashSet<>();
    }

    protected static Camera camera;
    protected static Set<Vector2f> points = new HashSet<>();
    protected static float positionLerpFactor = 0.1f;    // Controls how smoothly the camera moves
    protected static float zoomLerpFactor = 0.1f; // Controls how smoothly the camera zooms
    protected static float totalPoints = 0f;
    protected static transient Vector2f absoluteControlPoint = null;
    protected static float shakeDuration = 1f;
    protected static float shakeIntensity = 0.5f;
    protected static transient float shakeElapsedTime = 0f;
    protected static final Random randomizer = new Random();
    protected static boolean hasAbsoluteControl = false;

    // - - - Camera movement limits
    protected static Vector2f xLimits = new Vector2f();
    protected static Vector2f yLimits = new Vector2f();
    protected static boolean limitXEnabled = false;
    protected static boolean limitYEnabled = false;

    // - - - Zoom limits
    protected static float minZoom = 0.5f;        // Minimum zoom level (most zoomed in)
    protected static float maxZoom = 2.0f;        // Maximum zoom level (most zoomed out)
    protected static boolean minZoomEnabled = false;
    protected static boolean maxZoomEnabled = false;

    // - - - Load / Store
    protected static boolean stored = false;
    protected CameraData data = null;
    protected static transient CameraControlComponent activeInstance;

    protected void save()
    {
        this.data = new CameraData();
        this.data.maxZoom = maxZoom;
        this.data.minZoom = minZoom;
        this.data.xLimits = new Vector2f(xLimits);
        this.data.yLimits = new Vector2f(yLimits);
        this.data.shakeDuration = shakeDuration;
        this.data.shakeIntensity = shakeIntensity;
        this.data.positionLerpFactor = positionLerpFactor;
        this.data.zoomLerpFactor = zoomLerpFactor;
        this.data.points = new HashSet<>(points);
    }

    protected void load()
    {
        maxZoom = this.data.maxZoom;
        minZoom = this.data.minZoom;
        xLimits = new Vector2f(this.data.xLimits);
        yLimits = new Vector2f(this.data.yLimits);
        shakeDuration = this.data.shakeDuration;
        shakeIntensity = this.data.shakeIntensity;
        positionLerpFactor = this.data.positionLerpFactor;
        zoomLerpFactor = this.data.zoomLerpFactor;
        points = new HashSet<>(points);
    }

    @Override
    public void start()
    {
        camera = GameWindow.getCurrentScene().getCamera();
        if (activeInstance == null)
        {
            activeInstance = this;
            if (data != null)
            {
                load();
            }
        }
        addPoint(this.gameObject.transform.position);
    }

    @Override
    public void destroy()
    {
        removePoint(this.gameObject.transform.position);
        if (activeInstance == this)
        {
            save();
            activeInstance = null;
        }
    }

    @Override
    public void editorGUI()
    {
        super.deleteButton();
        positionLerpFactor = Widgets.drawFloatControl(Icons.MapPin + "  Position Smoothness", positionLerpFactor);
        zoomLerpFactor = Widgets.drawFloatControl(Icons.ExpandAlt + "  Zoom Smoothness", zoomLerpFactor);
        Widgets.text("");
        shakeDuration = Widgets.drawFloatControl(Icons.Stopwatch + "  Screen Shake Duration", shakeDuration);
        shakeIntensity = Widgets.drawFloatControl(Icons.Bomb + "  Screen Shake Intensity", shakeIntensity);
        if (Widgets.button(Icons.CameraRetro + "  Screen Shake")) this.triggerShake(shakeDuration, shakeIntensity);
        Widgets.text("");
        if (minZoomEnabled) minZoom = Widgets.drawFloatControl(Icons.Camera + "  Minimum Zoom", minZoom);
        if (maxZoomEnabled) maxZoom = Widgets.drawFloatControl(Icons.Camera + "  Maximum Zoom", maxZoom);
        minZoomEnabled = Widgets.drawBoolControl(Icons.Expand + "  Limit Minimum Zoom", minZoomEnabled);
        maxZoomEnabled = Widgets.drawBoolControl(Icons.Expand + "  Limit Maximum Zoom", maxZoomEnabled);
        Widgets.text("");
        if (limitXEnabled) Widgets.drawVec2Control(Icons.Crosshairs + "  X Limits", xLimits);
        if (limitYEnabled) Widgets.drawVec2Control(Icons.Crosshairs + "  Y Limits", yLimits);
        limitXEnabled = Widgets.drawBoolControl(Icons.Crosshairs + "  Limit X Movement", limitXEnabled);
        limitYEnabled = Widgets.drawBoolControl(Icons.Crosshairs + "  Limit Y Movement", limitYEnabled);

        if (activeInstance == this) save();
    }

    public void clearPoints() {points.clear();}

    @Override
    public void update(float DELTA_TIME)
    {
        if (points.isEmpty()) return;
        clamp();
        Vector2f targetPosition = calculateAveragePosition();
        Vector2f currentPosition = camera.getPosition();

        if (limitXEnabled) targetPosition.x = Math.max(xLimits.x, Math.min(xLimits.y, targetPosition.x));
        if (limitYEnabled) targetPosition.y = Math.max(yLimits.x, Math.min(yLimits.y, targetPosition.y));

        currentPosition.lerp(targetPosition, positionLerpFactor);
        camera.setPosition(currentPosition);

        float targetZoom = calculateZoom();
        float currentZoom = camera.getZoom();

        if (minZoomEnabled) targetZoom = Math.max(minZoom, targetZoom);
        if (maxZoomEnabled) targetZoom = Math.min(maxZoom, targetZoom);

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

    protected Vector2f calculateAveragePosition()
    {
        Vector2f sum = ForgeMath.calculateCentroid(new ArrayList<>(points));
        if (hasAbsoluteControl && absoluteControlPoint != null) return absoluteControlPoint;
        Vector2f point = new Vector2f(sum.sub(new Vector2f(camera.getProjectionSize()).mul(camera.getZoom() * 0.5f)));
        if (shakeElapsedTime < shakeDuration) point.add(generateShakeOffset());
        return point;
    }

    public void triggerShake(float DURATION, float INTENSITY)
    {
        shakeDuration = DURATION;
        shakeIntensity = INTENSITY;
        shakeElapsedTime = 0f;
    }

    protected Vector2f generateShakeOffset() { return new Vector2f((randomizer.nextFloat() * 2 - 1), (randomizer.nextFloat() * 2 - 1)).mul(shakeIntensity);}

    public void takeAbsoluteControl(Vector2f POINT)
    {
        hasAbsoluteControl = true;
        absoluteControlPoint = POINT;
    }

    public void takeAbsoluteControl() {takeAbsoluteControl(this.gameObject.transform.position);}

    protected float calculateZoom()
    {
        if (hasAbsoluteControl || points.size() < 2) return minZoom;

        float maxDistance = 0f;
        for (Vector2f point1 : points) {
            for (Vector2f point2 : points) {
                if (!point1.equals(point2)) {
                    float distance = point1.distance(camera.getPosition());
                    maxDistance = Math.max(maxDistance, distance);
                }
            }
        }

        return (maxDistance) / Math.max(camera.getProjectionSize().x, camera.getProjectionSize().y);
    }

    protected void clamp()
    {
        positionLerpFactor = Math.max(0, Math.min(1.0f, positionLerpFactor));
        zoomLerpFactor = Math.max(0f, Math.min(1.0f, zoomLerpFactor));
    }

    public void setCameraLimits(Vector2f X_LIMITS, Vector2f Y_LIMITS, boolean LIMIT_X, boolean LIMIT_Y)
    {
        xLimits = X_LIMITS;
        yLimits = Y_LIMITS;
        limitXEnabled = LIMIT_X;
        limitYEnabled = LIMIT_Y;
    }

    // Set zoom limit settings
    public void setZoomLimits(boolean ENABLE_MIN_ZOOM_LIMITS, boolean ENABLE_MAX_ZOOM_LIMITS)
    {
        minZoomEnabled = ENABLE_MIN_ZOOM_LIMITS;
        maxZoomEnabled = ENABLE_MAX_ZOOM_LIMITS;
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        clamp();
        shakeElapsedTime += DELTA_TIME;
        super.editorUpdate(DELTA_TIME);
    }

    public void debugDraw()
    {
        for (Vector2f point : points)
        {
            DebugPencil.addBox(point, new Vector2f(0.1f));
            DebugPencil.addCircle(point, 0.005f);
        }
    }
}