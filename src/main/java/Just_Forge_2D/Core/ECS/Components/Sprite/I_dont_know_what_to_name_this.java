package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Core.ECS.Components.justForgeComponent;
import Just_Forge_2D.Renderer.justForgeTexture;
import org.joml.Vector2f;
import org.joml.Vector4f;

// - - - Sprite Renderer
public class I_dont_know_what_to_name_this extends justForgeComponent
{
    private boolean isFirstTime = true;
    private Vector4f color;
    private Vector2f[] textureCoords;
    private justForgeTexture texture;

    public I_dont_know_what_to_name_this(Vector4f COLOR)
    {
        this.color = COLOR;
        this.texture = null;
    }
    public I_dont_know_what_to_name_this(justForgeTexture TEXTURE)
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
                new Vector2f(1.0f, 1.0f),
                new Vector2f(1.0f, 0.0f),
                new Vector2f(0.0f, 1.0f),
                new Vector2f(0.0f, 0.0f)
        };
        return textureCoords;
    }
}
