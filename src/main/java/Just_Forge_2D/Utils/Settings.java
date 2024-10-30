package Just_Forge_2D.Utils;

import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.RenderingSystem.TextureMaximizeFilter;
import Just_Forge_2D.RenderingSystem.TextureMinimizeFilter;
import Just_Forge_2D.RenderingSystem.TextureWrapping;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImVec2;
import imgui.ImVec4;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Settings
{
    private static Settings instance = new Settings();

    // - - - Grid
    private final float GRID_WIDTH = 1f;
    public static float GRID_WIDTH() {return instance.GRID_WIDTH;}
    private final float GRID_HEIGHT = 1f;
    public static float GRID_HEIGHT() {return instance.GRID_HEIGHT;}
    private final boolean SHOW_GRID = true;
    public static boolean SHOW_GRID() {return instance.SHOW_GRID;}

    // - - - Render Batching
    private final int MAX_BATCH_SIZE = 1024;
    public static int MAX_BATCH_SIZE() {return instance.MAX_BATCH_SIZE;}

    // - - - Debug Pencil
    private final int DEBUG_PENCIL_MAX_LINES = 32768;
    public static int DEBUG_PENCIL_MAX_LINES() { return instance.DEBUG_PENCIL_MAX_LINES;}
    private final int DEBUG_PENCIL_DEFAULT_LIFE = 1;
    public static int DEBUG_PENCIL_DEFAULT_LIFE() {return instance.DEBUG_PENCIL_DEFAULT_LIFE;}
    private final Vector3f DEBUG_PENCIL_DEFAULT_COLOR = new Vector3f(1, 1, 1);
    public static Vector3f DEBUG_PENCIL_DEFAULT_COLOR() {return instance.DEBUG_PENCIL_DEFAULT_COLOR;}
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
    public static final boolean DARK_MODE_ENABLED = false;
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

    // - - - Texture
    private final TextureWrapping DEFAULT_TEXTURE_WRAP_S = TextureWrapping.REPEAT;
    private final TextureWrapping DEFAULT_TEXTURE_WRAP_T = TextureWrapping.REPEAT;
    private final TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER = TextureMinimizeFilter.LINEAR;
    private final TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER = TextureMaximizeFilter.LINEAR;
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_S() { return instance.DEFAULT_TEXTURE_WRAP_S;}
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_T() { return instance.DEFAULT_TEXTURE_WRAP_T;}
    public static TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER() { return instance.DEFAULT_TEXTURE_MIN_FILTER;}
    public static TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER() { return instance.DEFAULT_TEXTURE_MAX_FILTER;}

    // - - - Log files
    private final int MAX_LOG_FILE_LIMIT = 15;
    public static int MAX_LOG_FILE_LIMIT() { return instance.MAX_LOG_FILE_LIMIT;}

    // - - - Max Display Size
    public static final float MAX_IMAGE_DISPLAY_WIDTH = 64;
    public static final float MAX_IMAGE_DISPLAY_HEIGHT = 64;

    // - - - Text Component:
    private final String DEFAULT_TEXT = "FORGE";
    public static String DEFAULT_TEXT() { return instance.DEFAULT_TEXT;}

    private final float DEFAULT_CHARACTER_SPACING = 1f;
    public static float DEFAULT_CHARACTER_SPACING() {return instance.DEFAULT_CHARACTER_SPACING;}

    private final float DEFAULT_TAB_SPACING = 4f;
    public static float DEFAULT_TAB_SPACING() {return instance.DEFAULT_TAB_SPACING;}

    private final float DEFAULT_LINE_HEIGHT = 1f;
    public static float DEFAULT_LINE_HEIGHT() {return instance.DEFAULT_LINE_HEIGHT;}

    private final float DEFAULT_TEXT_SIZE = 1f;
    public static float DEFAULT_TEXT_SIZE() {return instance.DEFAULT_TEXT_SIZE;}

    private final boolean DEFAULT_TEXT_MOVE_WITH_MASTER = false;
    public static boolean DEFAULT_TEXT_MOVE_WITH_MASTER() {return instance.DEFAULT_TEXT_MOVE_WITH_MASTER;}

    private final int DEFAULT_TEXT_LAYER = 1;
    public static int DEFAULT_TEXT_LAYER() {return instance.DEFAULT_TEXT_LAYER;}

    private final Vector4f DEFAULT_CHARACTER_COLOR = new Vector4f(1.0f);
    public static Vector4f DEFAULT_CHARACTER_COLOR() {return instance.DEFAULT_CHARACTER_COLOR;}

    public static void save()
    {
        Gson gson = new GsonBuilder().create();

        String json = gson.toJson(instance);
        try
        {
            FileWriter writer = new FileWriter("Settings.json");
            writer.write(json);
            writer.close();
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_FATAL(e.getMessage());
        }
    }

    public static void load()
    {
        try
        {
            String json = new String(Files.readAllBytes(Paths.get("Settings.json")));
            Gson gson = new GsonBuilder().create();
            instance = gson.fromJson(json, Settings.class);
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_ERROR(e.getMessage());
            Logger.FORGE_LOG_ERROR("Reverting to Default Settings");
        }
    }
}