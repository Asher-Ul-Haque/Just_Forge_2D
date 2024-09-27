package Just_Forge_2D.EditorSystem.Windows;

import WindowSystem.WindowConfig;
import WindowSystem.WindowSystemManager;

public class MainWindowConfig extends WindowConfig
{
    public MainWindowConfig()
    {
        super();
        this.width = WindowSystemManager.getMonitorSize().x;
        this.height = WindowSystemManager.getMonitorSize().y;
        this.visible = false;
        this.transparent = true;
    }
}
