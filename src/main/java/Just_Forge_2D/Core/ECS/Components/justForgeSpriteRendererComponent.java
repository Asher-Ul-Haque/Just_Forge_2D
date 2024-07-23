package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.justForgeLogger;

// - - - Sprite Renderer
public class justForgeSpriteRendererComponent extends justForgeComponent
{
    private boolean isFirstTime = true;

    @Override
    public void start()
    {
        justForgeLogger.FORGE_LOG_DEBUG("I am starting");
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (isFirstTime)
        {
            justForgeLogger.FORGE_LOG_DEBUG("I am updating");
            isFirstTime = false;
        }
    }
}
