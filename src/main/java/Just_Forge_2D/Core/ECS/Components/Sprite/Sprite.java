package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Renderer.justForgeTexture;
import org.joml.Vector2f;


// - - - Utilities for renderering sprites. Need to rename this file
public class Sprite
{
    // - - - private variables for texturing
    private justForgeTexture texture = null;
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
    public justForgeTexture getTexture()
    {
        return this.texture;
    }

    // - - - for coordinates
    public Vector2f[] getTextureCoordinates()
    {
        return this.textureCoordinates;
    }


    // - - - Setters - - -

    // - - - for texture
    public void setTexture(justForgeTexture TEXTURE)
    {
        this.texture = TEXTURE;
    }

    // - - - for coordinates
    public void setTextureCoordinates(Vector2f[] TEXTURE_COORDS)
    {
        this.textureCoordinates = TEXTURE_COORDS;
    }
}
