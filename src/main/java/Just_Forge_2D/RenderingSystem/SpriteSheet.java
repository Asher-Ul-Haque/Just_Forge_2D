package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

// - - - Sprite Sheets to use texture
public class SpriteSheet
{
    // - - - private variables
    private final Texture texture;
    private final List<Sprite> sprites;
    private final int height;
    private final int width;
    private final int spacing;


    // - - - | Functions | - - -


    // - - - Constructor to make a sprite sheet
    public SpriteSheet(Texture SPRITE_SHEET, int SPRITE_WIDTH, int SPRITE_HEIGHT, int SPRITE_COUNT, int SPACING)
    {
        this.width = SPRITE_WIDTH;
        this.height = SPRITE_HEIGHT;
        this.spacing = SPACING;
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

            addSprite(textureCoords, new Vector2f(SPRITE_WIDTH, SPRITE_HEIGHT));

            currentX += SPRITE_WIDTH + SPACING;
            if (currentX >= SPRITE_SHEET.getWidth())
            {
                currentX = 0;
                currentY -= SPRITE_HEIGHT + SPACING;
            }
        }
    }

    private void addSprite(Vector2f[] COORDS, Vector2f SIZE)
    {
        Sprite sprite = new Sprite();
        sprite.setTexture(this.texture);
        sprite.setTextureCoordinates(COORDS);
        sprite.setWidth(SIZE.x);
        sprite.setHeight(SIZE.y);
        this.sprites.add(sprite);
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

    public int getHeight() {return this.height;}

    public int getWidth() {return this.width;}

    public int getSpacing() {return this.spacing;}

    // - - - getter for texture
    public Texture getTexture()
    {
        return this.texture;
    }


    // - - - Editor
    public void Editor()
    {
        ImGui.text("Sprite Sheet Editor");

        if (Widgets.button(Icons.TrashAlt + " Delete All Sprites"))
        {
            this.sprites.clear();
            Logger.FORGE_LOG_INFO("Cleared all sprites.");
        }

        ImGui.separator();
        for (int i = 0; i < sprites.size(); ++i)
        {
            Sprite sprite = sprites.get(i);
            Vector2f[] texCoords = sprite.getTextureCoordinates();

            ImGui.pushID(i);
            ImGui.text("Sprite " + (i + 1));

            // - - - Display the sprite using the correct texture coordinates with Y-axis flipped
            ImGui.image(sprite.getTexture().getID(), sprite.getWidth(), sprite.getHeight(),
                    texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y);

            // - - - Convert UV coordinates to pixel coordinates (with Y-axis flip)
            Vector2f topLeft = new Vector2f(
                    texCoords[3].x * texture.getWidth(),
                    (1 - texCoords[3].y) * texture.getHeight()  // Flip Y
            );
            Vector2f bottomRight = new Vector2f(
                    texCoords[1].x * texture.getWidth(),
                    (1 - texCoords[1].y) * texture.getHeight()  // Flip Y
            );

            Vector2f size = new Vector2f(sprite.getWidth(), sprite.getHeight());
            Widgets.drawVec2Control("Sprite Size (W, H)", size);
            sprite.setWidth(size.x);
            sprite.setHeight(size.y);
            Widgets.drawVec2Control("Top Left (X, Y)", topLeft);
            Widgets.drawVec2Control("Bottom Right (X, Y)", bottomRight);

            // - - - Convert pixel coordinates back to UV coordinates with Y-axis flip
            texCoords[0].set(bottomRight.x / texture.getWidth(), 1 - (topLeft.y / texture.getHeight())); // Top Right
            texCoords[1].set(bottomRight.x / texture.getWidth(), 1 - (bottomRight.y / texture.getHeight())); // Bottom Right
            texCoords[2].set(topLeft.x / texture.getWidth(), 1 - (bottomRight.y / texture.getHeight())); // Bottom Left
            texCoords[3].set(topLeft.x / texture.getWidth(), 1 - (topLeft.y / texture.getHeight())); // Top Left

            // - - - Option to remove this sprite
            if (Widgets.button(Icons.TrashAlt + " Remove Sprite"))
            {
                this.sprites.remove(i);
                Logger.FORGE_LOG_INFO("Removed sprite " + i);
                ImGui.popID();
                break; // Rebuild the list after removal
            }

            ImGui.separator();
            ImGui.popID();
        }

        if (Widgets.button(Icons.PlusSquare + " Add Sprite"))
        {
            Vector2f topLeft = new Vector2f(0, 0);
            Vector2f bottomRight = new Vector2f(this.width, this.height);

            // - - - Convert pixel coordinates to UV coordinates with Y-axis flip
            Vector2f[] defaultCoords = new Vector2f[] {
                    new Vector2f(bottomRight.x / texture.getWidth(), 1 - (topLeft.y / texture.getHeight())),  // Top Right
                    new Vector2f(bottomRight.x / texture.getWidth(), 1 - (bottomRight.y / texture.getHeight())),  // Bottom Right
                    new Vector2f(topLeft.x / texture.getWidth(), 1 - (bottomRight.y / texture.getHeight())),  // Bottom Left
                    new Vector2f(topLeft.x / texture.getWidth(), 1 - (topLeft.y / texture.getHeight()))   // Top Left
            };

            // - - - Add the new sprite to the list
            addSprite(defaultCoords, new Vector2f(bottomRight.x - topLeft.x, bottomRight.y - topLeft.y));
            Logger.FORGE_LOG_INFO("Added a new sprite.");
        }
    }




}
