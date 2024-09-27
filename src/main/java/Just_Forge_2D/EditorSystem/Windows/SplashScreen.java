package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.GameSystem.GameManager;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.ProjectManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import RenderingSystem.Texture;
import SceneSystem.EmptySceneInitializer;
import Utils.AssetPool;
import Utils.DefaultValues;
import Utils.Logger;
import WindowSystem.WindowSystemManager;
import imgui.ImGui;
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

    // - - - initialization
    public static void initialize()
    {
        if (!isInitialized)
        {
            Logger.FORGE_LOG_INFO("Initializing Just Forge 2D");
            // - - - set up the window
            MainWindow.get().setClearColor(new Vector4f(0.0f));
            MainWindow.get().setAlwaysOnTop(true);
            MainWindow.get().setDecorated(false);
            MainWindow.get().setSize(600, 400);
            MainWindow.get().setPosition(
                    (WindowSystemManager.getMonitorSize().x - MainWindow.get().getWidth()) / 2,
                    (WindowSystemManager.getMonitorSize().y - MainWindow.get().getHeight()) / 2
            );

            // - - - set up the texture
            Logger.FORGE_LOG_TRACE("Loading Default Texture");
            if (logoTexture == null)
            {
                logoTexture = new Texture();
                logoTexture.init(DefaultValues.DEFAULT_ICON_PATH);
                AssetPool.addTexture("Default", DefaultValues.DEFAULT_ICON_PATH);
            }

            // - - - flip the flags
            Logger.FORGE_LOG_TRACE("Getting Ready to go");
            MainWindow.get().setVisible(true);
            isInitialized = true;
        }
    }


    // - - - the main render function
    public static void render(float DELTA_TIME)
    {
        initialize();
        timer += DELTA_TIME;
        // - - - create splash screen window
        ImGui.begin("Splash Screen", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(MainWindow.get().getWidth(), MainWindow.get().getHeight());
        ImGui.setNextWindowBgAlpha(0f);

        // - - - create the logo
        float imageX = (MainWindow.get().getWidth() - (float) logoTexture.getWidth()) / 2.0f;
        float imageY = (MainWindow.get().getHeight() - (float) logoTexture.getHeight()) / 2.0f;
        if (timer > splashTime + relapseTime && !readyToLoad)
        {
            imageX -= ((float) MainWindow.get().getWidth() / 4);
        }
        ImGui.setCursorPos(imageX, imageY);
        ImGui.image(logoTexture.getID(), (float) logoTexture.getWidth(), (float) logoTexture.getHeight(), 0, 1, 1, 0);

        if (timer > splashTime)
        {
            if (timer > splashTime + relapseTime)
            {
                if (!readyToLoad) displayWelcomePage();
                else displayLoadingPage();
            }
            else
            {
                MainWindow.get().setVisible(false);
            }
        }
        ImGui.end();
    }

    // - - - the display page
    private static void displayWelcomePage()
    {
        if (!MainWindow.get().isDecorated())
        {
            Logger.FORGE_LOG_TRACE("Getting the window back");
            MainWindow.get().setDecorated(true);
            MainWindow.get().setAlwaysOnTop(false);
            MainWindow.get().setVisible(true);
        }

        // - - - early return for cleanup
        if (EditorSystemManager.isRelease)
        {
            cleanup();
            return;
        }

        float buttonWidth = 256f;
        float buttonHeight = 64f;
        float buttonX = MainWindow.get().getWidth() - buttonWidth - 32f;
        float buttonY = 128f;

        ImGui.setCursorPos(buttonX, buttonY);
        if (timer > splashTime + relapseTime)
        {
            ImGui.pushFont(ImGUIManager.interExtraBold);
            if (ImGui.button("Create New Project", buttonWidth, buttonHeight))
            {
                if (ProjectManager.createNewProject())
                {
                    MainWindow.get().setVisible(false);
                    readyToLoad = true;
                }
            }
            buttonY += 80.0f;
            ImGui.setCursorPos(buttonX, buttonY);
            if (ImGui.button("Open Existing Project", buttonWidth, buttonHeight))
            {
                if (ProjectManager.openExistingProject())
                {
                    MainWindow.get().setVisible(false);
                    readyToLoad = true;
                }
            }
        }
        ImGui.popFont();
    }

    // - - - load code and display the text
    private static void displayLoadingPage()
    {
        if (load) cleanup();
        if (!MainWindow.get().isVisible()) MainWindow.get().setVisible(true);

        ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("Loading Project: " + EditorSystemManager.projectDir).x) / 2);
        ImGui.setCursorPosY(MainWindow.get().getHeight() - 32);
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        ImGui.text("Loading Project: " + EditorSystemManager.projectDir);
        Theme.resetDefaultTextColor();
        load = true;
    }

    // - - - finish up
    public static void cleanup()
    {
        if (!compiling)
        {
            Logger.FORGE_LOG_TRACE("Compiling");
            compiling = true;
            Logger.FORGE_LOG_TRACE("Building user code");
            GameManager.buildUserCode();
            compiling = true;
            if (GameManager.isSuccess())
            {
                if (EditorSystemManager.currentSceneInitializer == null)
                {
                    EditorSystemManager.setCurrentSceneInitializer(EmptySceneInitializer.class);
                }
                MainWindow.get().setVisible(false);
                Logger.FORGE_LOG_TRACE("Project Path : " + EditorSystemManager.projectDir);
                EditorSystemManager.setCurrentState(EditorSystemManager.state.isEditor);
                MainWindow.get().maximize();
                MainWindow.get().setVisible(true);
                if (EditorSystemManager.isRelease) EventManager.notify(null, new Event(EventTypes.ForgeStart));
                else EventManager.notify(null, new Event(EventTypes.ForgeStop));
            }
            else
            {
                timer = splashTime + relapseTime + 0.01f;
                compiling = false;
                readyToLoad = false;
                load = false;
            }
        }
    }
}