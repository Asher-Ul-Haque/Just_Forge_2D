package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.EditorSystem.Themes.ConfigFlags;
import Just_Forge_2D.EditorSystem.Windows.*;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.InputSystem.*;
import imgui.ImFontAtlas;
import imgui.ImFontConfig;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;


// - - - Class to manage imGUI
public class ImGUIManager
{
    // - - - private variables
    private static final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static long windowPtr;
    private static ImGuiIO io;
    static float timer = 0f;


    // - - - Functions - - -


    private static void setInputMapping()
    {
        // - - - Input mapping - - -

        // - - - Keyboard mapping. ImGui will use those indices to peek into the io.KeysDown[] array.
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = GLFW.GLFW_KEY_TAB;
        keyMap[ImGuiKey.LeftArrow] = GLFW.GLFW_KEY_LEFT;
        keyMap[ImGuiKey.RightArrow] = GLFW.GLFW_KEY_RIGHT;
        keyMap[ImGuiKey.UpArrow] = GLFW.GLFW_KEY_UP;
        keyMap[ImGuiKey.DownArrow] = GLFW.GLFW_KEY_DOWN;
        keyMap[ImGuiKey.PageUp] = GLFW.GLFW_KEY_PAGE_UP;
        keyMap[ImGuiKey.PageDown] = GLFW.GLFW_KEY_PAGE_DOWN;
        keyMap[ImGuiKey.Home] = GLFW.GLFW_KEY_HOME;
        keyMap[ImGuiKey.End] = GLFW.GLFW_KEY_END;
        keyMap[ImGuiKey.Insert] = GLFW.GLFW_KEY_INSERT;
        keyMap[ImGuiKey.Delete] = GLFW.GLFW_KEY_DELETE;
        keyMap[ImGuiKey.Backspace] = GLFW.GLFW_KEY_BACKSPACE;
        keyMap[ImGuiKey.Space] = GLFW.GLFW_KEY_SPACE;
        keyMap[ImGuiKey.Enter] = GLFW.GLFW_KEY_ENTER;
        keyMap[ImGuiKey.Escape] = GLFW.GLFW_KEY_ESCAPE;
        keyMap[ImGuiKey.KeyPadEnter] = GLFW.GLFW_KEY_KP_ENTER;
        keyMap[ImGuiKey.A] = GLFW.GLFW_KEY_A;
        keyMap[ImGuiKey.C] = GLFW.GLFW_KEY_C;
        keyMap[ImGuiKey.V] = GLFW.GLFW_KEY_V;
        keyMap[ImGuiKey.X] = GLFW.GLFW_KEY_X;
        keyMap[ImGuiKey.Y] = GLFW.GLFW_KEY_Y;
        keyMap[ImGuiKey.Z] = GLFW.GLFW_KEY_Z;
        io.setKeyMap(keyMap);

        // - - - cursor mapping
        mouseCursors[ImGuiMouseCursor.Arrow] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);

