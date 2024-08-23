package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;


public class EditorManager
{
    protected static WindowConfig splashScreenConfig = new WindowConfig(600, 400, "Just-Forge-2D", true, WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y,  1f, true, false, true, true, false, true, DefaultValues.DEFAULT_ICON_PATH);
    protected static WindowConfig editorScreenConfig = new WindowConfig();
    protected static Window splashScreen;
    protected static Window editorScreen;
    protected static Scene currentScene;
    protected static GameObject currentGameObject;
    protected static Scene testScene;

    public static void run()
    {
        WindowSystemManager.initialize();
        splashScreen = new Window(splashScreenConfig);

        while (!splashScreen.shouldClose())
        {
            splashScreen.loop();
//            if (!Keyboard.isKeyPressed(Keys.ESCAPE)) continue;

        }
        splashScreen.finish();
        splashScreen.close();

        editorScreen = new EditorWindow(editorScreenConfig);

        ImGuiManager.initialize(editorScreen.getGlfwWindowPtr());
        testScene = SceneSystemManager.addScene(testScene, new TestSceneInitializer(), "Test");
        GameObject g1 = new GameObject("Test Obj");
        testScene.addGameObject(g1);
        testScene.start();
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
