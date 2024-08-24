package Just_Forge_2D.ForgeEditor;


import Just_Forge_2D.ForgeEditor.Configurations.ConfigFlags;
import Just_Forge_2D.ForgeEditor.Windows.PropertiesWindow;
import Just_Forge_2D.ForgeEditor.Windows.SceneHierarchyPanel;
import Just_Forge_2D.ForgeEditor.Windows.SplashScreen;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.callback.ImStrConsumer;
import imgui.callback.ImStrSupplier;
import imgui.flag.*;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import imgui.internal.ImGuiContext;
import imgui.type.ImBoolean;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiManager
{
    private static final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
    private static final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();
    private static final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
    private static ImGuiIO io;

    public static void initialize(long HANDLE)
    {
        // - - - initialize IMgui
        ImGuiContext context = ImGui.createContext();
        io = ImGui.getIO();

        // - - - save imGUI save file
        Logger.FORGE_LOG_INFO("Creating ImGUI Context");
        io.setIniFilename("Layout.txt");

        // - - - Enable docking and viewports
        Logger.FORGE_LOG_DEBUG("Docking enabled : " + ConfigFlags.dockingEnable);
        if (ConfigFlags.dockingEnable)
        {
            io.addConfigFlags(ImGuiConfigFlags.DockingEnable);
        }
        Logger.FORGE_LOG_DEBUG("Viewports enabled : " + ConfigFlags.viewportsEnable);
        if (ConfigFlags.viewportsEnable)
        {
            io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        }

        // - - - initailize for GLFW and OpenGL
        io.setBackendPlatformName("imgui_java_impl_glfw");
        imGuiGl3.init("#version 450 core");
        imGuiGlfw.init(HANDLE, false);


        // - - - Setup Input - - -

        // - - - Keyboard
        setupKeyboard(HANDLE);

        // - - - Setup Mouse
        setupMousePointers(HANDLE);

        // - - - clipboard
        setupCallbacks(HANDLE);
    }

    private static void setupKeyboard(long HANDLE)
    {
        final int[] keyMap = new int[ImGuiKey.COUNT];
        keyMap[ImGuiKey.Tab] = Keys.TAB.keyCode;
        keyMap[ImGuiKey.LeftArrow] = Keys.ARROW_LEFT.keyCode;
        keyMap[ImGuiKey.RightArrow] = Keys.ARROW_RIGHT.keyCode;
        keyMap[ImGuiKey.UpArrow] = Keys.ARROW_UP.keyCode;
        keyMap[ImGuiKey.DownArrow] = Keys.ARROW_DOWN.keyCode;
        keyMap[ImGuiKey.PageUp] = Keys.PAGE_UP.keyCode;
        keyMap[ImGuiKey.PageDown] = Keys.PAGE_DOWN.keyCode;
        keyMap[ImGuiKey.Home] = Keys.HOME.keyCode;
        keyMap[ImGuiKey.End] = Keys.END.keyCode;
        keyMap[ImGuiKey.Insert] = Keys.INSERT.keyCode;
        keyMap[ImGuiKey.Delete] = Keys.DELETE.keyCode;
        keyMap[ImGuiKey.Backspace] = Keys.BACKSPACE.keyCode;
        keyMap[ImGuiKey.Space] = Keys.SPACE.keyCode;
        keyMap[ImGuiKey.Enter] = Keys.ENTER.keyCode;
        keyMap[ImGuiKey.Escape] = Keys.ESCAPE.keyCode;
        keyMap[ImGuiKey.KeyPadEnter] = Keys.NUMPAD_ENTER.keyCode;
        keyMap[ImGuiKey.A] = Keys.A.keyCode;
        keyMap[ImGuiKey.C] = Keys.C.keyCode;
        keyMap[ImGuiKey.V] = Keys.V.keyCode;
        keyMap[ImGuiKey.X] = Keys.X.keyCode;
        keyMap[ImGuiKey.Y] = Keys.Y.keyCode;
        keyMap[ImGuiKey.Z] = Keys.Z.keyCode;
        io.setKeyMap(keyMap);

        GLFW.glfwSetKeyCallback(HANDLE, (w, key, scancode, action, mods) ->
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

        GLFW.glfwSetCharCallback(HANDLE, (w, c) ->
        {
            if (c != GLFW.GLFW_KEY_DELETE)
            {
                io.addInputCharacter(c);
            }
        });
    }

    private static void setupMousePointers(long HANDLE)
    {
        mouseCursors[ImGuiMouseCursor.Arrow] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.TextInput] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_IBEAM_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeAll] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNS] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_VRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeEW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNESW] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.ResizeNWSE] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        mouseCursors[ImGuiMouseCursor.Hand] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_HAND_CURSOR);
        mouseCursors[ImGuiMouseCursor.NotAllowed] = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);

        GLFW.glfwSetMouseButtonCallback(HANDLE, (w, button, action, mods) ->
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
            if (!io.getWantCaptureMouse())
            {
                Mouse.mouseButtonCallback(w, button, action, mods);
            }
        });

        // - - - callback for mouse scroll
        GLFW.glfwSetScrollCallback(HANDLE, (w, xOffset, yOffset) ->
        {
            io.setMouseWheelH(io.getMouseWheelH() + (float) xOffset);
            io.setMouseWheel(io.getMouseWheel() + (float) yOffset);
            if (!io.getWantCaptureMouse())
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

    private static void setupCallbacks(long HANDLE)
    {
        io.setSetClipboardTextFn(new ImStrConsumer()
        {
            @Override
            public void accept(String STR)
            {
                glfwSetClipboardString(HANDLE, STR);
            }
        });

        io.setGetClipboardTextFn(new ImStrSupplier()
        {
            @Override
            public String get()
            {
                return GLFW.glfwGetClipboardString(HANDLE);
            }
        });
    }

    private static void startFrame(float DELTA_TIME)
    {
        imGuiGlfw.newFrame();
        ImGui.newFrame();
        if (ConfigFlags.dockingEnable && !EditorManager.isSplashScreen) setupDockspace();
    }

    private static void endFrame()
    {
        ImGui.endFrame();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ConfigFlags.viewportsEnable)
        {
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
        }
    }

    public static void update(float DELTA_TIME)
    {
        startFrame(DELTA_TIME);
        if (EditorManager.isSplashScreen)
        {
            SplashScreen.render();
        }
        else
        {
            SceneHierarchyPanel.render();
            PropertiesWindow.render();
            EditorManager.viewport.render();
        }
        ImGui.render();
        endFrame();
    }

    private static void destroyImGui()
    {
        Logger.FORGE_LOG_INFO("Disposing off ImGUI");
        imGuiGl3.dispose();
        ImGui.destroyContext();
    }

    private static void setupDockspace()
    {
        int windowFlags = ImGuiWindowFlags.MenuBar | ImGuiWindowFlags.NoDocking;

        ImGui.setNextWindowPos(0.0f, 0.0f, ImGuiCond.Always);
        ImGui.setNextWindowSize(io.getDisplaySizeX(), io.getDisplaySizeY());
        ImGui.pushStyleVar(ImGuiStyleVar.WindowRounding, 0.0f);
        ImGui.pushStyleVar(ImGuiStyleVar.WindowBorderSize, 0.0f);

        windowFlags |= ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoCollapse | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove | ImGuiWindowFlags.NoBringToFrontOnFocus | ImGuiWindowFlags.NoNavFocus;

        ImGui.begin("Dockspace Demo", new ImBoolean(true), windowFlags);
        ImGui.popStyleVar(2);

        // - - - SETUP DOCKSPACE
        ImGui.dockSpace(ImGui.getID("Dockspace"));
    }
}