        Logger.FORGE_LOG_INFO("Input system mapped with editor gui");
    }

    private static void setKeyboardCallbacks()
    {
        GLFW.glfwSetKeyCallback(windowPtr, (w, key, scancode, action, mods) ->
        {
            if (action == GLFW.GLFW_PRESS)
            {
                io.setKeysDown(key, true);
            }
            else if (action == GLFW.GLFW_RELEASE)
            {
                io.setKeysDown(key, false);
            }

            io.setKeyCtrl(io.getKeysDown(GLFW.GLFW_KEY_LEFT_CONTROL) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_CONTROL));
            io.setKeyShift(io.getKeysDown(GLFW.GLFW_KEY_LEFT_SHIFT) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_SHIFT));
            io.setKeyAlt(io.getKeysDown(GLFW.GLFW_KEY_LEFT_ALT) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_ALT));
            io.setKeySuper(io.getKeysDown(GLFW.GLFW_KEY_LEFT_SUPER) || io.getKeysDown(GLFW.GLFW_KEY_RIGHT_SUPER));

            if (!io.getWantCaptureKeyboard())
            {
                Keyboard.keyCallback(w, key, scancode, action, mods);
            }
        });

        // - - - callback for typing
        GLFW.glfwSetCharCallback(windowPtr, (w, c) ->
        {
            if (c != GLFW.GLFW_KEY_DELETE)
            {
                io.addInputCharacter(c);
            }
        });
    }

    private static void setMouseCallbacks()
    {
        GLFW.glfwSetMouseButtonCallback(windowPtr, (w, button, action, mods) ->
        {
            final boolean[] mouseDown = new boolean[5];

            mouseDown[0] = button == GLFW.GLFW_MOUSE_BUTTON_1 && action != GLFW.GLFW_RELEASE;
            mouseDown[1] = button == GLFW.GLFW_MOUSE_BUTTON_2 && action != GLFW.GLFW_RELEASE;
            mouseDown[2] = button == GLFW.GLFW_MOUSE_BUTTON_3 && action != GLFW.GLFW_RELEASE;
            mouseDown[3] = button == GLFW.GLFW_MOUSE_BUTTON_4 && action != GLFW.GLFW_RELEASE;
            mouseDown[4] = button == GLFW.GLFW_MOUSE_BUTTON_5 && action != GLFW.GLFW_RELEASE;

            io.setMouseDown(mouseDown);

            if (!io.getWantCaptureMouse() && mouseDown[1])
            {
                ImGui.setWindowFocus(null);
            }
            if (!io.getWantCaptureMouse() || GameViewport.getWantCaptureMouse())
            {
                Mouse.mouseButtonCallback(w, button, action, mods);
            }
        });

        // - - - callback for mouse scroll
        GLFW.glfwSetScrollCallback(windowPtr, (w, xOffset, yOffset) ->
        {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            if (!io.getWantCaptureMouse() || GameViewport.getWantCaptureMouse())
            {
                Mouse.mouseScrollCallback(w, xOffset, yOffset);
            }
            else
            {
                Mouse.clear();
            }
        });
        Logger.FORGE_LOG_INFO("editor gui input system callbacks assigned. Ready for immediate mode GUI");
    }

    private static void setClipboardCallbacks()
    {
        io.setSetClipboardTextFn(new ImStrConsumer()
        {
            @Override
            public void accept(final String s)
            {
                GLFW.glfwSetClipboardString(windowPtr, s);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier()
        {
            @Override
            public String get()
            {
                final String clipboardString = GLFW.glfwGetClipboardString(windowPtr);
                if (clipboardString != null)
                {
                    return clipboardString;
                }
                else
                {
                    return "";
                }
            }
        });
        Logger.FORGE_LOG_INFO("editor gui clipboard reading activated");
    }

    private static void setFontAtlas()
    {
        // - - - spritesheet but for fonts
        final ImFontAtlas fontAtlas = io.getFonts();
        final ImFontConfig fontConfig = new ImFontConfig(); // Natively allocated object, should be explicitly destroyed

        // Glyphs could be added per-font as well as per config used globally like here
        fontConfig.setGlyphRanges(fontAtlas.getGlyphRangesDefault());

        final String fontPath = "Assets/Fonts/JetBrainsMono-Bold.ttf";
        final int fontSize = 16;

// Fonts merge example
        fontConfig.setPixelSnapH(true);
        fontAtlas.addFontFromFileTTF(fontPath, fontSize, fontConfig);

        fontConfig.destroy(); // After all fonts were added we don't need this config more
        fontAtlas.build();

    }

    // - - - start function because constructors are pointless
    public static void initImGui(long HANDLE)
    {
        windowPtr = HANDLE;
        Logger.FORGE_LOG_INFO("Created imgui for window " + HANDLE);
        // IMPORTANT!!
        // This line is critical for Dear ImGui to work.
        ImGui.createContext();
        Logger.FORGE_LOG_INFO("context created for imgui rendering");

        // - - - Initialize ImGuiIO config
        io = ImGui.getIO();

        io.setIniFilename("Configurations/editorLayout.justForgeFile"); // We don't want to save .ini file
        if (ConfigFlags.dockingEnable) io.addConfigFlags(ImGuiConfigFlags.DockingEnable); // enable docking
        if (ConfigFlags.viewportsEnable) io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable); // enable docking
        io.setBackendPlatformName("imgui_java_impl_glfw");

        setInputMapping();
        setKeyboardCallbacks();
        setMouseCallbacks();
        setClipboardCallbacks();
        setFontAtlas();

        EditorSystemManager.getCurrentTheme().applyTheme();
        imGuiGl3.init("#version 450 core");
        Logger.FORGE_LOG_INFO("Editor GUI ready");
    }


    // - - - GUI usage functions - - -

    public static void update(float DELTA_TIME, Scene SCENE)
    {
        timer += DELTA_TIME;
        startFrame(DELTA_TIME);
        ImGui.newFrame();
        if (ConfigFlags.dockingEnable) setupDockSpace();
        if (timer > 5f)
        {
            SCENE.editorGUI();
            GameViewport.render();
            PropertiesWindow.render();
            MenuBar.render();
            SceneHierarchyWindow.editorGUI();
        }
        else
        {
            ImGui.begin("Sample");
            ImGui.text("S");
            ImGui.end();
        }
        ImGui.end();
        ImGui.render();

        endFrame();
    }

    private static void startFrame(final float DELTA_TIME)
    {
        // - - - Get window properties and mouse position
        float[] winWidth = {EditorWindow.get().getWidth()};
        float[] winHeight = {EditorWindow.get().getHeight()};
        double[] mousePosX = {0};
        double[] mousePosY = {0};
        GLFW.glfwGetCursorPos(windowPtr, mousePosX, mousePosY);

        // - - - We SHOULD call those methods to update Dear ImGui state for the current frame
        final ImGuiIO io = ImGui.getIO();
        io.setDisplaySize(winWidth[0], winHeight[0]);
        io.setDisplayFramebufferScale(1f, 1f);
        io.setMousePos((float) mousePosX[0], (float) mousePosY[0]);
        io.setDeltaTime(DELTA_TIME);

        // - - - Update the mouse cursor
        final int imguiCursor = ImGui.getMouseCursor();
        GLFW.glfwSetCursor(windowPtr, mouseCursors[imguiCursor]);
        GLFW.glfwSetInputMode(windowPtr, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    private static void endFrame()
    {
        imGuiGl3.renderDrawData(ImGui.getDrawData());
    }

    public static void destroyImGui()
    {
        imGuiGl3.dispose();
        ImGui.destroyContext();
        Logger.FORGE_LOG_INFO("Editor GUI destroyed");
    }

    private static void setupDockSpace()
    {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(EditorWindow.get().getWidth(), EditorWindow.get().getHeight());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 4.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // - - - SETUP DOCKSPACE
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }
}