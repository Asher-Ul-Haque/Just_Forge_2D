package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Utils.justForgeLogger;
import org.joml.Vector4f;

// - - - Sprite Renderer
public class justForgeSpriteRendererComponent extends justForgeComponent
{
    private boolean isFirstTime = true;
    private Vector4f color;

    public justForgeSpriteRendererComponent(Vector4f COLOR)
    {
        this.color = COLOR;
    }

    @Override
    public void start()
    {
    }

    @Override
    public void update(float DELTA_TIME)
    {
    }

    public Vector4f getColor()
    {
        return this.color;
    }
}
