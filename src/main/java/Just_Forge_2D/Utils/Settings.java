package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
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
    private  float GRID_WIDTH = 1f;
    public static float GRID_WIDTH() {return getInstance().GRID_WIDTH;}
    private  float GRID_HEIGHT = 1f;
    public static float GRID_HEIGHT() {return getInstance().GRID_HEIGHT;}
    private  boolean SHOW_GRID = true;
    public static boolean SHOW_GRID() {return getInstance().SHOW_GRID;}

    // - - - Render Batching
    private  int MAX_BATCH_SIZE = 1024;
    public static int MAX_BATCH_SIZE() {return getInstance().MAX_BATCH_SIZE;}

    // - - - Debug Pencil
    private  int DEBUG_PENCIL_MAX_LINES = 32768;
    public static int DEBUG_PENCIL_MAX_LINES() { return getInstance().DEBUG_PENCIL_MAX_LINES;}
    private  int DEBUG_PENCIL_DEFAULT_LIFE = 1;
    public static int DEBUG_PENCIL_DEFAULT_LIFE() {return getInstance().DEBUG_PENCIL_DEFAULT_LIFE;}
    private  Vector3f DEBUG_PENCIL_DEFAULT_COLOR = new Vector3f(1, 1, 1);
    public static Vector3f DEBUG_PENCIL_DEFAULT_COLOR() {return getInstance().DEBUG_PENCIL_DEFAULT_COLOR;}
    private  int DEBUG_PENCIL_DEFAULT_WIDTH = 4;
    public static int DEBUG_PENCIL_DEFAULT_WIDTH() {return getInstance().DEBUG_PENCIL_DEFAULT_WIDTH;}
    private  int DEBUG_PENCIL_MAX_CIRCLE_PRECISION = 100;
    public static int DEBUG_PENCIL_MAX_CIRCLE_PRECISION() {return getInstance().DEBUG_PENCIL_MAX_CIRCLE_PRECISION;}
    private  int DEBUG_PENCIL_MIN_CIRCLE_PRECISION = 16;
    public static int DEBUG_PENCIL_MIN_CIRCLE_PRECISION() {return getInstance().DEBUG_PENCIL_MIN_CIRCLE_PRECISION;};
    private  float DEBUG_PENCIL_DEFAULT_ROTATION = 0f;
    public static float DEBUG_PENCIL_DEFAULT_ROTATION() {return getInstance().DEBUG_PENCIL_DEFAULT_ROTATION;}

    // - - - Physics
    private  Vec2 GRAVITY = new Vec2(0, -20f);
    public static Vec2 GRAVITY() { return getInstance().GRAVITY; }
    private  float PHYSICS_DELTA_TIME = 1.0f / 60f;
    public static float PHYSICS_DELTA_TIME() { return getInstance().PHYSICS_DELTA_TIME; }
    private  int VELOCITY_ITERATIONS = 8;
    public static int VELOCITY_ITERATIONS() { return getInstance().VELOCITY_ITERATIONS; }
    private  int POSITION_ITERATIONS = 3;
    public static int POSITION_ITERATIONS() { return getInstance().POSITION_ITERATIONS; }
    private  float LINEAR_DAMPING = 0.9f;
    public static float LINEAR_DAMPING() { return getInstance().LINEAR_DAMPING; }
    private  float ANGULAR_DAMPING = 0.8f;
    public static float ANGULAR_DAMPING() { return getInstance().ANGULAR_DAMPING; }
    private  float DEFAULT_MASS = 1f;
    public static float DEFAULT_MASS() { return getInstance().DEFAULT_MASS; }
    private  boolean ROTATION_FIXED = false;
    public static boolean ROTATION_FIXED() { return getInstance().ROTATION_FIXED; }
    private  boolean CONTINUOUS_COLLISION = false;
    public static boolean CONTINUOUS_COLLISION() { return getInstance().CONTINUOUS_COLLISION; }
    private  float GRAVITY_SCALE = 1.0f;
    public static float GRAVITY_SCALE() { return getInstance().GRAVITY_SCALE; }
    private  float DEFAULT_ROTATION = 0.0f;
    public static float DEFAULT_ROTATION() { return getInstance().DEFAULT_ROTATION; }
    private  float DEFAULT_RESTITUTION = 0.5f;
    public static float DEFAULT_RESTITUTION() { return getInstance().DEFAULT_RESTITUTION; }
    private  float DEFAULT_FRICTION = 0.5f;
    public static float DEFAULT_FRICTION() { return getInstance().DEFAULT_FRICTION; }
    private  float DEFAULT_MAX_WALK_SPEED = 5f;
    public static float DEFAULT_MAX_WALK_SPEED() { return getInstance().DEFAULT_MAX_WALK_SPEED; }
    private  float DEFAULT_GROUND_ACCELERATION = 20.0f;
    public static float DEFAULT_GROUND_ACCELERATION() { return getInstance().DEFAULT_GROUND_ACCELERATION; }
    private  float DEFAULT_GROUND_DECELERATION = 20.0f;
    public static float DEFAULT_GROUND_DECELERATION() { return getInstance().DEFAULT_GROUND_DECELERATION; }
    private  float DEFAULT_AIR_ACCELERATION = 5f;
    public static float DEFAULT_AIR_ACCELERATION() { return getInstance().DEFAULT_AIR_ACCELERATION; }
    private  float DEFAULT_AIR_DECELERATION = 5f;
    public static float DEFAULT_AIR_DECELERATION() { return getInstance().DEFAULT_AIR_DECELERATION; }


    // - - - Transform Component
    private  int DEFAULT_LAYER = 0;
    public static int DEFAULT_LAYER() { return getInstance().DEFAULT_LAYER; }

    // - - - Editor Camera
    private  float DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE = 0.032f;
    public static float DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE() { return getInstance().DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE; }

    private  float DEFAULT_EDITOR_CAMERA_LERP_TIME = 0.0f;
    public static float DEFAULT_EDITOR_CAMERA_LERP_TIME() { return getInstance().DEFAULT_EDITOR_CAMERA_LERP_TIME; }

    private  float DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY = 30.0f;
    public static float DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY() { return getInstance().DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY; }

    private  float DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY = 0.05f;
    public static float DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY() { return getInstance().DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY; }

    private  float DEFAULT_CAMERA_ZOOM = 1f;
    public static float DEFAULT_CAMERA_ZOOM() { return getInstance().DEFAULT_CAMERA_ZOOM; }

    private  Vector2f DEFAULT_CAMERA_PROJECTION_SIZE = new Vector2f(16f, 9f);
    public static Vector2f DEFAULT_CAMERA_PROJECTION_SIZE() { return getInstance().DEFAULT_CAMERA_PROJECTION_SIZE; }

