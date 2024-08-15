package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.Sprite;

import Just_Forge_2D.Renderer.Texture;
import org.joml.Vector2f;


// - - - Utilities for renderering sprites. Need to rename this file
public class Sprite
{
    // - - - private variables for texturing
    private float width, height;
    private Texture texture = null;
    private Vector2f[] textureCoordinates = {
    new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1),
            };


    // - - - | Functions | - - -


    // - - - - Constructors

    // - - - Not useless constructors for once, store textures, invent texture coordinate
    /*public justForgeSprite(justForgeTexture TEXTURE)
    {
        this.texture = TEXTURE;
        this.textureCoordinates = new Vector2f[]{
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1),
                };
    }

    // - - - Store both texture and its coordinates
    public justForgeSprite(justForgeTexture TEXTURE, Vector2f[] TEXTURE_COORDINATES)
    {
        this.texture = TEXTURE;
        this.textureCoordinates = TEXTURE_COORDINATES;
    }*/


    // - - - Getters - - -

    // - - - for texture
    public Texture getTexture()
    {
        return this.texture;
    }

    // - - - for coordinates
    public Vector2f[] getTextureCoordinates()
    {
        return this.textureCoordinates;
    }

    // - - - for width
    public float getWidth()
    {
        return this.width;
    }

    // - - - for height
    public float getHeight()
    {
        return this.height;
    }

    // - - - for textID
    public int getTextureID()
    {
        return texture == null ? -1 : texture.getID();
    }


    // - - - Setters - - -

    // - - - for texture
    public void setTexture(Texture TEXTURE)
    {
        this.texture = TEXTURE;
    }

    // - - - for coordinates
    public void setTextureCoordinates(Vector2f[] TEXTURE_COORDS)
    {
        this.textureCoordinates = TEXTURE_COORDS;
    }

    // - - - for width
    public void setWidth(float NEW_WIDTH)
    {
        this.width = NEW_WIDTH;
    }

    // - - - for height
    public void setHeight(float NEW_HEIGHT)
    {
        this.height = NEW_HEIGHT;
    }
}
