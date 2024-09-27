package RenderingSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

// - - - Sprite Sheets to use texture
public class SpriteSheet
{
    // - - - private variables
    private final Texture texture;
    private final List<Sprite> sprites;


    // - - - | Functions | - - -


    // - - - Constructor to make a sprite sheet
    public SpriteSheet(Texture SPRITE_SHEET, int SPRITE_WIDTH, int SPRITE_HEIGHT, int SPRITE_COUNT, int SPACING)
    {
        this.sprites = new ArrayList<>();
        this.texture = SPRITE_SHEET;

        int currentX = 0;
        int currentY = SPRITE_SHEET.getHeight() - SPRITE_HEIGHT; // bottom left corner of top left sprite

        for (int i = 0; i < SPRITE_COUNT; ++i)
        {
            //  normalized coordinates
            float topY = (currentY + SPRITE_HEIGHT) / (float)SPRITE_SHEET.getHeight();
            float rightX = (currentX + SPRITE_WIDTH) / (float)SPRITE_SHEET.getWidth();
            float leftX = currentX / (float)SPRITE_SHEET.getWidth();
            float bottomY = currentY / (float)SPRITE_SHEET.getHeight();

            Vector2f[] textureCoords = new Vector2f[]{
                            new Vector2f(rightX, topY),
                            new Vector2f(rightX, bottomY),
                            new Vector2f(leftX, bottomY),
                            new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTextureCoordinates(textureCoords);
            sprite.setWidth(SPRITE_WIDTH);
            sprite.setHeight(SPRITE_HEIGHT);
            this.sprites.add(sprite);

            currentX += SPRITE_WIDTH + SPACING;
            if (currentX >= SPRITE_SHEET.getWidth())
            {
                currentX = 0;
                currentY -= SPRITE_HEIGHT + SPACING;
            }
        }
    }

    // - - - getter for sprites

    public Sprite getSprite(int INDEX)
    {
        return this.sprites.get(INDEX);
    }

    public List<Sprite> getSprites()
    {
        return this.sprites;
    }

    // - - - getter to get size
    public int size()
    {
        return this.sprites.size();
    }

    // - - - getter for texture
    public Texture getTexture()
    {
        return this.texture;
    }



}
