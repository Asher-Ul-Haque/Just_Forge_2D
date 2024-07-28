package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Utils.justForgeLogger;

// - - - Font Renderer Component
public class justForgeFont extends Component
{
    // - - - Function to start
    @Override
    public void start()
    {
        if (gameObject.getCompoent(SpriteComponent.class) != null)
        {
            justForgeLogger.FORGE_LOG_DEBUG("Found font renderer");
        }
    }

    // - - - update the font renderer
    @Override
    public void update(float DELTA_TIME)
    {
    }
}
