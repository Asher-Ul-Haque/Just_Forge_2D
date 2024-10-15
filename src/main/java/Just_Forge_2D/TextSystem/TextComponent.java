package Just_Forge_2D.TextSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class TextComponent extends Component
{

    private String text = "Just\tForge\n2D";
    private transient List<GameObject> characters;
    private float characterSpacing = 1f;
    private float tabSpacing = 4 * characterSpacing;
    private float lineHeight = 24f;
    private float size = 1f;
    private int layer = 0;
    private String font = "font";
    private Vector4f characterColor = new Vector4f(1.0f);
    private Vector2f position = new Vector2f();
    private boolean moveWithMaster = true;
    private transient TransformComponent masterPrev = new TransformComponent();

    public TextComponent()
    {
        this.characters = new ArrayList<>();
    }

    @Override
    public void start()
    {
        if (moveWithMaster)
        {
            position.set(this.gameObject.transform.position);
            this.gameObject.transform.copy(masterPrev);
        }
        parseText();
    }

    private void parseText()
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

            // - - - Handle newlines
            if (currentChar == '\n')
            {
                xPos = 0f;
                yPos -= lineHeight;
                continue;
            }

            // - - - Handle tabs
            if (currentChar == '\t')
            {
                xPos += tabSpacing;
                continue;
            }

            // - - - Handle spaces
            if (currentChar == ' ')
            {
                xPos += characterSpacing;
                continue;
            }

            // - - - Create a new GameObject for the character
            GameObject charObject = createCharacterObject(currentChar, sheet);
            charObject.transform.position.set(position).add(xPos, yPos);
            characters.add(charObject);

            // - - - Move xPos for the next character
            xPos += characterSpacing;
        }
    }

    private GameObject createCharacterObject(char character, SpriteSheet FONT)
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

    private int getSpriteIndex(char character)
    {
        if (character >= 'a' && character <= 'z') return character - 'a';  // - - - Sprites 0-25 for 'a' to 'z'
        else if (character >= '0' && character <= '9') return character - '0' + 26;  // - - - Sprites 26-35 for '0' to '9'
        return 0;  // - - - Handle any unsupported character (can be expanded)
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
            this.gameObject.transform.copy(masterPrev);
            this.position.add(new Vector2f(this.gameObject.transform.position).sub(masterPrev.position));
        }
    }

    @Override
    public void editorGUI()
    {
        if (ImGui.button("Remove"))
        {
            this.gameObject.removeComponent(this.getClass());
            destroy();
        }
        String temp = Widgets.inputText("Text", text);
        String sheet = Widgets.inputText("Font SpriteSheet", font);
        float spacing = Widgets.drawFloatControl("Spacing", characterSpacing);
        float tab = Widgets.drawFloatControl("Tab", tabSpacing);
        float height = Widgets.drawFloatControl("Line Height", lineHeight);
        float size2 = Widgets.drawFloatControl("Size", size);
        int l = Widgets.drawIntControl("Layer", layer);
        Vector2f pos = new Vector2f(position);
        Widgets.drawVec2Control("Position", pos);
        moveWithMaster = Widgets.drawBoolControl("Move With Master", moveWithMaster);
        Vector4f copy = new Vector4f(characterColor);
        Widgets.colorPicker4("Color", copy);
        if (!text.equals(temp) || !sheet.equals(font) || spacing != characterSpacing || tab != tabSpacing || height != lineHeight || size2 != size || !copy.equals(characterColor) || !pos.equals(position) || l != layer)
        {
            text = temp;
            font = sheet;
            characterSpacing = spacing;
            tabSpacing = tab;
            lineHeight = height;
            size = size2;
            layer = l;
            characterColor.set(copy);
            if (moveWithMaster) position.set(this.gameObject.transform.position);
            else position.set(pos);
            setText(text);
        }
    }

    @Override
    public void destroy()
    {
        for (GameObject g : characters) g.destroy();
    }
}