// - - - Window - - -

    // - - - sizes
    private  int DEFAULT_WINDOW_WIDTH = 1980;
    public static int DEFAULT_WINDOW_WIDTH() { return getInstance().DEFAULT_WINDOW_WIDTH; }

    private  int DEFAULT_WINDOW_HEIGHT = 720;
    public static int DEFAULT_WINDOW_HEIGHT() { return getInstance().DEFAULT_WINDOW_HEIGHT; }

    private  float DEFAULT_ASPECT_RATIO = 16f / 9f;
    public static float DEFAULT_ASPECT_RATIO() { return getInstance().DEFAULT_ASPECT_RATIO; }

    private  float DEFAULT_SIZE_DOWN_FACTOR = 16f;
    public static float DEFAULT_SIZE_DOWN_FACTOR() { return getInstance().DEFAULT_SIZE_DOWN_FACTOR; }

    // - - - config
    private  float DEFAULT_FPS = 60.0f;
    public static float DEFAULT_FPS() { return getInstance().DEFAULT_FPS; }

    private  String DEFAULT_WINDOW_TITLE = "Just Forge 2D";
    public static String DEFAULT_WINDOW_TITLE() { return getInstance().DEFAULT_WINDOW_TITLE; }

    private  Vector4f DEFAULT_CLEAR_COLOR = new Vector4f(1.0f);
    public static Vector4f DEFAULT_CLEAR_COLOR() { return getInstance().DEFAULT_CLEAR_COLOR; }

    private  String DEFAULT_ICON_PATH = "Assets/Textures/logo.png";
    public static String DEFAULT_ICON_PATH() { return getInstance().DEFAULT_ICON_PATH; }

    private  boolean DEFAULT_VSYNC_ENABLE = true;
    public static boolean DEFAULT_VSYNC_ENABLE() { return getInstance().DEFAULT_VSYNC_ENABLE; }

    private  boolean DEFAULT_WINDOW_TRANSPARENCY_STATE = false;
    public static boolean DEFAULT_WINDOW_TRANSPARENCY_STATE() { return getInstance().DEFAULT_WINDOW_TRANSPARENCY_STATE; }

    private  boolean DEFAULT_WINDOW_MAXIMIZED_STATE = false;
    public static boolean DEFAULT_WINDOW_MAXIMIZED_STATE() { return getInstance().DEFAULT_WINDOW_MAXIMIZED_STATE; }

    private  boolean DEFAULT_WINDOW_VISIBLE_STATE = true;
    public static boolean DEFAULT_WINDOW_VISIBLE_STATE() { return getInstance().DEFAULT_WINDOW_VISIBLE_STATE; }

    private  boolean DEFAULT_WINDOW_DECORATION_STATE = true;
    public static boolean DEFAULT_WINDOW_DECORATION_STATE() { return getInstance().DEFAULT_WINDOW_DECORATION_STATE; }

    private  boolean DEFAULT_WINDOW_RESIZABLE_STATE = true;
    public static boolean DEFAULT_WINDOW_RESIZABLE_STATE() { return getInstance().DEFAULT_WINDOW_RESIZABLE_STATE; }

    private  boolean DEFAULT_WINDOW_FLOAT_STATUS = false;
    public static boolean DEFAULT_WINDOW_FLOAT_STATUS() { return getInstance().DEFAULT_WINDOW_FLOAT_STATUS; }

    // - - - animation component
    private  float DEFAULT_FRAME_TIME = 0.12f;
    public static float DEFAULT_FRAME_TIME() { return getInstance().DEFAULT_FRAME_TIME; }

    // - - - save directory
    private  String DEFAULT_SAVE_DIR = "/SceneScripts/";
    public static String DEFAULT_SAVE_DIR() { return getInstance().DEFAULT_SAVE_DIR; }

    // - - - keyboard control component
    private  float DEFAULT_JUMP_IMPULSE = 15f;
    public static float DEFAULT_JUMP_IMPULSE() { return getInstance().DEFAULT_JUMP_IMPULSE; }

    private  float DEFAULT_MAX_RUN_SPEED = 7f;
    public static float DEFAULT_MAX_RUN_SPEED() { return getInstance().DEFAULT_MAX_RUN_SPEED; }

    private  float DEFAULT_GROUND_DETECT_RAY_LENGTH = 0.05f;
    public static float DEFAULT_GROUND_DETECT_RAY_LENGTH() { return getInstance().DEFAULT_GROUND_DETECT_RAY_LENGTH; }

    private  float DEFAULT_COYOTE_TIME = 0.1f;
    public static float DEFAULT_COYOTE_TIME() { return getInstance().DEFAULT_COYOTE_TIME; }

    private  int DEFAULT_MAX_JUMPS = 1;
    public static int DEFAULT_MAX_JUMPS() { return getInstance().DEFAULT_MAX_JUMPS; }

    private  Keys DEFAULT_MOVE_RIGHT_KEY = Keys.D;
    public static Keys DEFAULT_MOVE_RIGHT_KEY() { return getInstance().DEFAULT_MOVE_RIGHT_KEY; }

    private  Keys DEFAULT_MOVE_LEFT_KEY = Keys.A;
    public static Keys DEFAULT_MOVE_LEFT_KEY() { return getInstance().DEFAULT_MOVE_LEFT_KEY; }

    private  Keys DEFAULT_RUN_KEY = Keys.CAPS_LOCK;
    public static Keys DEFAULT_RUN_KEY() { return getInstance().DEFAULT_RUN_KEY; }

    private  Keys DEFAULT_JUMP_KEY = Keys.SPACE;
    public static Keys DEFAULT_JUMP_KEY() { return getInstance().DEFAULT_JUMP_KEY; }

    // - - - Texture
    private  TextureWrapping DEFAULT_TEXTURE_WRAP_S = TextureWrapping.REPEAT;
    private  TextureWrapping DEFAULT_TEXTURE_WRAP_T = TextureWrapping.REPEAT;
    private  TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER = TextureMinimizeFilter.LINEAR;
    private  TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER = TextureMaximizeFilter.LINEAR;
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_S() { return getInstance().DEFAULT_TEXTURE_WRAP_S;}
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_T() { return getInstance().DEFAULT_TEXTURE_WRAP_T;}
    public static TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER() { return getInstance().DEFAULT_TEXTURE_MIN_FILTER;}
    public static TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER() { return getInstance().DEFAULT_TEXTURE_MAX_FILTER;}


    // - - - Theme
    private boolean DARK_MODE_ENABLED = true;
    public static boolean DARK_MODE_ENABLED() { return getInstance().DARK_MODE_ENABLED; }
    private  ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR = new ImVec4(0.129411765f, 0.1450980f, 0.16078f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_SECONDARY_COLOR; }
    private  ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR = new ImVec4(0.97647f, 0.97254902f, 0.968627541f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_PRIMARY_COLOR; }
    private  ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR = new ImVec4(0.203921569f, 0.22745f, 0.2509f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_TERTIARY_COLOR; }
    private  ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR = new ImVec4(0.8747f, 0.892156f, 0.91760f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_QUATERNARY_COLOR; }
    private  float DEFAULT_POPUP_ROUNDING = 4f;
    public static float DEFAULT_POPUP_ROUNDING() { return getInstance().DEFAULT_POPUP_ROUNDING; }
    private  float DEFAULT_WINDOW_BORDER_SIZE = 0.0f;
    public static float DEFAULT_WINDOW_BORDER_SIZE() { return getInstance().DEFAULT_WINDOW_BORDER_SIZE; }
    private  float DEFAULT_WINDOW_ROUNDING = 0.0f;
    public static float DEFAULT_WINDOW_ROUNDING() { return getInstance().DEFAULT_WINDOW_ROUNDING; }
    private  ImVec2 DEFAULT_WINDOW_PADDING = new ImVec2(4.0f, 4.0f);
    public static ImVec2 DEFAULT_WINDOW_PADDING() { return getInstance().DEFAULT_WINDOW_PADDING; }
    private  ImVec2 DEFAULT_FRAME_PADDING = new ImVec2(4.0f, 4.0f);
    public static ImVec2 DEFAULT_FRAME_PADDING() { return getInstance().DEFAULT_FRAME_PADDING; }
    private  float DEFAULT_FRAME_ROUNDING = 4.0f;
    public static float DEFAULT_FRAME_ROUNDING() { return getInstance().DEFAULT_FRAME_ROUNDING; }
    private  float DEFAULT_TREE_NODE_INDENT = 16f;
    public static float DEFAULT_TREE_NODE_INDENT() { return getInstance().DEFAULT_TREE_NODE_INDENT; }
    private  float DEFAULT_TAB_ROUNDING = 4f;
    public static float DEFAULT_TAB_ROUNDING() { return getInstance().DEFAULT_TAB_ROUNDING; }
    private  float DEFAULT_TAB_BORDER = 0f;
    public static float DEFAULT_TAB_BORDER() { return getInstance().DEFAULT_TAB_BORDER; }

    // - - - Log Files
    private  int MAX_LOG_FILE_LIMIT = 15;
    public static int MAX_LOG_FILE_LIMIT() { return getInstance().MAX_LOG_FILE_LIMIT; }

    // - - - Max Display Size
    private  float MAX_IMAGE_DISPLAY_WIDTH = 64;
    public static float MAX_IMAGE_DISPLAY_WIDTH() { return getInstance().MAX_IMAGE_DISPLAY_WIDTH; }
    private  float MAX_IMAGE_DISPLAY_HEIGHT = 64;
    public static float MAX_IMAGE_DISPLAY_HEIGHT() { return getInstance().MAX_IMAGE_DISPLAY_HEIGHT; }

    // - - - Text Component
    private  String DEFAULT_TEXT = "FORGE";
    public static String DEFAULT_TEXT() { return getInstance().DEFAULT_TEXT; }
    private  float DEFAULT_CHARACTER_SPACING = 1f;
    public static float DEFAULT_CHARACTER_SPACING() { return getInstance().DEFAULT_CHARACTER_SPACING; }
    private  float DEFAULT_TAB_SPACING = 4f;
    public static float DEFAULT_TAB_SPACING() { return getInstance().DEFAULT_TAB_SPACING; }
    private  float DEFAULT_LINE_HEIGHT = 1f;
    public static float DEFAULT_LINE_HEIGHT() { return getInstance().DEFAULT_LINE_HEIGHT; }
    private  float DEFAULT_TEXT_SIZE = 1f;
    public static float DEFAULT_TEXT_SIZE() { return getInstance().DEFAULT_TEXT_SIZE; }
    private  boolean DEFAULT_TEXT_MOVE_WITH_MASTER = false;
    public static boolean DEFAULT_TEXT_MOVE_WITH_MASTER() { return getInstance().DEFAULT_TEXT_MOVE_WITH_MASTER; }
    private  int DEFAULT_TEXT_LAYER = 1;
    public static int DEFAULT_TEXT_LAYER() { return getInstance().DEFAULT_TEXT_LAYER; }
    private  Vector4f DEFAULT_CHARACTER_COLOR = new Vector4f(1.0f);
    public static Vector4f DEFAULT_CHARACTER_COLOR() { return getInstance().DEFAULT_CHARACTER_COLOR; }


    public static void save()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(instance);
        try (FileWriter globalWriter = new FileWriter("Settings.justForgeFile"); FileWriter localWriter = new FileWriter(EditorSystemManager.projectDir + "/Settings.justForgeFile"))
        {
            globalWriter.write(json);
            localWriter.write(json);

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