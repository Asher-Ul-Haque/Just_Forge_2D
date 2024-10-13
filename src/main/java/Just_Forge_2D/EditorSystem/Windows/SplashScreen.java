package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.GameManager;
import Just_Forge_2D.GameSystem.ProjectManager;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.SceneSystem.EmptySceneScript;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
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
            GameWindow.get().setClearColor(new Vector4f(0.0f, 0.0f, 0.0f, 1.0f));
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
                logoTexture.init(DefaultValues.DEFAULT_ICON_PATH);
                AssetPool.addTexture("Default", DefaultValues.DEFAULT_ICON_PATH);
            }

            // - - - flip the flags
            Logger.FORGE_LOG_TRACE("Getting Ready to go");
            GameWindow.get().setVisible(true);
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
        ImGui.setWindowSize(GameWindow.get().getWidth(), GameWindow.get().getHeight());
        ImGui.setNextWindowBgAlpha(0f);

        // - - - create the logo
        float imageX = (GameWindow.get().getWidth() - (float) logoTexture.getWidth()) / 2.0f;
        float imageY = (GameWindow.get().getHeight() - (float) logoTexture.getHeight()) / 2.0f;
        if (timer > splashTime + relapseTime && !readyToLoad)
        {
            imageX -= ((float) GameWindow.get().getWidth() / 4);
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
                GameWindow.get().setVisible(false);
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
            GameWindow.get().setDecorated(true);
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
        if (timer > splashTime + relapseTime)
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
        if (load) cleanup();
        if (!GameWindow.get().isVisible()) GameWindow.get().setVisible(true);

        ImGui.setCursorPosX((ImGui.getWindowWidth() - ImGui.calcTextSize("Loading Project: " + ProjectManager.PROJECT_NAME).x) / 2);
        ImGui.setCursorPosY(GameWindow.get().getHeight() - 32);
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        String title = "Loading Project: " + ProjectManager.PROJECT_NAME;
        ImGui.text(title);
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
                    EditorSystemManager.setCurrentSceneInitializer(EmptySceneScript.class);
                }
                GameWindow.get().setVisible(false);
                Logger.FORGE_LOG_TRACE("Project Path : " + EditorSystemManager.projectDir);
                GameWindow.get().maximize();
                EditorSystemManager.setCurrentState(EditorSystemManager.state.isEditor);
                if (EditorSystemManager.isRelease) EventManager.notify(null, new Event(EventTypes.ForgeStart));
                else EventManager.notify(null, new Event(EventTypes.ForgeStop));
                if (EditorSystemManager.isRelease) GameWindow.get().setTitle(ProjectManager.PROJECT_NAME);
                else GameWindow.get().setTitle("Just Forge 2D    -    " + ProjectManager.PROJECT_NAME);
                GameWindow.get().setVisible(true);
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