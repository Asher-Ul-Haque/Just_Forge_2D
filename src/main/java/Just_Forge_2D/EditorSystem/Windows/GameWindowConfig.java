package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.WindowSystem.WindowConfig;
import Just_Forge_2D.WindowSystem.WindowSystemManager;

public class GameWindowConfig extends WindowConfig
{
    public GameWindowConfig()
    {
        super();
        this.width = WindowSystemManager.getMonitorSize().x;
        this.height = WindowSystemManager.getMonitorSize().y;
        this.visible = false;
        this.transparent = true;
    }
}
