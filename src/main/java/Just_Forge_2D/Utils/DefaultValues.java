package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.InputSystem.Keys;
import imgui.ImGui;
import imgui.ImGuiViewport;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiWindowFlags;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class DefaultValues
{
    // - - - Grid
    public static float GRID_WIDTH = 1f;
    public static float GRID_HEIGHT = 1f;
    public static final boolean SHOW_GRID = true;

    // - - - Render Batching
    public static final int MAX_BATCH_SIZE = 1024;

    // - - - Debug Pencil
    public static final int DEBUG_PENCIL_MAX_LINES = 32768;
    public static final int DEBUG_PENCIL_DEFAULT_LIFE = 1;
    public static final Vector3f DEBUG_PENCIL_DEFAULT_COLOR = new Vector3f(1, 1, 1);
    public static final int DEBUG_PENCIL_DEFAULT_WIDTH = 4;
    public static final int DEBUG_PENCIL_MAX_CIRCLE_PRECISION = 100;
    public static final int DEBUG_PENCIL_MIN_CIRCLE_PRECISION = 16;
    public static final float DEBUG_PENCIL_DEFAULT_ROTATION = 0f;

    // - - - Physics
    public static final Vec2 GRAVITY = new Vec2(0, -20f);
    public static final float PHYSICS_DELTA_TIME = 1.0f / 60f;
    public static final int VELOCITY_ITERATIONS = 8;
    public static final int POSITION_ITERATIONS = 3;
    public static final float LINEAR_DAMPING = 0.9f;
    public static final float ANGULAR_DAMPING = 0.8f;
    public static final float DEFAULT_MASS = 1f;
    public static final boolean ROTATION_FIXED = false;
    public static final boolean CONTINUOUS_COLLISION = false;
    public static final float GRAVITY_SCALE = 1.0f;
    public static final float DEFAULT_ROTATION = 0.0f;
    public static final float DEFAULT_RESTITUTION = 0.5f;
    public static final float DEFAULT_FRICTION = 0.5f;
    public static final float DEFAULT_MAX_WALK_SPEED = 5f;
    public static final float DEFAULT_GROUND_ACCELERATION = 20.0f;
    public static final float DEFAULT_GROUND_DECELERATION = 20.0f;
    public static final float DEFAULT_AIR_ACCELERATION = 5f;
    public static final float DEFAULT_AIR_DECELERATION = 5f;

    // - - - Transform Component
    public static final int DEFAULT_LAYER = 0;

    // - - - Editor Camera
    public static final float DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE = 0.032f;
    public static final float DEFAULT_EDITOR_CAMERA_LERP_TIME = 0.0f;
    public static final float DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY = 30.0f;
    public static final float DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY = 0.05f;
    public static final float DEFAULT_CAMERA_ZOOM = 1f;
    public static final Vector2f DEFAULT_CAMERA_PROJECTION_SIZE = new Vector2f(16f, 9f);


    // - - - Window - - -

    // - - - sizes
    public static final int DEFAULT_WINDOW_WIDTH = 1980;
    public static final int DEFAULT_WINDOW_HEIGHT = 720;
    public static final float DEFAULT_ASPECT_RATIO = 16f / 9f;
    public static final float DEFAULT_SIZE_DOWN_FACTOR = 16f;

    // - - - config
    public static final float DEFAULT_FPS = 60.0f;
    public static final String DEFAULT_WINDOW_TITLE = "Just Forge 2D";
    public static final Vector4f DEFAULT_CLEAR_COLOR = new Vector4f(1.0f);
    public static final String DEFAULT_ICON_PATH = "Assets/Textures/logo.png";
    public static final boolean DEFAULT_VSYNC_ENABLE = true;
    public static final boolean DEFAULT_WINDOW_TRANSPARENCY_STATE = false;
    public static final boolean DEFAULT_WINDOW_MAXIMIZED_STATE = false;
    public static final boolean DEFAULT_WINDOW_VISIBLE_STATE = true;
    public static final boolean DEFAULT_WINDOW_DECORATION_STATE = true;
    public static final boolean DEFAULT_WINDOW_RESIZABLE_STATE = true;
    public static final boolean DEFAULT_WINDOW_FLOAT_STATUS = false;

    // - - - animation component
    public static final float DEFAULT_FRAME_TIME = 0.12f;

    // - - - save directory
    public static final String DEFAULT_SAVE_DIR = "/SceneScripts/";

    // - - - keyboard control component
    public static final float DEFAULT_JUMP_IMPULSE = 15f;
    public static final float DEFAULT_MAX_RUN_SPEED = 7f;
    public static final float DEFAULT_GROUND_DETECT_RAY_LENGTH = 0.05f;
    public static final float DEFAULT_COYOTE_TIME = 0.1f;
    public static final int DEFAULT_MAX_JUMPS = 1;
    public static final Keys DEFAULT_MOVE_RIGHT_KEY = Keys.D;
    public static final Keys DEFAULT_MOVE_LEFT_KEY = Keys.A;
    public static final Keys DEFAULT_RUN_KEY = Keys.CAPS_LOCK;
    public static final Keys DEFAULT_JUMP_KEY = Keys.SPACE;

    // - - - theme
    public static final boolean DARK_MODE_ENABLED = true;
    public static final ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR = new ImVec4(0.129411765f, 0.1450980f, 0.16078f, 1.0f);
    public static final ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR = new ImVec4(0.97647f, 0.97254902f, 0.968627541f, 1.0f);
    public static final ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR = new ImVec4(0.203921569f, 0.22745f, 0.2509f, 1.0f);
    public static final ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR = new ImVec4(0.8747f, 0.892156f, 0.91760f, 1.0f);
    public static final float DEFAULT_POPUP_ROUNDING = 4f;
    public static final float DEFAULT_WINDOW_BORDER_SIZE = 0.0f;
    public static final float DEFAULT_WINDOW_ROUNDING = 0.0f;
    public static final ImVec2 DEFAULT_WINDOW_PADDING = new ImVec2(4.0f, 4.0f);
    public static final ImVec2 DEFAULT_FRAME_PADDING = new ImVec2(4.0f, 4.0f);
    public static final float DEFAULT_FRAME_ROUNDING = 4.0f;
    public static final float DEFAULT_TREE_NODE_INDENT = 16f;
    public static final float DEFAULT_TAB_ROUNDING = 4f;
    public static final float DEFAULT_TAB_BORDER = 0f;

    // - - - Max Display Size
    public static final float MAX_IMAGE_DISPLAY_WIDTH = 64;
    public static final float MAX_IMAGE_DISPLAY_HEIGHT = 64;


    private static boolean showWindow = true;

    public static void render()
    {
        if (!showWindow) return;

        // - - - Get the viewport size (screen dimensions)
        ImGuiViewport viewport = ImGui.getMainViewport();
        float screenWidth = viewport.getWorkSizeX();
        float screenHeight = viewport.getWorkSizeY();

        // - - - Calculate the center position
        float windowWidth = 800;  // Set the window width
        float windowHeight = 600; // Set the window height
        float posX = (screenWidth - windowWidth) / 2.0f;
        float posY = (screenHeight - windowHeight) / 2.0f;

        // - - - Set the next window position to the center of the screen
        ImGui.setNextWindowPos(posX, posY);
        ImGui.setNextWindowSize(windowWidth, windowHeight);

        if (ImGui.begin("Settings", ImGuiWindowFlags.NoCollapse))
        {
            ImGui.setCursorPosX(windowWidth - 64);
            if (ImGui.button(" X "))
            {
                showWindow = false;
            }
            gridSettings();
        }

        ImGui.end();
    }

    private static void gridSettings()
    {
        if (ImGui.collapsingHeader("Grid Settings"))
        {
            Vector2f size = new Vector2f(GRID_WIDTH, GRID_HEIGHT);
            Widgets.drawVec2Control("Grid Size", size);
            GRID_WIDTH = Math.max(0.1f, size.x);
            GRID_HEIGHT = Math.max(0.1f, size.y);
        }
    }
}