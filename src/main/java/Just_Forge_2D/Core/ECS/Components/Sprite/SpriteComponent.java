package Just_Forge_2D.Core.ECS.Components.Sprite;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Editor.ForgeIsGUI;
import Just_Forge_2D.Renderer.Texture;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import org.joml.Vector2f;
import org.joml.Vector4f;

// - - - Component for rendering sprites
public class SpriteComponent extends Component
{
    // - - - private variables - - -
    private final Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    private transient TransformComponent lastTransform = new TransformComponent();
    private transient boolean isChanged = true;


    // - - - | Functions | - - -


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
        }
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
        if (this.gameObject.transform == null) Logger.FORGE_LOG_ERROR(gameObject);
        this.lastTransform = gameObject.transform.copy();
    }

    // - - - Update if data changes
    @Override
    public void update(float DELTA_TIME)
    {
        if (this.gameObject.transform == null) Logger.FORGE_LOG_ERROR(gameObject);
        if (!this.lastTransform.equals(this.gameObject.transform))
        {
            this.gameObject.transform.copy(this.lastTransform);
            this.isChanged = true;
        }
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (this.gameObject.transform == null) Logger.FORGE_LOG_ERROR(gameObject);
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
        if (ForgeIsGUI.colorPicker4("Color Picker", this.color))
        {
            this.isChanged = true;
        }
    }


    // - - - modify properties
    public void setTexture(Texture TEXTURE)
    {
        this.sprite.setTexture(TEXTURE);
    }

    public void setChanged()
    {
        this.isChanged = true;
    }
}