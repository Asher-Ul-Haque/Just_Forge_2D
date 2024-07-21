package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.justForgeWindow;

public class justForgeLevelScene extends justForgeScene
{
    public justForgeLevelScene()
    {
        System.out.println("Inside Level Scene");
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
