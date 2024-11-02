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
    private static Settings instance = null;
    
    private static Settings getInstance()
    {
        if (instance == null)
        {
            load();
        }
        return instance;
    }

    // - - - Grid
    private final float GRID_WIDTH = 1f;
    public static float GRID_WIDTH() {return getInstance().GRID_WIDTH;}
    private final float GRID_HEIGHT = 1f;
    public static float GRID_HEIGHT() {return getInstance().GRID_HEIGHT;}
    private final boolean SHOW_GRID = true;
    public static boolean SHOW_GRID() {return getInstance().SHOW_GRID;}

    // - - - Render Batching
    private final int MAX_BATCH_SIZE = 1024;
    public static int MAX_BATCH_SIZE() {return getInstance().MAX_BATCH_SIZE;}

    // - - - Debug Pencil
    private final int DEBUG_PENCIL_MAX_LINES = 32768;
    public static int DEBUG_PENCIL_MAX_LINES() { return getInstance().DEBUG_PENCIL_MAX_LINES;}
    private final int DEBUG_PENCIL_DEFAULT_LIFE = 1;
    public static int DEBUG_PENCIL_DEFAULT_LIFE() {return getInstance().DEBUG_PENCIL_DEFAULT_LIFE;}
    private final Vector3f DEBUG_PENCIL_DEFAULT_COLOR = new Vector3f(1, 1, 1);
    public static Vector3f DEBUG_PENCIL_DEFAULT_COLOR() {return getInstance().DEBUG_PENCIL_DEFAULT_COLOR;}
    private final int DEBUG_PENCIL_DEFAULT_WIDTH = 4;
    public static int DEBUG_PENCIL_DEFAULT_WIDTH() {return getInstance().DEBUG_PENCIL_DEFAULT_WIDTH;}
    private final int DEBUG_PENCIL_MAX_CIRCLE_PRECISION = 100;
    public static int DEBUG_PENCIL_MAX_CIRCLE_PRECISION() {return getInstance().DEBUG_PENCIL_MAX_CIRCLE_PRECISION;}
    private final int DEBUG_PENCIL_MIN_CIRCLE_PRECISION = 16;
    public static int DEBUG_PENCIL_MIN_CIRCLE_PRECISION() {return getInstance().DEBUG_PENCIL_MIN_CIRCLE_PRECISION;};
    private final float DEBUG_PENCIL_DEFAULT_ROTATION = 0f;
    public static float DEBUG_PENCIL_DEFAULT_ROTATION() {return getInstance().DEBUG_PENCIL_DEFAULT_ROTATION;}

    // - - - Physics
    private final Vec2 GRAVITY = new Vec2(0, -20f);
    public static Vec2 GRAVITY() { return getInstance().GRAVITY; }
    private final float PHYSICS_DELTA_TIME = 1.0f / 60f;
    public static float PHYSICS_DELTA_TIME() { return getInstance().PHYSICS_DELTA_TIME; }
    private final int VELOCITY_ITERATIONS = 8;
    public static int VELOCITY_ITERATIONS() { return getInstance().VELOCITY_ITERATIONS; }
    private final int POSITION_ITERATIONS = 3;
    public static int POSITION_ITERATIONS() { return getInstance().POSITION_ITERATIONS; }
    private final float LINEAR_DAMPING = 0.9f;
    public static float LINEAR_DAMPING() { return getInstance().LINEAR_DAMPING; }
    private final float ANGULAR_DAMPING = 0.8f;
    public static float ANGULAR_DAMPING() { return getInstance().ANGULAR_DAMPING; }
    private final float DEFAULT_MASS = 1f;
    public static float DEFAULT_MASS() { return getInstance().DEFAULT_MASS; }
    private final boolean ROTATION_FIXED = false;
    public static boolean ROTATION_FIXED() { return getInstance().ROTATION_FIXED; }
    private final boolean CONTINUOUS_COLLISION = false;
    public static boolean CONTINUOUS_COLLISION() { return getInstance().CONTINUOUS_COLLISION; }
    private final float GRAVITY_SCALE = 1.0f;
    public static float GRAVITY_SCALE() { return getInstance().GRAVITY_SCALE; }
    private final float DEFAULT_ROTATION = 0.0f;
    public static float DEFAULT_ROTATION() { return getInstance().DEFAULT_ROTATION; }
    private final float DEFAULT_RESTITUTION = 0.5f;
    public static float DEFAULT_RESTITUTION() { return getInstance().DEFAULT_RESTITUTION; }
    private final float DEFAULT_FRICTION = 0.5f;
    public static float DEFAULT_FRICTION() { return getInstance().DEFAULT_FRICTION; }
    private final float DEFAULT_MAX_WALK_SPEED = 5f;
    public static float DEFAULT_MAX_WALK_SPEED() { return getInstance().DEFAULT_MAX_WALK_SPEED; }
    private final float DEFAULT_GROUND_ACCELERATION = 20.0f;
    public static float DEFAULT_GROUND_ACCELERATION() { return getInstance().DEFAULT_GROUND_ACCELERATION; }
    private final float DEFAULT_GROUND_DECELERATION = 20.0f;
    public static float DEFAULT_GROUND_DECELERATION() { return getInstance().DEFAULT_GROUND_DECELERATION; }
    private final float DEFAULT_AIR_ACCELERATION = 5f;
    public static float DEFAULT_AIR_ACCELERATION() { return getInstance().DEFAULT_AIR_ACCELERATION; }
    private final float DEFAULT_AIR_DECELERATION = 5f;
    public static float DEFAULT_AIR_DECELERATION() { return getInstance().DEFAULT_AIR_DECELERATION; }


    // - - - Transform Component
    private final int DEFAULT_LAYER = 0;
    public static int DEFAULT_LAYER() { return getInstance().DEFAULT_LAYER; }

    // - - - Editor Camera
    private final float DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE = 0.032f;
    public static float DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE() { return getInstance().DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE; }

    private final float DEFAULT_EDITOR_CAMERA_LERP_TIME = 0.0f;
    public static float DEFAULT_EDITOR_CAMERA_LERP_TIME() { return getInstance().DEFAULT_EDITOR_CAMERA_LERP_TIME; }

    private final float DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY = 30.0f;
    public static float DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY() { return getInstance().DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY; }

    private final float DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY = 0.05f;
    public static float DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY() { return getInstance().DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY; }

    private final float DEFAULT_CAMERA_ZOOM = 1f;
    public static float DEFAULT_CAMERA_ZOOM() { return getInstance().DEFAULT_CAMERA_ZOOM; }

    private final Vector2f DEFAULT_CAMERA_PROJECTION_SIZE = new Vector2f(16f, 9f);
    public static Vector2f DEFAULT_CAMERA_PROJECTION_SIZE() { return getInstance().DEFAULT_CAMERA_PROJECTION_SIZE; }

