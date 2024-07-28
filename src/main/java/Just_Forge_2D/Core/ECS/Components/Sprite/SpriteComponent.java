package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Renderer.justForgeTexture;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

// - - - Component for rendering sprites
public class SpriteComponent extends Component
{
    // - - - private variables - - -
    private Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    private transient TransformComponent lastTransform = new TransformComponent();
    private transient boolean isChanged = true;


    // - - - | Functions | - - -


    // - - - Constructors - - -

    // - - - store color and make a sprite without texture
    /*public justForgeSpriteRenderer(Vector4f COLOR)
    {
        this.sprite = new justForgeSprite(null);
        this.color = COLOR;
    }

    // - - - well just take in the sprite and give no tint
    public justForgeSpriteRenderer(justForgeSprite SPRITE)
    {
        this.sprite = SPRITE;
        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
    }*/


    // - - - Getters and Setters - - -

    // - - - Color
    public Vector4f getColor()
    {
        return this.color;
    }

    public void setColor(Vector4f COLOR)
    {
        if (!this.color.equals(COLOR))
        {
            this.color.set(COLOR);
            this.isChanged = true;
            return;
        }
        justForgeLogger.FORGE_LOG_WARNING("Previous color and current color equal when changing color of a sprite: " + this.sprite);
    }

    // - - - Texture
    public justForgeTexture getTexture()
    {
        return sprite.getTexture();
    }

    public Vector2f[] getTextureCoords()
    {
        return sprite.getTextureCoordinates();
    }

    // - - - Sprite
    public void setSprite(Sprite SPRITE)
    {
        this.sprite = SPRITE;
        this.isChanged = true;
    }

    // - - - Animations
    public boolean isChanged()
    {
        return this.isChanged;
    }

    public void clean()
    {
        this.isChanged = false;
    }


    // - - - Use Functions - - -

    // - - - Start because constructors are pointless
    @Override
    public void start()
    {
        this.lastTransform = gameObject.transform.copy();
    }

    // - - - Update if data changes
    @Override
    public void update(float DELTA_TIME)
    {
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            this.isChanged = true;
        }
    }


    // - - - Editor Functionality - - -

    @Override
    public void editorGUI()
    {
        float[] inColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color Picker: ", inColor))
        {
            this.color.set(inColor[0], inColor[1], inColor[2], inColor[3]);
            this.isChanged = true;
        }
    }
}