package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EditorSystem.Windows.AssetPoolDisplay;
import Just_Forge_2D.RenderingSystem.*;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.function.Consumer;

// - - - Component for rendering sprites
public class SpriteComponent extends Component
{
    // - - - private variables - - -
    private final Vector4f color = new Vector4f(1, 1, 1, 1);
    private Sprite sprite = new Sprite();
    private transient TransformComponent lastTransform = new TransformComponent();
    private transient boolean isChanged = true;
    private boolean showAtRuntime = true;
    private boolean read = false;
    private boolean wrote = false;


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

    public Sprite getSpriteCopy()
    {
        Sprite copy = new Sprite();
        copy.setHeight(this.sprite.getHeight());
        copy.setWidth(this.sprite.getWidth());
        copy.setTexture(this.getTexture());
        copy.setTextureCoordinates(this.getTextureCoords());
        return copy;
    }

    public void setSprite(Sprite SPRITE)
    {
        if (SPRITE.equals(this.sprite)) return;
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
        if (sprite.getTexture() != null)
        {
            this.sprite.setTexture(AssetPool.makeTexture(this.sprite.getTexture().getFilepath()));
            applyTextureFilters();
        }
        this.lastTransform = gameObject.transform.copy();
        read = true;
        wrote = false;
    }

    public TextureMinimizeFilter getMinimizeFilter()
    {
        return sprite.getTexture().getMinimizeFilter();
    }

    public TextureMaximizeFilter getMaximizeFilter()
    {
        return sprite.getTexture().getMaximizeFilter();
    }

    public TextureWrapping getWrap_sFilter()
    {
        return sprite.getTexture().getWrap_sFilter();
    }

    public TextureWrapping getWrap_tFilter()
    {
        return sprite.getTexture().getWrap_tFilter();
    }

    public void setMinimizeFilter(TextureMinimizeFilter FILTER)
    {
        getTexture().setMIN_FILTER(FILTER);
    }

    public void setMaximizeFilter(TextureMaximizeFilter FILTER)
    {
        getTexture().setMAX_FILTER(FILTER);
    }

    public void setWrap_s(TextureWrapping WRAP)
    {
        getTexture().setWARP_S(WRAP);
    }

    public void setWrap_t(TextureWrapping WRAP)
    {
        getTexture().setWARP_T(WRAP);
    }

    public void applyTextureFilters()
    {
        getTexture().init(getTexture().getFilepath());
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
        if (!read)
        {
            applyTextureFilters();
            Renderer renderer = GameWindow.getCurrentScene().getRenderer();
            if (renderer != null)
            {
                start();
                renderer.add(this.gameObject);
            }
        }
    }



    // - - - Editor Functionality - - -

    public static Sprite spriteGUI(Sprite SPR, Consumer<Sprite> CALLBACK, int HASHCODE)
    {
        if (AssetPoolDisplay.getMode().equals(AssetPoolDisplay.Mode.SPRITE_SELECTION))
        {
            Widgets.text("Click on any Texture or Sprite Sheet in the Asset Pool");
        }
        if (SPR.getTexture() != null)
        {
            Vector2f[] texCoords = SPR.getTextureCoordinates();
            if (Widgets.imageButton(SPR.getTextureID(), SPR.getWidth() * 4, SPR.getHeight() * 4, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y, true))
            {
                AssetPoolDisplay.enableSpriteSelection(CALLBACK);
            }

            if (Widgets.button(Icons.Trash + " ##" + HASHCODE, true))
            {
                return new Sprite();
            }
        }
        else
        {
            if (Widgets.button(Icons.Image + " ##" + HASHCODE, true))
            {
                AssetPoolDisplay.enableSpriteSelection(CALLBACK);
            }
        }
        return SPR;
    }

    @Override
    public void editorGUI()
    {
        super.deleteButton();
        setSprite(spriteGUI(this.sprite, this::setSprite, this.hashCode()));
        if (getTexture() != null)
        {
            setMaximizeFilter(Widgets.drawEnumControls(TextureMaximizeFilter.class, Icons.Box + "  Texture Maximize Filter", getMaximizeFilter()));
            setMinimizeFilter(Widgets.drawEnumControls(TextureMinimizeFilter.class, Icons.Box + "  Texture Minimize Filter", getMinimizeFilter()));
            setWrap_s(Widgets.drawEnumControls(TextureWrapping.class, Icons.Box + "  Texture Wrap -S Filter", getWrap_sFilter()));
            setWrap_t(Widgets.drawEnumControls(TextureWrapping.class, Icons.Box + "  Texture Wrap -T  Filter", getWrap_tFilter()));

            if (Widgets.button("Apply Filter" + " ##" + this.hashCode(), true))
            {
                applyTextureFilters();
            }
        }
        if (Widgets.colorPicker4(Icons.EyeDropper +"  Color Picker", this.color)) this.isChanged = true;
        setShowAtRuntime(Widgets.drawBoolControl((getShowAtRuntime() ? Icons.Eye : Icons.EyeSlash) + "  Show", getShowAtRuntime()));
        Widgets.text("");
    }


    // - - - modify properties
    public void setTexture(Texture TEXTURE)
    {
        this.sprite.setTexture(TEXTURE);
        setChanged();
    }

    public void setChanged()
    {
        this.isChanged = true;
    }

    @Override
    public void destroy()
    {
        Renderer renderer = GameWindow.getCurrentScene().getRenderer();
        if (renderer != null)
        {
            renderer.destroyGameObject(this.gameObject);
        }
        this.isChanged = true;
    }
}