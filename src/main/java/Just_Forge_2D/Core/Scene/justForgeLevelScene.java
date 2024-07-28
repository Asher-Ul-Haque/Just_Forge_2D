package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.justForgeLogger;


// - - - Default Level
public class justForgeLevelScene extends justForgeScene
{
    private float time = 0;
    // - - - TODO: temporary, removed this
    public justForgeLevelScene()
    {
        justForgeLogger.FORGE_LOG_INFO("Current Scene: Default Level");
        Window.get().r = 1;
        Window.get().g = 1;
        Window.get().b = 1;
        Window.get().a = 1;
    }

    @Override
    public void update(double DELTA_TIME)
    {
    }
}
