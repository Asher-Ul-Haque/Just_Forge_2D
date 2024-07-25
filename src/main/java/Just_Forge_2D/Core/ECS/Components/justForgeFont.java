package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Utils.justForgeLogger;

// - - - Renderer Component
public class justForgeFont extends justForgeComponent
{
    @Override
    public void start()
    {
        if (gameObject.getCompoent(justForgeSprite.class) != null)
        {
            justForgeLogger.FORGE_LOG_DEBUG("Found font renderer");
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {

    }
}
