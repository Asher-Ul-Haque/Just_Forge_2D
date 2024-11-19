package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.RenderingSystem.TextureMaximizeFilter;
import Just_Forge_2D.RenderingSystem.TextureMinimizeFilter;
import Just_Forge_2D.RenderingSystem.TextureWrapping;
import Just_Forge_2D.Themes.CleanTheme;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
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
    private static boolean popupOpen = false;
    
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

    private float DEFAULT_WINDOW_OPACITY = 1f;
    public static float DEFAULT_WINDOW_OPACITY() { return getInstance().DEFAULT_WINDOW_OPACITY; }

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
    private  TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER = TextureMinimizeFilter.NEAREST;
    private  TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER = TextureMaximizeFilter.NEAREST;
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_S() { return getInstance().DEFAULT_TEXTURE_WRAP_S;}
    public static TextureWrapping DEFAULT_TEXTURE_WRAP_T() { return getInstance().DEFAULT_TEXTURE_WRAP_T;}
    public static TextureMinimizeFilter DEFAULT_TEXTURE_MIN_FILTER() { return getInstance().DEFAULT_TEXTURE_MIN_FILTER;}
    public static TextureMaximizeFilter DEFAULT_TEXTURE_MAX_FILTER() { return getInstance().DEFAULT_TEXTURE_MAX_FILTER;}


    // - - - ThemE
    private boolean DARK_MODE_ENABLED = true;

    public static void toggleDarkMode()
    {
        instance.DARK_MODE_ENABLED = !instance.DARK_MODE_ENABLED;
    }

    public static boolean DARK_MODE_ENABLED() { return getInstance().DARK_MODE_ENABLED; }
    private  ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR = new ImVec4(0.129411765f, 0.1450980f, 0.16078f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_SECONDARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_SECONDARY_COLOR; }
    private  ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR = new ImVec4(0.97647f, 0.97254902f, 0.968627541f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_PRIMARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_PRIMARY_COLOR; }
    private  ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR = new ImVec4(0.203921569f, 0.22745f, 0.2509f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_TERTIARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_TERTIARY_COLOR; }
    private  ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR = new ImVec4(0.8747f, 0.892156f, 0.91760f, 1.0f);
    public static ImVec4 DEFAULT_CLEAN_THEME_QUATERNARY_COLOR() { return getInstance().DEFAULT_CLEAN_THEME_QUATERNARY_COLOR; }
    private  float DEFAULT_POPUP_ROUNDING = 8f;
    public static float DEFAULT_POPUP_ROUNDING() { return getInstance().DEFAULT_POPUP_ROUNDING; }
    private  float DEFAULT_WINDOW_BORDER_SIZE = 0.0f;
    public static float DEFAULT_WINDOW_BORDER_SIZE() { return getInstance().DEFAULT_WINDOW_BORDER_SIZE; }
    private  float DEFAULT_WINDOW_ROUNDING = 8.0f;
    public static float DEFAULT_WINDOW_ROUNDING() { return getInstance().DEFAULT_WINDOW_ROUNDING; }
    private  ImVec2 DEFAULT_WINDOW_PADDING = new ImVec2(4.0f, 4.0f);
    public static ImVec2 DEFAULT_WINDOW_PADDING() { return getInstance().DEFAULT_WINDOW_PADDING; }
    private  ImVec2 DEFAULT_FRAME_PADDING = new ImVec2(4.0f, 4.0f);
    public static ImVec2 DEFAULT_FRAME_PADDING() { return getInstance().DEFAULT_FRAME_PADDING; }
    private  float DEFAULT_FRAME_ROUNDING = 8.0f;
    public static float DEFAULT_FRAME_ROUNDING() { return getInstance().DEFAULT_FRAME_ROUNDING; }
    private  float DEFAULT_TREE_NODE_INDENT = 16f;
    public static float DEFAULT_TREE_NODE_INDENT() { return getInstance().DEFAULT_TREE_NODE_INDENT; }
    private  float DEFAULT_TAB_ROUNDING = 8f;
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

    public static void editorGUI()
    {
        if (popupOpen)
        {
            ImVec4 colorCache = EditorSystemManager.getCurrentTheme().popupBgColor;
            EditorSystemManager.getCurrentTheme().applyPopupBg(EditorSystemManager.getCurrentTheme().windowBgColor);

            // - - - the popup goes here
            switch (Widgets.popUp(Settings::editInstance))
            {
                case OK:
                    save();
                    popupOpen = false;
                    break;

                case CANCEL:
                    load();
                    popupOpen = false;
                    break;

                default:
                    break;
            }
            EditorSystemManager.getCurrentTheme().applyPopupBg(colorCache);
        }
    }

    private static void editInstance()
    {
        if (Widgets.button(Settings.DARK_MODE_ENABLED() ? Icons.Sun : Icons.Moon, true))
        {
            Settings.toggleDarkMode();
            EditorSystemManager.setTheme(new CleanTheme(Settings.DARK_MODE_ENABLED()));
        }
        if (ImGui.beginTabBar("Settings Tabs"))
        {
            if (ImGui.beginTabItem("Grid"))
            {
                getInstance().GRID_HEIGHT = Widgets.drawFloatControl("Height", GRID_HEIGHT());
                getInstance().GRID_WIDTH = Widgets.drawFloatControl("Width", GRID_HEIGHT());
                getInstance().SHOW_GRID = Widgets.drawBoolControl("Show", SHOW_GRID());
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Rendering"))
            {
                getInstance().MAX_BATCH_SIZE = Widgets.drawIntControl("Batch Size", MAX_BATCH_SIZE());
                getInstance().DEBUG_PENCIL_MAX_LINES = Widgets.drawIntControl("Pencil Lines", DEBUG_PENCIL_MAX_LINES());
                getInstance().DEBUG_PENCIL_DEFAULT_LIFE = Widgets.drawIntControl("Line Life", DEBUG_PENCIL_DEFAULT_LIFE());
                getInstance().DEBUG_PENCIL_DEFAULT_WIDTH = Widgets.drawIntControl("Line Width", DEBUG_PENCIL_DEFAULT_WIDTH());
                getInstance().DEBUG_PENCIL_MIN_CIRCLE_PRECISION = Widgets.drawIntControl("Circle Min Precision", DEBUG_PENCIL_MIN_CIRCLE_PRECISION());
                getInstance().DEBUG_PENCIL_MAX_CIRCLE_PRECISION = Widgets.drawIntControl("Circle Max Precision", DEBUG_PENCIL_MAX_CIRCLE_PRECISION());
                getInstance().DEBUG_PENCIL_DEFAULT_ROTATION = Widgets.drawFloatControl("Circle Max Precision", DEBUG_PENCIL_DEFAULT_ROTATION());
                getInstance().DEFAULT_LAYER = Widgets.drawIntControl("Layer", DEFAULT_LAYER());
                Widgets.colorPicker3("Default Color", DEBUG_PENCIL_DEFAULT_COLOR());
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Physics"))
            {
                Vector2f temp = new Vector2f(getInstance().GRAVITY.x, getInstance().GRAVITY.y);
                Widgets.drawVec2Control("Gravity", temp);
                getInstance().GRAVITY = new Vec2(temp.x, temp.y);
                getInstance().PHYSICS_DELTA_TIME = Widgets.drawFloatControl("Delta Time", PHYSICS_DELTA_TIME());
                getInstance().VELOCITY_ITERATIONS = Widgets.drawIntControl("Velocity Iterations", VELOCITY_ITERATIONS());
                getInstance().POSITION_ITERATIONS = Widgets.drawIntControl("Position Iterations", POSITION_ITERATIONS());
                getInstance().LINEAR_DAMPING = Widgets.drawFloatControl("Linear Damping", LINEAR_DAMPING());
                getInstance().ANGULAR_DAMPING = Widgets.drawFloatControl("Angular Damping", ANGULAR_DAMPING());
                getInstance().DEFAULT_MASS = Widgets.drawFloatControl("Mass", DEFAULT_MASS());
                getInstance().GRAVITY_SCALE = Widgets.drawFloatControl("Gravity Scale", GRAVITY_SCALE());
                getInstance().DEFAULT_ROTATION = Widgets.drawFloatControl("Default Rotation", DEFAULT_ROTATION());
                getInstance().DEFAULT_RESTITUTION = Widgets.drawFloatControl("Restitution", DEFAULT_RESTITUTION());
                getInstance().DEFAULT_FRICTION = Widgets.drawFloatControl("Friction", DEFAULT_FRICTION());
                getInstance().DEFAULT_MAX_WALK_SPEED = Widgets.drawFloatControl("Max Walk Speed", DEFAULT_MAX_WALK_SPEED());
                getInstance().DEFAULT_GROUND_ACCELERATION = Widgets.drawFloatControl("Ground Acceleration", DEFAULT_GROUND_ACCELERATION());
                getInstance().DEFAULT_GROUND_DECELERATION = Widgets.drawFloatControl("Ground Deceleration", DEFAULT_GROUND_DECELERATION());
                getInstance().DEFAULT_AIR_ACCELERATION = Widgets.drawFloatControl("Air Acceleration", DEFAULT_AIR_ACCELERATION());
                getInstance().DEFAULT_AIR_DECELERATION = Widgets.drawFloatControl("Air Deceleration", DEFAULT_AIR_DECELERATION());
                getInstance().ROTATION_FIXED = Widgets.drawBoolControl("Fixed Rotation", ROTATION_FIXED());
                getInstance().CONTINUOUS_COLLISION = Widgets.drawBoolControl("Continuous Collision", CONTINUOUS_COLLISION());
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Window Settings"))
            {
                getInstance().DEFAULT_WINDOW_WIDTH = Widgets.drawIntControl("Width", DEFAULT_WINDOW_WIDTH());
                getInstance().DEFAULT_WINDOW_HEIGHT = Widgets.drawIntControl("Height", DEFAULT_WINDOW_HEIGHT());
                getInstance().DEFAULT_ASPECT_RATIO = Widgets.drawFloatControl("Aspect Ratio", DEFAULT_ASPECT_RATIO());
                getInstance().DEFAULT_SIZE_DOWN_FACTOR = Widgets.drawFloatControl("Size Down Factor", DEFAULT_SIZE_DOWN_FACTOR());
                getInstance().DEFAULT_FPS = Widgets.drawFloatControl("FPS", DEFAULT_FPS());
                getInstance().DEFAULT_WINDOW_TITLE = Widgets.inputText("Title", DEFAULT_WINDOW_TITLE());
                Widgets.colorPicker4("Clear Color", DEFAULT_CLEAR_COLOR());
                getInstance().DEFAULT_ICON_PATH = Widgets.inputText("Icon Path", DEFAULT_ICON_PATH());
                getInstance().DEFAULT_VSYNC_ENABLE = Widgets.drawBoolControl("VSync", DEFAULT_VSYNC_ENABLE());
                getInstance().DEFAULT_WINDOW_TRANSPARENCY_STATE = Widgets.drawBoolControl("Transparency", DEFAULT_WINDOW_TRANSPARENCY_STATE());
                getInstance().DEFAULT_WINDOW_MAXIMIZED_STATE = Widgets.drawBoolControl("Maximized", DEFAULT_WINDOW_MAXIMIZED_STATE());
                getInstance().DEFAULT_WINDOW_VISIBLE_STATE = Widgets.drawBoolControl("Visible", DEFAULT_WINDOW_VISIBLE_STATE());
                getInstance().DEFAULT_WINDOW_DECORATION_STATE = Widgets.drawBoolControl("Decoration", DEFAULT_WINDOW_DECORATION_STATE());
                getInstance().DEFAULT_WINDOW_RESIZABLE_STATE = Widgets.drawBoolControl("Resizable", DEFAULT_WINDOW_RESIZABLE_STATE());
                getInstance().DEFAULT_WINDOW_FLOAT_STATUS = Widgets.drawBoolControl("Float", DEFAULT_WINDOW_FLOAT_STATUS());
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Keyboard Control"))
            {
                getInstance().DEFAULT_JUMP_IMPULSE = Widgets.drawFloatControl("Jump Impulse", DEFAULT_JUMP_IMPULSE());
                getInstance().DEFAULT_MAX_RUN_SPEED = Widgets.drawFloatControl("Max Run Speed", DEFAULT_MAX_RUN_SPEED());
                getInstance().DEFAULT_GROUND_DETECT_RAY_LENGTH = Widgets.drawFloatControl("Ground Detect Ray Length", DEFAULT_GROUND_DETECT_RAY_LENGTH());
                getInstance().DEFAULT_COYOTE_TIME = Widgets.drawFloatControl("Coyote Time", DEFAULT_COYOTE_TIME());
                getInstance().DEFAULT_MAX_JUMPS = Widgets.drawIntControl("Max Jumps", DEFAULT_MAX_JUMPS());
                getInstance().DEFAULT_MOVE_RIGHT_KEY = (Keys) Widgets.drawEnumControls(Keys.class, "Move Right", DEFAULT_MOVE_RIGHT_KEY());
                getInstance().DEFAULT_MOVE_LEFT_KEY = (Keys) Widgets.drawEnumControls(Keys.class, "Move Left", DEFAULT_MOVE_LEFT_KEY());
                getInstance().DEFAULT_RUN_KEY = (Keys) Widgets.drawEnumControls(Keys.class, "Run", DEFAULT_RUN_KEY());
                getInstance().DEFAULT_JUMP_KEY = (Keys) Widgets.drawEnumControls(Keys.class, "Jump", DEFAULT_JUMP_KEY());
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Texture"))
            {
                getInstance().DEFAULT_TEXTURE_WRAP_S = (TextureWrapping) Widgets.drawEnumControls(TextureWrapping.class, "Wrap S", DEFAULT_TEXTURE_WRAP_S());
                getInstance().DEFAULT_TEXTURE_WRAP_T = (TextureWrapping) Widgets.drawEnumControls(TextureWrapping.class, "Wrap T", DEFAULT_TEXTURE_WRAP_T());
                getInstance().DEFAULT_TEXTURE_MIN_FILTER = (TextureMinimizeFilter) Widgets.drawEnumControls(TextureMinimizeFilter.class, "Min Filter", DEFAULT_TEXTURE_MIN_FILTER());
                getInstance().DEFAULT_TEXTURE_MAX_FILTER = (TextureMaximizeFilter) Widgets.drawEnumControls(TextureMaximizeFilter.class, "Max Filter", DEFAULT_TEXTURE_MAX_FILTER());
                getInstance().MAX_IMAGE_DISPLAY_WIDTH = Widgets.drawFloatControl("Max Image Width", MAX_IMAGE_DISPLAY_WIDTH());
                getInstance().MAX_IMAGE_DISPLAY_HEIGHT = Widgets.drawFloatControl("Max Image Height", MAX_IMAGE_DISPLAY_HEIGHT());
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Text Component"))
            {
                getInstance().DEFAULT_CHARACTER_SPACING = Widgets.drawFloatControl("Character Spacing", DEFAULT_CHARACTER_SPACING());
                getInstance().DEFAULT_TAB_SPACING = Widgets.drawFloatControl("Tab Spacing", DEFAULT_TAB_SPACING());
                getInstance().DEFAULT_LINE_HEIGHT = Widgets.drawFloatControl("Line Height", DEFAULT_LINE_HEIGHT());
                getInstance().DEFAULT_TEXT_SIZE = Widgets.drawFloatControl("Text Size", DEFAULT_TEXT_SIZE());
                getInstance().DEFAULT_TEXT_MOVE_WITH_MASTER = Widgets.drawBoolControl("Move With Master", DEFAULT_TEXT_MOVE_WITH_MASTER());
                getInstance().DEFAULT_TEXT_LAYER = Widgets.drawIntControl("Layer", DEFAULT_TEXT_LAYER());
                Widgets.colorPicker4("Character Color", DEFAULT_CHARACTER_COLOR());
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Editor Camera"))
            {
                getInstance().DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE = Widgets.drawFloatControl("Drag Debounce", DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE());
                getInstance().DEFAULT_EDITOR_CAMERA_LERP_TIME = Widgets.drawFloatControl("Lerp Time", DEFAULT_EDITOR_CAMERA_LERP_TIME());
                getInstance().DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY = Widgets.drawFloatControl("Drag Sensitivity", DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY());
                getInstance().DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY = Widgets.drawFloatControl("Scroll Sensitivity", DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY());
                getInstance().DEFAULT_CAMERA_ZOOM = Widgets.drawFloatControl("Zoom", DEFAULT_CAMERA_ZOOM());
                Vector2f temp = new Vector2f(getInstance().DEFAULT_CAMERA_PROJECTION_SIZE.x, getInstance().DEFAULT_CAMERA_PROJECTION_SIZE.y);
                Widgets.drawVec2Control("Projection Size", temp);
                getInstance().DEFAULT_CAMERA_PROJECTION_SIZE = new Vector2f(temp.x, temp.y);
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Misc"))
            {
                getInstance().DEFAULT_FRAME_TIME = Widgets.drawFloatControl("Frame Time", DEFAULT_FRAME_TIME());
                getInstance().DEFAULT_SAVE_DIR = Widgets.inputText("Save Directory", DEFAULT_SAVE_DIR());
                getInstance().MAX_LOG_FILE_LIMIT = Widgets.drawIntControl("Log File Limit", MAX_LOG_FILE_LIMIT());
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
    }

    public static void trigger()
    {
        popupOpen = !popupOpen;
    }

}
