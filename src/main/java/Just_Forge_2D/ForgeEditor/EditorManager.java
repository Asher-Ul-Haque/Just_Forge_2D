package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.ForgeEditor.Configurations.ColorScheme;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
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
