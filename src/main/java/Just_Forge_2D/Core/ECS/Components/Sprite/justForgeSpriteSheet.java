package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Renderer.justForgeTexture;
import org.joml.Vector2f;
import java.util.ArrayList;
import java.util.List;

// - - - Sprite Sheets to use texture
public class justForgeSpriteSheet
{
    // - - - private variables
    private justForgeTexture spriteSheet;
    private List<justForgeSprite> sprites;


    // - - - | Functions | - - -


    // - - - Constructor to make a sprite sheet
    public justForgeSpriteSheet(justForgeTexture SPRITE_SHEET, int SPRITE_WIDTH, int SPRITE_HEIGHT, int SPRITE_COUNT, int SPACING)
    {
        this.sprites = new ArrayList<>();
        this.spriteSheet = SPRITE_SHEET;

        int currentX = 0;
        int currentY = SPRITE_SHEET.getHeight() - SPRITE_HEIGHT; // bototm left corner of top left sprite

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

            justForgeSprite sprite = new justForgeSprite(this.spriteSheet, textureCoords);
            this.sprites.add(sprite);

            currentX += SPRITE_WIDTH + SPACING;
            if (currentX >= SPRITE_SHEET.getWidth())
            {
                currentX = 0;
                currentY -= SPRITE_HEIGHT + SPACING;
            }
        }
    }

    // - - - getter to get a sprite by the index
    public justForgeSprite getSprite(int INDEX)
    {
        return this.sprites.get(INDEX);
    }

}
