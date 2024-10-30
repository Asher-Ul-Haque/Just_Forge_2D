package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EditorSystem.Windows.AssetPoolDisplay;
import Just_Forge_2D.RenderingSystem.Sprite;

public class Frame
{
    public Sprite sprite;
    public float frameTime;

    public Frame(Sprite SPRITE, float TIME)
    {
        this.sprite = SPRITE;
        this.frameTime = TIME;
    }

    public void editorGUI()
    {
        // - - - Show sprite preview as an image button
        if (Widgets.imageButton(sprite.getTextureID(), sprite.getWidth(), sprite.getHeight(), sprite.getTextureCoordinates()))
        {
            AssetPoolDisplay.enableSelection((Sprite SP)->{this.sprite = SP;});
        }

        // - - - Editable frame time
        Widgets.drawFloatControl(Icons.Clock + "  Frame Time", frameTime);
    }
}