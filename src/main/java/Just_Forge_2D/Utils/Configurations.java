package Just_Forge_2D.Utils;

import org.jbox2d.common.Vec2;
import org.joml.Vector3f;

public class Configurations
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
}
