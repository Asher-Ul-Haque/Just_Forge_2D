package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Renderer.justForgeTexture;
import org.joml.Vector2f;
import org.joml.Vector4f;

// - - - Sprite Renderer
public class justForgeSprite extends justForgeComponent
{
    private boolean isFirstTime = true;
    private Vector4f color;
    private Vector2f textureCoords[];
    private justForgeTexture texture = null;

    public justForgeSprite(Vector4f COLOR)
    {
        this.color = COLOR;
    }

    public justForgeSprite(justForgeTexture TEXTURE)
    {
        this.texture = TEXTURE;
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
        return this.texture;
    }

    public Vector2f[] getTextureCoords()
    {
        Vector2f[] textureCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
        };
        return textureCoords;
    }
}