// - - - Window - - -

    // - - - sizes
    private final int DEFAULT_WINDOW_WIDTH = 1980;
    public static int DEFAULT_WINDOW_WIDTH() { return getInstance().DEFAULT_WINDOW_WIDTH; }

    private final int DEFAULT_WINDOW_HEIGHT = 720;
    public static int DEFAULT_WINDOW_HEIGHT() { return getInstance().DEFAULT_WINDOW_HEIGHT; }

    private final float DEFAULT_ASPECT_RATIO = 16f / 9f;
    public static float DEFAULT_ASPECT_RATIO() { return getInstance().DEFAULT_ASPECT_RATIO; }

    private final float DEFAULT_SIZE_DOWN_FACTOR = 16f;
    public static float DEFAULT_SIZE_DOWN_FACTOR() { return getInstance().DEFAULT_SIZE_DOWN_FACTOR; }

    // - - - config
    private final float DEFAULT_FPS = 60.0f;
    public static float DEFAULT_FPS() { return getInstance().DEFAULT_FPS; }

    private final String DEFAULT_WINDOW_TITLE = "Just Forge 2D";
    public static String DEFAULT_WINDOW_TITLE() { return getInstance().DEFAULT_WINDOW_TITLE; }

    private final Vector4f DEFAULT_CLEAR_COLOR = new Vector4f(1.0f);
    public static Vector4f DEFAULT_CLEAR_COLOR() { return getInstance().DEFAULT_CLEAR_COLOR; }

    private final String DEFAULT_ICON_PATH = "Assets/Textures/logo.png";
    public static String DEFAULT_ICON_PATH() { return getInstance().DEFAULT_ICON_PATH; }

    private final boolean DEFAULT_VSYNC_ENABLE = true;
    public static boolean DEFAULT_VSYNC_ENABLE() { return getInstance().DEFAULT_VSYNC_ENABLE; }

    private final boolean DEFAULT_WINDOW_TRANSPARENCY_STATE = false;
    public static boolean DEFAULT_WINDOW_TRANSPARENCY_STATE() { return getInstance().DEFAULT_WINDOW_TRANSPARENCY_STATE; }

    private final boolean DEFAULT_WINDOW_MAXIMIZED_STATE = false;
    public static boolean DEFAULT_WINDOW_MAXIMIZED_STATE() { return getInstance().DEFAULT_WINDOW_MAXIMIZED_STATE; }

    private final boolean DEFAULT_WINDOW_VISIBLE_STATE = true;
    public static boolean DEFAULT_WINDOW_VISIBLE_STATE() { return getInstance().DEFAULT_WINDOW_VISIBLE_STATE; }

    private final boolean DEFAULT_WINDOW_DECORATION_STATE = true;
    public static boolean DEFAULT_WINDOW_DECORATION_STATE() { return getInstance().DEFAULT_WINDOW_DECORATION_STATE; }

    private final boolean DEFAULT_WINDOW_RESIZABLE_STATE = true;
    public static boolean DEFAULT_WINDOW_RESIZABLE_STATE() { return getInstance().DEFAULT_WINDOW_RESIZABLE_STATE; }

    private final boolean DEFAULT_WINDOW_FLOAT_STATUS = false;
    public static boolean DEFAULT_WINDOW_FLOAT_STATUS() { return getInstance().DEFAULT_WINDOW_FLOAT_STATUS; }

    // - - - animation component
    private final float DEFAULT_FRAME_TIME = 0.12f;
    public static float DEFAULT_FRAME_TIME() { return getInstance().DEFAULT_FRAME_TIME; }

    // - - - save directory
    private final String DEFAULT_SAVE_DIR = "/SceneScripts/";
    public static String DEFAULT_SAVE_DIR() { return getInstance().DEFAULT_SAVE_DIR; }

    // - - - keyboard control component
    private final float DEFAULT_JUMP_IMPULSE = 15f;
    public static float DEFAULT_JUMP_IMPULSE() { return getInstance().DEFAULT_JUMP_IMPULSE; }

    private final float DEFAULT_MAX_RUN_SPEED = 7f;
    public static float DEFAULT_MAX_RUN_SPEED() { return getInstance().DEFAULT_MAX_RUN_SPEED; }

    private final float DEFAULT_GROUND_DETECT_RAY_LENGTH = 0.05f;
    public static float DEFAULT_GROUND_DETECT_RAY_LENGTH() { return getInstance().DEFAULT_GROUND_DETECT_RAY_LENGTH; }

    private final float DEFAULT_COYOTE_TIME = 0.1f;
    public static float DEFAULT_COYOTE_TIME() { return getInstance().DEFAULT_COYOTE_TIME; }

    private final int DEFAULT_MAX_JUMPS = 1;
    public static int DEFAULT_MAX_JUMPS() { return getInstance().DEFAULT_MAX_JUMPS; }

    private final Keys DEFAULT_MOVE_RIGHT_KEY = Keys.D;
    public static Keys DEFAULT_MOVE_RIGHT_KEY() { return getInstance().DEFAULT_MOVE_RIGHT_KEY; }

    private final Keys DEFAULT_MOVE_LEFT_KEY = Keys.A;
    public static Keys DEFAULT_MOVE_LEFT_KEY() { return getInstance().DEFAULT_MOVE_LEFT_KEY; }

    private final Keys DEFAULT_RUN_KEY = Keys.CAPS_LOCK;
    public static Keys DEFAULT_RUN_KEY() { return getInstance().DEFAULT_RUN_KEY; }

    private final Keys DEFAULT_JUMP_KEY = Keys.SPACE;
    public static Keys DEFAULT_JUMP_KEY() { return getInstance().DEFAULT_JUMP_KEY; }

    // - - - Texture
    private final TextureWrapping DEFAULT_TEXTURE_WRAP_S = TextureWrapping.REPEAT;
    private final TextureWrapping DEFAULT_TEXTURE_WRAP_T = TextureWrapping.REPEAT;
    private final TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER = TextureMinimizeFilter.NEAREST;
    private final TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER = TextureMaximizeFilter.NEAREST;
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_S() { return getInstance().DEFAULT_TEXTURE_WRAP_S;}
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_T() { return getInstance().DEFAULT_TEXTURE_WRAP_T;}
    public static TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER() { return getInstance().DEFAULT_TEXTURE_MIN_FILTER;}
    public static TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER() { return getInstance().DEFAULT_TEXTURE_MAX_FILTER;}


    // - - - Theme
    private final boolean DARK_MODE_ENABLED = true;
    public static boolean DARK_MODE_ENABLED() { return getInstance().DARK_MODE_ENABLED; }
    private final ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR = new ImVec4(0.129411765f, 0.1450980f, 0.16078f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_SECONDARY_COLOR; }
    private final ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR = new ImVec4(0.97647f, 0.97254902f, 0.968627541f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_PRIMARY_COLOR; }
    private final ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR = new ImVec4(0.203921569f, 0.22745f, 0.2509f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_TERTIARY_COLOR; }
    private final ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR = new ImVec4(0.8747f, 0.892156f, 0.91760f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_QUATERNARY_COLOR; }
    private final float DEFAULT_POPUP_ROUNDING = 4f;
    public static float DEFAULT_POPUP_ROUNDING() { return getInstance().DEFAULT_POPUP_ROUNDING; }
    private final float DEFAULT_WINDOW_BORDER_SIZE = 0.0f;
    public static float DEFAULT_WINDOW_BORDER_SIZE() { return getInstance().DEFAULT_WINDOW_BORDER_SIZE; }
    private final float DEFAULT_WINDOW_ROUNDING = 0.0f;
    public static float DEFAULT_WINDOW_ROUNDING() { return getInstance().DEFAULT_WINDOW_ROUNDING; }
    private final ImVec2 DEFAULT_WINDOW_PADDING = new ImVec2(4.0f, 4.0f);
    public static ImVec2 DEFAULT_WINDOW_PADDING() { return getInstance().DEFAULT_WINDOW_PADDING; }
    private final ImVec2 DEFAULT_FRAME_PADDING = new ImVec2(4.0f, 4.0f);
    public static ImVec2 DEFAULT_FRAME_PADDING() { return getInstance().DEFAULT_FRAME_PADDING; }
    private final float DEFAULT_FRAME_ROUNDING = 4.0f;
    public static float DEFAULT_FRAME_ROUNDING() { return getInstance().DEFAULT_FRAME_ROUNDING; }
    private final float DEFAULT_TREE_NODE_INDENT = 16f;
    public static float DEFAULT_TREE_NODE_INDENT() { return getInstance().DEFAULT_TREE_NODE_INDENT; }
    private final float DEFAULT_TAB_ROUNDING = 4f;
    public static float DEFAULT_TAB_ROUNDING() { return getInstance().DEFAULT_TAB_ROUNDING; }
    private final float DEFAULT_TAB_BORDER = 0f;
    public static float DEFAULT_TAB_BORDER() { return getInstance().DEFAULT_TAB_BORDER; }

    // - - - Log Files
    private final int MAX_LOG_FILE_LIMIT = 15;
    public static int MAX_LOG_FILE_LIMIT() { return getInstance().MAX_LOG_FILE_LIMIT; }

    // - - - Max Display Size
    private final float MAX_IMAGE_DISPLAY_WIDTH = 64;
    public static float MAX_IMAGE_DISPLAY_WIDTH() { return getInstance().MAX_IMAGE_DISPLAY_WIDTH; }
    private final float MAX_IMAGE_DISPLAY_HEIGHT = 64;
    public static float MAX_IMAGE_DISPLAY_HEIGHT() { return getInstance().MAX_IMAGE_DISPLAY_HEIGHT; }

    // - - - Text Component
    private final String DEFAULT_TEXT = "FORGE";
    public static String DEFAULT_TEXT() { return getInstance().DEFAULT_TEXT; }
    private final float DEFAULT_CHARACTER_SPACING = 1f;
    public static float DEFAULT_CHARACTER_SPACING() { return getInstance().DEFAULT_CHARACTER_SPACING; }
    private final float DEFAULT_TAB_SPACING = 4f;
    public static float DEFAULT_TAB_SPACING() { return getInstance().DEFAULT_TAB_SPACING; }
    private final float DEFAULT_LINE_HEIGHT = 1f;
    public static float DEFAULT_LINE_HEIGHT() { return getInstance().DEFAULT_LINE_HEIGHT; }
    private final float DEFAULT_TEXT_SIZE = 1f;
    public static float DEFAULT_TEXT_SIZE() { return getInstance().DEFAULT_TEXT_SIZE; }
    private final boolean DEFAULT_TEXT_MOVE_WITH_MASTER = false;
    public static boolean DEFAULT_TEXT_MOVE_WITH_MASTER() { return getInstance().DEFAULT_TEXT_MOVE_WITH_MASTER; }
    private final int DEFAULT_TEXT_LAYER = 1;
    public static int DEFAULT_TEXT_LAYER() { return getInstance().DEFAULT_TEXT_LAYER; }
    private final Vector4f DEFAULT_CHARACTER_COLOR = new Vector4f(1.0f);
    public static Vector4f DEFAULT_CHARACTER_COLOR() { return getInstance().DEFAULT_CHARACTER_COLOR; }


    public static void save()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(instance);
        try
        {
            FileWriter writer = new FileWriter("Settings.justForgeFile");
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
            String json = new String(Files.readAllBytes(Paths.get("Settings.justForgeFile")));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            instance = gson.fromJson(json, Settings.class);
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_ERROR(e.getMessage());
            Logger.FORGE_LOG_ERROR("Reverting to Default Settings");
            instance = new Settings();
        }
    }
}