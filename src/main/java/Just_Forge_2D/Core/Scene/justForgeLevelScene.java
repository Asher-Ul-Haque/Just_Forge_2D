package Just_Forge_2D.Core.Scene;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Core.justForgeWindow;


// - - - Default Level
public class justForgeLevelScene extends justForgeScene
{
    private float time = 0;
    // - - - TODO: temporary, removed this
    public justForgeLevelScene()
    {
        justForgeLogger.FORGE_LOG_INFO("Current Scene: Default Level");
        justForgeWindow.get().r = 1;
        justForgeWindow.get().g = 1;
        justForgeWindow.get().b = 1;
        justForgeWindow.get().a = 1;
    }

    @Override
    public void update(double DELTA_TIME)
    {
    }
}
