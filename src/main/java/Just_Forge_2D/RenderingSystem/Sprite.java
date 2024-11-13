package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.Utils.Settings;
import org.joml.Vector2f;


// - - - Utilities for renderering sprites. Need to rename this file
public class Sprite
{
    // - - - private variables for texturing
    private float width = Settings.GRID_WIDTH();
    private float height = Settings.GRID_HEIGHT();
    private Texture texture = null;
    private Vector2f[] textureCoordinates = {
    new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1),
            };


    // - - - | Functions | - - -


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
        if (TEXTURE == null) return;
        if (width == 0) setWidth(TEXTURE.getWidth());
        if (height == 0) setHeight(TEXTURE.getHeight());
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

    public Sprite copy()
    {
        Sprite copy = new Sprite();
        copy.setWidth(this.getWidth());
        copy.setHeight(this.getHeight());
        copy.setTexture(this.getTexture());
        copy.setTextureCoordinates(this.getTextureCoordinates());
        return copy;
    }
}
