package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Core.ECS.Components.justForgeComponent;
import Just_Forge_2D.Renderer.justForgeTexture;
import org.joml.Vector2f;
import org.joml.Vector4f;

// - - - Sprite Renderer
public class justForgeSpriteRenderer extends justForgeComponent
{
    private Vector4f color;
    justForgeSprite sprite;

    public justForgeSpriteRenderer(Vector4f COLOR)
    {
        this.sprite = new justForgeSprite(null);
        this.color = COLOR;
    }

    public justForgeSpriteRenderer(justForgeSprite SPRITE)
    {
        this.sprite = SPRITE;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
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

    public justForgeTexture getTexture()
    {
        return sprite.getTexture();
    }

    public Vector2f[] getTextureCoords()
    {
        return sprite.getTextureCoordinates();
    }
}
