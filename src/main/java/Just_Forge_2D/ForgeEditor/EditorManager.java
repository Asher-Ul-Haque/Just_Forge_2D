package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.ForgeEditor.Themes.IntelliTheme;
import Just_Forge_2D.ForgeEditor.Themes.Theme;
import Just_Forge_2D.ForgeEditor.Windows.EditorWindow;
import Just_Forge_2D.ForgeEditor.Windows.SplashScreen;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;

import java.util.ArrayList;
import java.util.List;


public class EditorManager
{
    public static WindowConfig splashScreenConfig = new WindowConfig(600, 400, "Just-Forge-2D", true, WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y,  1f, true, false, true, true, false, true, DefaultValues.DEFAULT_ICON_PATH);
    public static WindowConfig editorScreenConfig = new WindowConfig();
    public static Window editorScreen;
    public static Scene currentScene;
    public static List<GameObject> currentGameObjects = new ArrayList<>();
    public static Scene testScene;
    public static boolean isSplashScreen = true;
    public static Theme currentTheme;

    public static void run()
    {
        WindowSystemManager.initialize();
        editorScreenConfig.setTransparent(true);
        editorScreen = new EditorWindow(editorScreenConfig);
        SplashScreen.setWindowConfig();

        ImGuiManager.initialize(editorScreen.getGlfwWindowPtr());
        currentTheme = new IntelliTheme();

        testScene = SceneSystemManager.addScene(testScene, new TestSceneInitializer(), "Test");
        GameObject g1 = new GameObject("Test Obj");
        testScene.addGameObject(g1);
        testScene.start();
        editorScreen.setVisible(true);
        while (!editorScreen.shouldClose())
        {
            editorScreen.loop();
            //ImGuiManager.update(splashScreen.getDeltaTime());
        }
        editorScreen.finish();
        editorScreen.close();
        WindowSystemManager.terminate();
    }
}
