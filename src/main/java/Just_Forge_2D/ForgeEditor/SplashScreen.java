package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.RenderingSystems.Texture;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;
import org.joml.Vector4f;

public class SplashScreen {
    private static Texture logoTexture;
    private static float transitionProgress = 0.0f;
    private static final float transitionSpeed = 0.02f;
    private static float timer = 5.0f;

    public static void setWindowConfig() {
        EditorManager.editorScreen.setClearColor(new Vector4f(0.5f));
        EditorManager.editorScreen.setAlwaysOnTop(true);
        EditorManager.editorScreen.setDecorated(false);
        setSizeAndPosition(600, 400);
    }

    private static void setSizeAndPosition(int width, int height) {
        EditorManager.editorScreen.setSize(width, height);
        EditorManager.editorScreen.setPosition(
                (WindowSystemManager.getMonitorSize().x - width) / 2,
                (WindowSystemManager.getMonitorSize().y - height) / 2
        );
    }

    protected static void loadTexture() {
        if (logoTexture == null) {
            logoTexture = new Texture();
            logoTexture.init("Assets/Textures/icon.png");
        }
    }

    public static void render() {
        ImGui.begin("Splash Screen", ImGuiWindowFlags.NoTitleBar | ImGuiWindowFlags.NoResize | ImGuiWindowFlags.NoMove);
        ImGui.setWindowPos(0, 0);
        ImGui.setWindowSize(EditorManager.editorScreen.getWidth(), EditorManager.editorScreen.getHeight());

        loadTexture();

        float imageX = (EditorManager.editorScreen.getWidth() - logoTexture.getWidth()) / 2.0f;
        float imageY = (EditorManager.editorScreen.getHeight() - logoTexture.getHeight()) / 3.0f;

        ImGui.setCursorPos(imageX, imageY);
        ImGui.image(logoTexture.getID(), logoTexture.getWidth(), logoTexture.getHeight(), 0, 1, 1, 0);

        // Update timer
        timer -= EditorManager.editorScreen.getDeltaTime();

        // Transition Logic
        if (timer <= 0f && transitionProgress < 1.0f) {
            transitionProgress += transitionSpeed;
            EditorManager.editorScreen.setSize(
                    (int) (600 + 400 * transitionProgress),
                    (int) (400 + 200 * transitionProgress)
            );
            setSizeAndPosition(EditorManager.editorScreen.getWidth(), EditorManager.editorScreen.getHeight());
        } else if (transitionProgress >= 1.0f) {
            displayWelcomePage();
        }

        ImGui.end();
    }

    private static void displayWelcomePage() {
        float windowWidth = EditorManager.editorScreen.getWidth();
        float windowHeight = EditorManager.editorScreen.getHeight();

        float buttonWidth = 200;
        float buttonHeight = 40;
        float buttonX = (windowWidth - buttonWidth) / 2.0f;
        float buttonY = (windowHeight * 0.6f); // Adjust the Y position of the buttons

        ImGui.setCursorPos(buttonX, buttonY);
        if (ImGui.button("Create New Project", buttonWidth, buttonHeight)) {
            // Handle new project creation
        }

        buttonY += buttonHeight + 20.0f; // Adjust spacing between buttons
        ImGui.setCursorPos(buttonX, buttonY);
        if (ImGui.button("Open Existing Project", buttonWidth, buttonHeight)) {
            // Handle opening existing project
        }
    }

    public static void cleanup() {
        if (logoTexture != null) {
            logoTexture.detach();
            logoTexture = null;
        }
    }
}
