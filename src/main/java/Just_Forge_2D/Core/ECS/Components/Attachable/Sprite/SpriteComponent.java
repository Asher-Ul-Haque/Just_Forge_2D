package Just_Forge_2D.Core.ECS.Components.Attachable.Sprite;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Editor.ForgeGUI;
import Just_Forge_2D.Renderer.Texture;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
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
        //justForgeLogger.FORGE_LOG_WARNING("Previous color and current color equal when changing color of a sprite: " + this.sprite);
    }

    // - - - Texture
    public Texture getTexture()
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
        if (this.gameObject.transform == null) justForgeLogger.FORGE_LOG_ERROR(gameObject);
        this.lastTransform = gameObject.transform.copy();
    }

    // - - - Update if data changes
    @Override
    public void update(float DELTA_TIME)
    {
        if (this.gameObject.transform == null) justForgeLogger.FORGE_LOG_ERROR(gameObject);
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
        if (ForgeGUI.colorPicker4("COlor Picker", this.color))
        {
            this.isChanged = true;
        }
    }

    public void setTexture(Texture TEXTURE)
    {
        this.sprite.setTexture(TEXTURE);
    }
}