package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EditorSystem.Windows.AssetPoolDisplay;
import Just_Forge_2D.RenderingSystem.*;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
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

    public Sprite getSpriteCopy()
    {
        Sprite copy = new Sprite();
        this.sprite.applyTextureFilters();
        copy.setHeight(this.sprite.getHeight());
        copy.setWidth(this.sprite.getWidth());
        copy.setTexture(this.getTexture());
        copy.setTextureCoordinates(this.getTextureCoords());
        copy.setMinimizeFilter(this.sprite.getMinimizeFilter());
        copy.setMaximizeFilter(this.sprite.getMaximizeFilter());
        copy.setWrap_sFilter(this.sprite.getWrap_s());
        copy.setWrap_tFilter(this.sprite.getWrap_t());
        return copy;
    }

    public void setSprite(Sprite SPRITE)
    {
        this.sprite = SPRITE;
        this.sprite.applyTextureFilters();
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
        this.sprite.setTexture(AssetPool.makeTexture(this.sprite.getTexture().getFilepath()));
        this.sprite.applyTextureFilters();
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
        super.deleteButton();
        if (AssetPoolDisplay.getMode().equals(AssetPoolDisplay.Mode.SELECTION))
        {
            Widgets.text("Click on any Texture or Sprite Sheet in the Asset Pool");
        }
        if (this.sprite.getTexture() != null)
        {
            Vector2f[] texCoords = this.sprite.getTextureCoordinates();
            if (Widgets.imageButton(this.sprite.getTextureID(), this.sprite.getWidth() * 2, this.sprite.getHeight() * 2, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y, true))
            {
                AssetPoolDisplay.enableSelection(this::setSprite);
            }
            ImGui.setCursorPosX((ImGui.getContentRegionAvailX() * 0.5f));

            if (Widgets.button(Icons.Trash))
            {
                Sprite n = new Sprite();
                setSprite(n);
            }
            Widgets.text("");

            TextureMaximizeFilter max = Widgets.drawEnumControls(TextureMaximizeFilter.class, Icons.Box + "  Texture Maximize Filter", sprite.getMaximizeFilter());
            if (max != null) sprite.setMaximizeFilter(max);

            TextureMinimizeFilter min = Widgets.drawEnumControls(TextureMinimizeFilter.class, Icons.Box + "  Texture Minimize Filter", sprite.getMinimizeFilter());
            if (min != null) sprite.setMinimizeFilter(min);

            TextureWrapping wrap = Widgets.drawEnumControls(TextureWrapping.class, Icons.Box + "  Texture Wrap S Filter", sprite.getWrap_s());
            if (wrap != null) sprite.setWrap_sFilter(wrap);

            wrap = Widgets.drawEnumControls(TextureWrapping.class, Icons.Box + "  Texture Wrap T Filter", sprite.getWrap_t());
            if (wrap != null) sprite.setWrap_tFilter(wrap);

            if (Widgets.button("Apply Filter"))
            {
                sprite.applyTextureFilters();
            }

        }
        else
        {
            ImGui.setCursorPosX((ImGui.getContentRegionAvailX() * 0.5f));
            if (Widgets.button(Icons.PlusSquare + "  Add Texture"))
            {
                AssetPoolDisplay.enableSelection(this::setSprite);
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
        this.isChanged = true;
    }
}