package Just_Forge_2D.ForgeEditor.Windows;

import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.ForgeEditor.ProjectManager;
import Just_Forge_2D.RenderingSystems.Texture;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import imgui.ImGui;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiWindowFlags;
import imgui.type.ImString;
import org.joml.Vector4f;

public class SplashScreen
{
    // - - - all the textures
    private static Texture logoTexture;
    private static Texture gearTexture;

    // - - - transition
    private static float transitionProgress = 0.0f;
    private static final float transitionSpeed = 0.1f;
    private static float timer = 5.0f;


    // - - - Functions - - -

    // - - - configure
    public static void setWindowConfig()
    {
        EditorManager.editorScreen.setClearColor(new Vector4f(0.5f));
        EditorManager.editorScreen.setAlwaysOnTop(true);
        EditorManager.editorScreen.setDecorated(false);
        setSizeAndPosition(600, 400);
    }

    private static void setSizeAndPosition(int WIDTH, int HEIGHT)
    {
        EditorManager.editorScreen.setSize(WIDTH, HEIGHT);
        EditorManager.editorScreen.setPosition(
                (WindowSystemManager.getMonitorSize().x - WIDTH) / 2,
                (WindowSystemManager.getMonitorSize().y - HEIGHT) / 2
        );
    }

    // - - - LOAD THE TEXTURES - - -

    protected static void loadTextures()
    {
        if (logoTexture == null)
        {
            logoTexture = new Texture();
            logoTexture.init(DefaultValues.DEFAULT_ICON_PATH);
        }
        if (gearTexture == null)
        {
            gearTexture = new Texture();
            gearTexture.init("Assets/Textures/gear.png");
        }
    }


    // - - - THE RENDER FUNCTION - - -


    public static void render()
    {
        // - - - create splash screen window
        ImGui.begin("Splash Screen", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(EditorManager.editorScreen.getWidth(), EditorManager.editorScreen.getHeight());
        ImGui.setNextWindowBgAlpha(0f);
        loadTextures();

        // - - - create the logo
        float imageX = (EditorManager.editorScreen.getWidth() - 200) / 2.0f;
        float imageY = (EditorManager.editorScreen.getHeight() - 200) / 2.0f;
        if (timer <= 0.0f)
        {
            imageY -= 100.0f;
        }
        ImGui.setCursorPos(imageX, imageY);
        ImGui.image(logoTexture.getID(), 200, 200, 0, 1, 1, 0);

        // - - - transition
        timer -= EditorManager.editorScreen.getDeltaTime();
        if (timer <= 0f && transitionProgress < 1.0f)
        {
            transitionProgress += transitionSpeed;
            setSizeAndPosition(600, (int) (400 + 200 * transitionProgress));
        }
        else if (transitionProgress >= 1.0f)
        {
            displayWelcomePage();
        }
        ImGui.end();
    }


    // - - - the welcome page
    private static void displayWelcomePage()
    {
        // - - - configure the window
        if (!EditorManager.editorScreen.isDecorated())
        {
            EditorManager.editorScreen.setDecorated(true);
            EditorManager.editorScreen.setAlwaysOnTop(false);
        };

        // - - - put the settings icon
        float gearIconX = EditorManager.editorScreen.getWidth() - gearTexture.getWidth() - 20;
        float gearIconY = 10;
        ImGui.setCursorPos(gearIconX, gearIconY);
        ImGui.pushStyleColor(ImGuiCol.Button, 0);
        if (ImGui.imageButton(gearTexture.getID(), gearTexture.getWidth(), gearTexture.getHeight(), 0, 1, 1, 0))
        {
        //    openSettings();
        }
        ImGui.popStyleColor();

        // - - - make the buttons
        float windowWidth = EditorManager.editorScreen.getWidth();
        float windowHeight = EditorManager.editorScreen.getHeight();
        float buttonWidth = 200;
        float buttonHeight = 40;
        float buttonX = (windowWidth - buttonWidth) / 2.0f;
        float buttonY = (windowHeight * 0.65f);

        ImGui.setCursorPos(buttonX, buttonY);
        if (ImGui.button("Create New Project", buttonWidth, buttonHeight))
        {
            ProjectManager.createNewProject();
        }
        buttonY += buttonHeight + 36.0f;
        ImGui.setCursorPos(buttonX, buttonY);
        if (ImGui.button("Open Existing Project", buttonWidth, buttonHeight))
        {
            ProjectManager.openExistingProject();
        }
    }

    public static void cleanup()
    {
        if (logoTexture != null)
        {
            logoTexture.detach();
            logoTexture = null;
        }
    }
}
