package Just_Forge_2D.Utils;

import org.jbox2d.common.Vec2;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DefaultValues
{
    public static final String DEFAULT_WINDOW_TITLE = "Just Forge 2D";
    public static boolean DEFAULT_WINDOW_TRANSPARENCY_STATE = false;
    public static boolean DEFAULT_WINDOW_MAXIMIZED_STATE = false;
    public static boolean DEFAULT_WINDOW_VISIBLE_STATE = true;
    public static boolean DEFAULT_WINDOW_DECORATION_STATE = true;
    public static boolean DEFAULT_WINDOW_RESIZABLE_STATE = true;
    public static boolean DEFAULT_VSYNC_ENABLE = true;
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
    public static final int DEFAULT_WINDOW_WIDTH = 800;
    public static final int DEFAULT_WINDOW_HEIGHT = 720;
    public static final float DEFAULT_ASPECT_RATIO = 16f / 9f;
    public static final int DEFAULT_FPS = 60;
    public static final Vector4f DEFAULT_CLEAR_COLOR = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    public static final boolean DEFAULT_WINDOW_FLOAT_STATUS = false;
    public static final String DEFAULT_ICON_PATH = "Assets/Textures/logo.png";
    public static final float DEFAULT_FRAME_TIME = 0.12f;
    public static final String DEFAULT_SHADER_PATH = "Assets/Shaders/default.glsl";
    public static final String DEFAULT_TEXTURE_PATH = "Assets/Textures/default.png";
    public static final String DEFAULT_EDITOR_LAYOUT_PATH = "Configurations/editorLayout.justForgeFile";
}
