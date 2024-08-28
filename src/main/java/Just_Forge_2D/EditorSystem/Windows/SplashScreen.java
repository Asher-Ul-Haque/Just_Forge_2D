package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.EditorWindow;
import Just_Forge_2D.EditorSystem.ProjectManager;
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
            EditorWindow.get().setClearColor(new Vector4f(0.5f));
            EditorWindow.get().setAlwaysOnTop(true);
            EditorWindow.get().setDecorated(false);
            EditorWindow.get().setSize(600, 400);
            EditorWindow.get().setPosition(
                    (WindowSystemManager.getMonitorSize().x - EditorWindow.get().getWidth()) / 2,
                    (WindowSystemManager.getMonitorSize().y - EditorWindow.get().getHeight()) / 2
            );
            if (logoTexture == null)
            {
                logoTexture = new Texture();
                logoTexture.init(DefaultValues.DEFAULT_ICON_PATH);
            }
            EditorWindow.get().setVisible(true);
            isInitialized = true;
        }
    }

    public static void render()
    {
        timer += EditorWindow.get().getDeltaTime();
        // - - - create splash screen window
        ImGui.begin("Splash Screen", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(EditorWindow.get().getWidth(), EditorWindow.get().getHeight());
        ImGui.setNextWindowBgAlpha(0f);

        // - - - create the logo
        float imageX = (EditorWindow.get().getWidth() - (float) logoTexture.getWidth() / 2) / 2.0f;
        float imageY = (EditorWindow.get().getHeight() - (float) logoTexture.getWidth() / 2) / 2.0f;
        if (timer > 5.2f)
        {
            imageY -= ((float) EditorWindow.get().getHeight() / 4);
        }
        ImGui.setCursorPos(imageX, imageY);
        ImGui.image(logoTexture.getID(), (float) logoTexture.getWidth() / 2, (float) logoTexture.getWidth() / 2, 0, 1, 1, 0);

        if (timer > 5f)
        {
            if (timer > 5.2f)
            {
                displayWelcomePage();
            }
            else
            {
                EditorWindow.get().setVisible(false);
            }
        }
        ImGui.end();
    }

    private static void displayWelcomePage()
    {
        if (!EditorWindow.get().isDecorated())
        {
            EditorWindow.get().setDecorated(true);
            EditorWindow.get().setAlwaysOnTop(false);
            EditorWindow.get().setVisible(true);
        }

        float windowWidth = EditorWindow.get().getWidth();
        float windowHeight = EditorWindow.get().getHeight();
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
        EditorWindow.get().setVisible(false);
        Logger.FORGE_LOG_TRACE("Project Path : " + EditorSystemManager.projectDir);
        EditorSystemManager.setCurrentState(EditorSystemManager.state.isEditor);
        EditorWindow.get().setSize(WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y);
        EditorWindow.get().setPosition(0, 0);
        EditorWindow.get().setClearColor(new Vector4f(0.0f, 0.541f, 0.772f, 1.0f));
        if (logoTexture != null)
        {
            logoTexture.detach();
            logoTexture = null;
        }
        EditorWindow.get().setVisible(true);
    }
}
