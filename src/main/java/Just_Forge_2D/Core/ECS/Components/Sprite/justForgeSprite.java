package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Renderer.justForgeTexture;
import org.joml.Vector2f;

public class justForgeSprite
{
    private justForgeTexture texture;
    private Vector2f[] textureCoordinates;

    public justForgeSprite(justForgeTexture TEXTURE)
    {
        this.texture = TEXTURE;
        this.textureCoordinates = new Vector2f[]
                {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
                };
    }

    public justForgeSprite(justForgeTexture TEXTURE, Vector2f[] TEXTURE_COORDINATES)
    {
        this.texture = TEXTURE;
        this.textureCoordinates = TEXTURE_COORDINATES;
    }

    public justForgeTexture getTexture()
    {
        return this.texture;
    }

    public Vector2f[] getTextureCoordinates()
    {
        return this.textureCoordinates;
    }
}
