package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Core.ECS.Components.justForgeComponent;
import Just_Forge_2D.Renderer.justForgeTexture;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Utils.justForgeTransform;
import org.joml.Vector2f;
import org.joml.Vector4f;

// - - - Sprite Renderer
public class justForgeSpriteRenderer extends justForgeComponent
{
    private Vector4f color;
    justForgeSprite sprite;
    private justForgeTransform lastTransform;
    private boolean isChanged = true;

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
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            this.isChanged = true;
        }
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

    public void setSprite(justForgeSprite SPRITE)
    {
        this.sprite = SPRITE;
        this.isChanged = true;
    }

    public void setColor(Vector4f COLOR)
    {
        if (!this.color.equals(COLOR))
        {
            this.color.set(COLOR);
            this.isChanged = true;
            return;
        }
        justForgeLogger.FORGE_LOG_WARNING("Previous color and current color equal when changing color of a sprite: " + this.sprite);
    }

    public boolean isChanged()
    {
        return this.isChanged;
    }

    public void clean()
    {
        this.isChanged = false;
    }
}
