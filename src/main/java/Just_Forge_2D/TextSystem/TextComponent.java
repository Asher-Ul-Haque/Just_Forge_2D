package Just_Forge_2D.TextSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;


public class TextComponent extends Component
{
    protected String text = "Just\tForge\n2D";
    protected transient List<GameObject> characters;
    protected float characterSpacing = 1f;
    protected float tabSpacing = 4 * characterSpacing;
    protected float lineHeight = 1f;
    protected float size = 1f;
    protected int layer = 0;
    protected String font = "font";
    protected Vector4f characterColor = new Vector4f(1.0f);
    protected boolean moveWithMaster = true;
    protected Vector2f firstCharacterOffset = new Vector2f();

    public TextComponent()
    {
        this.characters = new ArrayList<>();
    }

    @Override
    public void start()
    {
        parseText();
    }

    protected void parseText()
    {
        SpriteSheet sheet = AssetPool.getSpriteSheet(font);
        if (sheet == null)
        {
            Logger.FORGE_LOG_ERROR("No SpriteSheet named : " + font + " exists in the asset pool");
            return;
        }
        float xPos = 0f;
        float yPos = 0f;

        for (int i = 0; i < text.length(); i++)
        {
            char currentChar = text.toLowerCase().charAt(i);

            if (currentChar == '\n')
            {
                xPos = 0f;
                yPos -= lineHeight;
                continue;
            }

            if (currentChar == '\t')
            {
                xPos += tabSpacing;
                continue;
            }

            if (currentChar == ' ')
            {
                xPos += characterSpacing;
                continue;
            }

            GameObject charObject = createCharacterObject(currentChar, sheet);
            charObject.transform.position.set(this.gameObject.transform.position).add(xPos, yPos).add(firstCharacterOffset);
            characters.add(charObject);

            if (i == 0) firstCharacterOffset.set(charObject.transform.position).sub(this.gameObject.transform.position);

            xPos += characterSpacing;
        }
    }

    protected GameObject createCharacterObject(char character, SpriteSheet FONT)
    {
        int spriteIndex = getSpriteIndex(character);
        Sprite sprite = FONT.getSprite(spriteIndex);
        GameObject object = PrefabManager.generateDefaultSpriteObject(sprite, size, size);
        object.name = this.gameObject + " char : " + character +  " index : " + spriteIndex;
        GameWindow.getCurrentScene().addGameObject(object);
        object.getComponent(SpriteComponent.class).setColor(characterColor);
        object.transform.layer = layer;
        object.noSerialize();
        return object;
    }

    protected int getSpriteIndex(char character)
    {
        if (character >= 'a' && character <= 'z') return character - 'a';  // Sprites 0-25 for 'a' to 'z'
        else if (character >= '0' && character <= '9') return character - '0' + 26;  // Sprites 26-35 for '0' to '9'
        return 0;  // Handle any unsupported character (can be expanded)
    }

    public void setText(String newText)
    {
        this.text = newText;
        for (GameObject g : characters) g.destroy();
        this.characters.clear();
        parseText();
    }

    public String getText()
    {
        return this.text;
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (moveWithMaster)
        {
            Vector2f masterPosition = new Vector2f(this.gameObject.transform.position);
            Vector2f firstCharacterPosition = new Vector2f(this.characters.get(0).transform.position);
            for (GameObject character : this.characters)
            {
                Vector2f characterOffset = new Vector2f(character.transform.position).sub(firstCharacterPosition);
                character.transform.position.set(masterPosition).add(firstCharacterOffset).add(characterOffset);
            }
        }
    }




    @Override
    public void editorGUI()
    {
        if (Widgets.button(Icons.Trash + " Destroy##" + this.getClass().hashCode()))
        {
            this.gameObject.removeComponent(this.getClass());
        }
        String temp = Widgets.inputText(Icons.Comment + "  Text", text);
        String sheet = Widgets.inputText(Icons.Font + "  Font SpriteSheet", font);
        float spacing = Widgets.drawFloatControl(Icons.TextWidth + "  Spacing", characterSpacing);
        float tab = Widgets.drawFloatControl(Icons.TextWidth + "  Tab", tabSpacing);
        float height = Widgets.drawFloatControl(Icons.TextHeight + "  Line Height", lineHeight);
        float size2 = Widgets.drawFloatControl(Icons.TextHeight + "  Size", size);
        int l = Widgets.drawIntControl(Icons.LayerGroup + "  Layer", layer);
        Vector2f pos = new Vector2f(firstCharacterOffset);
        Vector4f copy = new Vector4f(characterColor);
        Widgets.colorPicker4(Icons.EyeDropper + "  Color", copy);
        Widgets.drawVec2Control(Icons.Crosshairs + "  Offset", pos);
        moveWithMaster = Widgets.drawBoolControl((moveWithMaster ? Icons.Walking : Icons.Wheelchair) + "  Move With Master", moveWithMaster);
        if (!text.equals(temp) || !sheet.equals(font) || spacing != characterSpacing || tab != tabSpacing || height != lineHeight || size2 != size || !copy.equals(characterColor) || !pos.equals(firstCharacterOffset) || l != layer)
        {
            text = temp;
            font = sheet;
            characterSpacing = spacing;
            tabSpacing = tab;
            lineHeight = height;
            size = size2;
            layer = l;
            characterColor.set(copy);
            firstCharacterOffset.set(pos);
            setText(text);
        }
    }

    @Override
    public void destroy()
    {
        for (GameObject g : characters) g.destroy();
    }
}