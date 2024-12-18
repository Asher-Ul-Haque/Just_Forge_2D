package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.AssetPool.AssetPoolSerializer;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.GameCodeLoader;
import Just_Forge_2D.GameSystem.GameManager;
import Just_Forge_2D.GameSystem.ProjectManager;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Themes.Theme;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import Just_Forge_2D.WindowSystem.GameWindow;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector4f;

public class SplashScreen
{

    // - - -  private variables
    private static Texture logoTexture;
    private static boolean isInitialized = false;
    private static float timer = 0.0f;
    private static final float splashTime = 2.0f;
    private static final float relapseTime = 0.2f;
    private static boolean readyToLoad = false;
    private static boolean load = false;
    private static boolean compiling = false;
    private static float progress = 0.0f;

    // - - - initialization
    public static void initialize()
    {
        if (!isInitialized)
        {
            Logger.FORGE_LOG_INFO("Initializing Just Forge 2D");
            // - - - set up the window
            GameWindow.get().setClearColor(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f));
            GameWindow.get().setAlwaysOnTop(true);
            GameWindow.get().setDecorated(false);
            GameWindow.get().setSize(600, 400);
            GameWindow.get().setPosition(
                    (WindowSystemManager.getMonitorSize().x - GameWindow.get().getWidth()) / 2,
                    (WindowSystemManager.getMonitorSize().y - GameWindow.get().getHeight()) / 2
            );

            // - - - set up the texture
            Logger.FORGE_LOG_TRACE("Loading Default Texture");
            if (logoTexture == null)
            {
                logoTexture = new Texture();
                logoTexture.init("Assets/Textures/logo.png");
                AssetPool.addTexture("Default", Settings.DEFAULT_ICON_PATH(), true);
            }

            // if (!EditorSystemManager.isRelease) startEarlyCompilation();

            // - - - flip the flags
            Logger.FORGE_LOG_TRACE("Getting Ready to go");
            GameWindow.get().setVisible(true);
            isInitialized = true;
        }
    }


    // - - - the main render function
    public static void restart()
    {
        timer = splashTime + relapseTime + 0.01f;
        compiling = false;
        readyToLoad = false;
        progress = 0.0f;
        load = false;
        isInitialized = false;
    }

    public static void render(float DELTA_TIME)
    {
        initialize();
        timer += DELTA_TIME;
        // - - - create splash screen window
        ImGui.begin("Splash Screen", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(GameWindow.get().getWidth(), GameWindow.get().getHeight());
        ImGui.setNextWindowBgAlpha(0f);

        // - - - create the logo
        float imageX = (GameWindow.get().getWidth() - (float) logoTexture.getWidth()) / 2.0f;
        float imageY = (GameWindow.get().getHeight() - (float) logoTexture.getHeight()) / 2.0f;
        if (timer > splashTime + relapseTime && !readyToLoad)
        {
            if (!EditorSystemManager.isRelease) imageX -= ((float) GameWindow.get().getWidth() / 4);
        }
        ImGui.setCursorPos(imageX, imageY);
        ImGui.image(logoTexture.getID(), (float) logoTexture.getWidth(), (float) logoTexture.getHeight(), 0, 1, 1, 0);
        ImGui.setCursorPosX(imageX);
        if (!EditorSystemManager.isRelease)
        {
            ImGui.pushFont(ImGUIManager.interExtraBold);
            Widgets.text("       J  U  S  T       F  O  R  G  E       2  D");
            ImGui.popFont();
        }

        if (timer > splashTime)
        {
            if (timer > splashTime + relapseTime)
            {
                if (!readyToLoad) displayWelcomePage();
                else displayLoadingPage();
            }
            else
            {
                if (!EditorSystemManager.isRelease) GameWindow.get().setVisible(false);
            }
        }
        ImGui.end();
    }

    // - - - the display page
    private static void displayWelcomePage()
    {
        if (!GameWindow.get().isDecorated())
        {
            Logger.FORGE_LOG_TRACE("Getting the window back");
            if (!EditorSystemManager.isRelease) GameWindow.get().setDecorated(true);
            GameWindow.get().setAlwaysOnTop(false);
            GameWindow.get().setVisible(true);
        }

        // - - - early return for cleanup
        if (EditorSystemManager.isRelease)
        {
            cleanup();
            return;
        }

        float buttonWidth = 256f;
        float buttonHeight = 64f;
        float buttonX = GameWindow.get().getWidth() - buttonWidth - 32f;
        float buttonY = 128f;

        ImGui.setCursorPos(buttonX, buttonY);
        if (timer > splashTime + relapseTime && !EditorSystemManager.isRelease)
        {
            ImGui.pushFont(ImGUIManager.interExtraBold);
            if (ImGui.button("Create New Project", buttonWidth, buttonHeight))
            {
                if (ProjectManager.createNewProject())
                {
                    GameWindow.get().setVisible(false);
                    readyToLoad = true;
                }
            }
            buttonY += 80.0f;
            ImGui.setCursorPos(buttonX, buttonY);
            if (ImGui.button("Open Existing Project", buttonWidth, buttonHeight))
            {
                if (ProjectManager.openExistingProject())
                {
                    GameWindow.get().setVisible(false);
                    readyToLoad = true;
                }
            }
        }
        ImGui.popFont();
    }

    // - - - load code and display the text
    private static void displayLoadingPage()
    {
        ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("Loading Project: " + ProjectManager.PROJECT_NAME).x) / 2);
        ImGui.setCursorPosY(GameWindow.get().getHeight() - 40);
        String title = "Loading Project: " + ProjectManager.PROJECT_NAME;
        Widgets.text(title);
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        ImGui.setCursorPosY(GameWindow.get().getHeight() - 20);
        ImVec4 backColor = EditorSystemManager.getCurrentTheme().quaternaryColor;
        ImVec4 color = EditorSystemManager.getCurrentTheme().secondaryColor;
        ImGui.pushStyleColor(ImGuiCol.PlotHistogram, color.x, color.y, color.z, color.w);
        ImGui.pushStyleColor(ImGuiCol.FrameBg, backColor.x, backColor.y, backColor.z, backColor.w);
        progress = Math.min(GameManager.getProgressPercentage(), progress + 0.01f);
        ImGui.progressBar(progress, ImGui.getContentRegionAvailX(), 14);
        ImGui.popStyleColor(2);
        Theme.resetDefaultTextColor();

        if (load) cleanup();
        if (!EditorSystemManager.isRelease)
        {
            if (!GameWindow.get().isVisible()) GameWindow.get().setVisible(true);
        }
        load = true;
    }

    // - - - finish up
    public static void cleanup()
    {
        // - - - Check if lastProjectPath is null before comparing
        if (!compiling)
        {
            Logger.FORGE_LOG_TRACE("Compiling");
            compiling = true;
            Logger.FORGE_LOG_TRACE("Building user code");
            GameWindow.get().setCurrentScene(null);
            GameManager.buildUserCode();
        }

        // Ensure success or that the paths match
        if (GameManager.isSuccess() && GameManager.getProgressPercentage() == 1f)
        {
            progress = 1f;
            AssetPoolSerializer.loadAssetPool(EditorSystemManager.projectDir + "/.forge/Pool.justForgeFile");
            if (!EditorSystemManager.isRelease)
            {
                AssetPool.addSound("Default", "default.ogg", false, false);
                GameCodeLoader.openEye();
            }
            if (EditorSystemManager.currentSceneInitializer == null || GameWindow.getCurrentScene() == null)
            {
                EditorSystemManager.setCurrentSceneInitializer(null);
            }

            GameWindow.get().setVisible(false);
            Logger.FORGE_LOG_TRACE("Project Path : " + EditorSystemManager.projectDir);
            GameWindow.get().setDecorated(true);
            GameWindow.get().maximize();
            EditorSystemManager.setCurrentState(EditorSystemManager.state.isEditor);

            if (EditorSystemManager.isRelease)
            {
                EventManager.notify(null, new Event(EventTypes.ForgeStart));
            }

            String windowTitle = EditorSystemManager.isRelease ? ProjectManager.PROJECT_NAME : "Just Forge 2D    -    " + ProjectManager.PROJECT_NAME;
            GameWindow.get().setTitle(windowTitle);
            GameWindow.get().setVisible(true);
        }
        else if (!GameManager.isSuccess() && GameManager.getProgressPercentage() == 1f)
        {
            EditorSystemManager.setCurrentState(EditorSystemManager.state.isSplashScreen);
            GameWindow.get().restore();
            GameWindow.get().resetTitleBar();
            SplashScreen.restart();
            SplashScreen.initialize();
        }
    }
}