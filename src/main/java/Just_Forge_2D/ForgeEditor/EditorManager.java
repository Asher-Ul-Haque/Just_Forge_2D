package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.ForgeEditor.Configurations.ColorScheme;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import org.joml.Vector4f;

public class EditorManager
{
    protected static WindowConfig splashScreenConfig = new WindowConfig(600, 400, "Just-Forge-2D", true, WindowSystemManager.getMonitorSize().x, WindowSystemManager.getMonitorSize().y,  1f, true, false, true, true, false, true, DefaultValues.DEFAULT_ICON_PATH);
    protected static WindowConfig editorScreenConfig = new WindowConfig();
    protected static Window splashScreen; // = new Window(splashScreenConfig);
    protected static Window editorScreen;

    public static void run()
    {
        WindowSystemManager.initialize();
        splashScreen = new Window(splashScreenConfig);
        splashScreen.setClearColor(ColorScheme.primaryColor);

        while (!splashScreen.shouldClose())
        {
            splashScreen.loop();
        }
        splashScreen.finish();
        splashScreen.close();

        editorScreen = new Window(editorScreenConfig);

        while (!editorScreen.shouldClose())
        {
            editorScreen.loop();
        }
        editorScreen.finish();
        editorScreen.close();

        WindowSystemManager.terminate();
    }
}
