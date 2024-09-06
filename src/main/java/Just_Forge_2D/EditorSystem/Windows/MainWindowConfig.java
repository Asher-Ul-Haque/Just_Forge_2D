package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;

public class MainWindowConfig extends WindowConfig
{
    public MainWindowConfig()
    {
        super();
        this.x = WindowSystemManager.getMonitorSize().x;
        this.y = WindowSystemManager.getMonitorSize().y;
        this.visible = false;
    }
}
