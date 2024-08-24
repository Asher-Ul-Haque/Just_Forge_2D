package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.ForgeEditor.Themes.IntelliTheme;
import Just_Forge_2D.ForgeEditor.Themes.Theme;
import Just_Forge_2D.ForgeEditor.Windows.EditorWindow;
import Just_Forge_2D.ForgeEditor.Windows.SplashScreen;
import Just_Forge_2D.Main;
import Just_Forge_2D.RenderingSystems.Texture;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;


public class EditorManager
{
    public static WindowConfig editorScreenConfig = new WindowConfig();
    public static Window editorScreen;
    public static Scene currentScene;
    public static List<GameObject> currentGameObjects = new ArrayList<>();
    public static Scene testScene;
    public static boolean isSplashScreen = true;
    public static Theme currentTheme;
    public static String projectPath;
    public static GameViewport viewport;
    public static Window test;

    public static void run()
    {
        WindowSystemManager.initialize();
        test = new Window(new WindowConfig());

        test.setClearColor(new Vector4f(1.0f, 1.0f, 1.0f, 1.0f));
        editorScreenConfig.setTransparent(true);
        editorScreenConfig.setVisible(false);
        editorScreen = new EditorWindow(editorScreenConfig);
        SplashScreen.setWindowConfig();
//
        ImGuiManager.initialize(editorScreen.getGlfwWindowPtr());
        currentTheme = new IntelliTheme();

        editorScreen.setVisible(true);
        viewport = new GameViewport(Main.petWindow);
        while (!test.shouldClose())
        {
            editorScreen.loop();
            Main.update();
            //ImGuiManager.update(splashScreen.getDeltaTime());
        }
        editorScreen.finish();
        editorScreen.close();
        WindowSystemManager.terminate();
    }
}
