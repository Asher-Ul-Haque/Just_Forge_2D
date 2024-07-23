package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.justForgeCamera;

// - - - Abstract class for all the scenes
public abstract class justForgeScene
{
    protected justForgeCamera camera;

    public justForgeScene()
    {
    }
    public abstract void update(double DELTA_TIME);
    public void init(){}
}
