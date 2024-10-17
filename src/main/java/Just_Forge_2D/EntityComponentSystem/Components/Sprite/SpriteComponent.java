package Just_Forge_2D.EntityComponentSystem.Components.Sprite;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.Logger;
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
    private boolean showAtRuntime = true;


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

    public boolean getShowAtRuntime()
    {
        return showAtRuntime;
    }

    public void setShowAtRuntime(boolean REALLY)
    {
        this.showAtRuntime = REALLY;
        this.isChanged = true;
    }

    // - - - Use Functions - - -

    // - - - Start because constructors are pointless
    @Override
    public void start()
    {
        if (this.gameObject.transform == null)
        {
            this.sprite.setTexture(AssetPool.makeTexture(this.sprite.getTexture().getFilepath()));
        }
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
        update(DELTA_TIME);
    }



    // - - - Editor Functionality - - -

    @Override
    public void editorGUI()
    {
        if (Widgets.button(Icons.Trash + " Destroy##" + this.getClass().hashCode()))
        {
            this.isChanged = true;
            this.gameObject.removeComponent(this.getClass());
        }
        if (Widgets.colorPicker4(Icons.EyeDropper +"  Color Picker", this.color)) this.isChanged = true;
        setShowAtRuntime(Widgets.drawBoolControl((getShowAtRuntime() ? Icons.Eye : Icons.EyeSlash) + "  Show", getShowAtRuntime()));
        if (this.sprite.getTexture() != null)
        {
            Vector2f[] texCoords = this.sprite.getTextureCoordinates();
            if (Widgets.imageButton(this.sprite.getTextureID(), this.sprite.getWidth() * 2, this.sprite.getHeight() * 2, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y));
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