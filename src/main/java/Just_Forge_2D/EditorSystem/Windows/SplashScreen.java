package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.GameManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.ProjectManager;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector4f;

public class SplashScreen
{
    private static Texture logoTexture;
    private static boolean isInitialized = false;
    private static float timer = 0.0f;

    public static void intiailize()
    {
        if (!isInitialized)
        {
            MainWindow.get().setClearColor(new Vector4f(0.5f));
            MainWindow.get().setAlwaysOnTop(true);
            MainWindow.get().setDecorated(false);
            MainWindow.get().setSize(600, 400);
            MainWindow.get().setPosition(
                    (WindowSystemManager.getMonitorSize().x - MainWindow.get().getWidth()) / 2,
                    (WindowSystemManager.getMonitorSize().y - MainWindow.get().getHeight()) / 2
            );
            if (logoTexture == null)
            {
                logoTexture = new Texture();
                logoTexture.init(DefaultValues.DEFAULT_ICON_PATH);
            }
            MainWindow.get().setVisible(true);
            isInitialized = true;
        }
    }

    public static void render()
    {
        timer += MainWindow.get().getDeltaTime();
        // - - - create splash screen window
        ImGui.begin("Splash Screen", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(MainWindow.get().getWidth(), MainWindow.get().getHeight());
        ImGui.setNextWindowBgAlpha(0f);

        // - - - create the logo
        float imageX = (MainWindow.get().getWidth() - (float) logoTexture.getWidth() / 2) / 2.0f;
        float imageY = (MainWindow.get().getHeight() - (float) logoTexture.getWidth() / 2) / 2.0f;
        if (timer > 3.2f)
        {
            imageY -= ((float) MainWindow.get().getHeight() / 4);
        }
        ImGui.setCursorPos(imageX, imageY);
        ImGui.image(logoTexture.getID(), (float) logoTexture.getWidth() / 2, (float) logoTexture.getWidth() / 2, 0, 1, 1, 0);

        if (timer > 3f)
        {
            if (timer > 3.2f)
            {
                displayWelcomePage();
            }
            else
            {
                MainWindow.get().setVisible(false);
            }
        }
        ImGui.end();
    }

    private static void displayWelcomePage()
    {
        if (!MainWindow.get().isDecorated())
        {
            MainWindow.get().setDecorated(true);
            MainWindow.get().setAlwaysOnTop(false);
            MainWindow.get().setVisible(true);
        }

        if (EditorSystemManager.isRelease)
        {
            cleanup();
            return;
        }

        float windowWidth = MainWindow.get().getWidth();
        float windowHeight = MainWindow.get().getHeight();
        float buttonWidth = windowWidth / 3;
        float buttonHeight = windowHeight / 8;
        float buttonX = (windowWidth - buttonWidth) / 2.0f;
        float buttonY = (windowHeight * 0.6f);

        ImGui.setCursorPos(buttonX, buttonY);
        if (ImGui.button("Create New Project", buttonWidth, buttonHeight))
        {
            if (ProjectManager.createNewProject()) cleanup();
        }
        buttonY += buttonHeight + 36.0f;
        ImGui.setCursorPos(buttonX, buttonY);
        if (ImGui.button("Open Existing Project", buttonWidth, buttonHeight))
        {
            if (ProjectManager.openExistingProject()) cleanup();
        }
    }

    public static void cleanup()
    {
        GameManager.buildUserCode();
        MainWindow.get().setVisible(false);
        Logger.FORGE_LOG_TRACE("Project Path : " + EditorSystemManager.projectDir);
        EditorSystemManager.setCurrentState(EditorSystemManager.state.isEditor);
        MainWindow.get().setSize(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
        MainWindow.get().setPosition(0, 0);
        if (logoTexture != null)
        {
            logoTexture.detach();
            logoTexture = null;
        }
        MainWindow.get().setVisible(true);
        if (EditorSystemManager.isRelease) EventManager.notify(null, new Event(EventTypes.ForgeStart));
        else EventManager.notify(null, new Event(EventTypes.ForgeStop));
    }
}