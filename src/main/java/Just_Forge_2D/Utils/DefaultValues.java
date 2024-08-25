package Just_Forge_2D.Utils;

import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class DefaultValues
{
    public static final float GRID_WIDTH = 0.25f;
    public static final float GRID_HEIGHT = 0.25f;
    public static final int MAX_BATCH_SIZE = 1024;
    public static final int DEBUG_PENCIL_MAX_LINES = 512;
    public static final int DEBUG_PENCIL_DEFAULT_LIFE = 1;
    public static final Vector3f DEBUG_PENCIL_DEFAULT_COLOR = new Vector3f(0, 1, 0);
    public static final int DEBUG_PENCIL_DEFAULT_WIDTH = 2;
    public static final int DEBUG_PENCIL_DEFAULT_CIRCLE_PRECISION = 20;
    public static final float DEBUG_PENCIL_DEFAULT_ROTATION = 0f;
    public static final Vec2 GRAVITY = new Vec2(0, -10f);
    public static final float PHYSICS_DELTA_TIME = 1.0f / 60f;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;
    public static final float LINEAR_DAMPING = 0.9f;
    public static final float ANGULAR_DAMPING = 0.8f;
    public static final float DEFAULT_MASS = 0f;
    public static final boolean ROTATION_FIXED = false;
    public static final boolean CONTINUOUS_COLLISION = true;
    public static final float DEFAULT_ROTATION = 0.0f;
    public static final int DEFAULT_LAYER = 0;
    public static final float DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE = 0.032f;
    public static final float DEFAULT_EDITOR_CAMERA_LERP_TIME = 0.0f;
    public static final float DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVTY = 30.0f;
    public static final float DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY = 0.1f;
    public static final float DEFAULT_CAMERA_ZOOM = 1f;
    public static final String DEFAULT_SAVE_FILE = "Configurations/Levels/level.justForgeFile";
    public static final int DEFAULT_WINDOW_WIDTH = 1980;
    public static final int DEFAULT_WINDOW_HEIGHT = 720;
    public static final float DEFAULT_ASPECT_RATIO = 16f / 9f;
    public static final float DEFAULT_FRAME_TIME = 0.12f;
    public static final Vector2f DEFAULT_CAMERA_PROJECTION_SIZE = new Vector2f(16f / 3f, 9f / 3f);
}
