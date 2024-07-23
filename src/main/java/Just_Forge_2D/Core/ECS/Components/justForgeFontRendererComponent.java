package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.justForgeLogger;

public class justForgeFontRendererComponent extends justForgeComponent
{
    @Override
    public void start()
    {
        if (gameObject.getCompoent(justForgeSpriteRendererComponent.class) != null)
        {
            justForgeLogger.FORGE_LOG_DEBUG("Found font renderer");
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {

    }
}
