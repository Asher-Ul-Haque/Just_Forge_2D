package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import org.joml.Vector2f;


// - - - Utilities for renderering sprites. Need to rename this file
public class Sprite
{
    // - - - private variables for texturing
    private float width = Settings.GRID_WIDTH();
    private float height = Settings.GRID_HEIGHT();
    private TextureMinimizeFilter minimizeFilter = Settings.DEFAULT_TEXTURE_MIN_FILTER();
    private TextureMaximizeFilter maximizeFilter = Settings.DEFAULT_TEXTURE_MAX_FILTER();
    private TextureWrapping wrap_s = Settings.DEFAULT_TEXTURE_WRAP_S();
    private TextureWrapping wrap_t = Settings.DEFAULT_TEXTURE_WRAP_T();
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

    // - - - filters
    public TextureMinimizeFilter getMinimizeFilter() {return minimizeFilter;}
    public TextureMaximizeFilter getMaximizeFilter() {return maximizeFilter;}
    public TextureWrapping getWrap_s() {return wrap_s;}
    public TextureWrapping getWrap_t() {return wrap_t;}


    // - - - Setters - - -

    // - - - for filters

    public void setMinimizeFilter(TextureMinimizeFilter FILTER) {minimizeFilter = FILTER;}
    public void setMaximizeFilter(TextureMaximizeFilter FILTER) {maximizeFilter = FILTER;}
    public void setWrap_sFilter(TextureWrapping FILTER) {wrap_s = FILTER;}
    public void setWrap_tFilter(TextureWrapping FILTER) {wrap_t = FILTER;}

    public void applyTextureFilters()
    {
        if (texture == null)
        {
            Logger.FORGE_LOG_WARNING("Texture is null");
            return;
        }
        texture.setFilters(maximizeFilter, minimizeFilter, wrap_s, wrap_t);
    }


    // - - - for texture
    public void setTexture(Texture TEXTURE)
    {
        this.texture = TEXTURE;
        if (TEXTURE == null) return;
        setWidth(TEXTURE.getWidth());
        setHeight(TEXTURE.getHeight());
        setWrap_tFilter(TEXTURE.getWrap_tFilter());
        setWrap_sFilter(TEXTURE.getWrap_sFilter());
        setMaximizeFilter(TEXTURE.getMaximizeFilter());
        setMinimizeFilter(TEXTURE.getMinimizeFilter());
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
        copy.applyTextureFilters();
        return copy;
    }
}
