package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.justForgeLogger;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class TranslationGizmo extends Component
{
    private Vector4f xAxisColor = new Vector4f(1, 0, 0, 1);
    private Vector4f xAxisHoverColor = new Vector4f();
    private Vector4f yAxisColor = new Vector4f(0, 1, 0, 1);
    private Vector4f yAxisHoverColor = new Vector4f();
    private Vector2f xAxisOffset = new Vector2f(64, -5);
    private Vector2f yAxisOffset = new Vector2f(16, 61);

    private GameObject xAxisGizmo;
    private GameObject yAxisGizmo;
    private SpriteComponent xAxisSprite;
    private SpriteComponent yAxisSprite;

    private GameObject activeGameObject = null;

    public TranslationGizmo(Sprite ARROW)
    {
        this.xAxisGizmo = Prefabs.generateSpriteObject(ARROW, 16, 48);
        this.yAxisGizmo = Prefabs.generateSpriteObject(ARROW, 16, 48);
        this.xAxisSprite = this.xAxisGizmo.getCompoent(SpriteComponent.class);
        this.yAxisSprite = this.yAxisGizmo.getCompoent(SpriteComponent.class);
        this.xAxisGizmo.transform.position.add(this.xAxisOffset);
        this.yAxisGizmo.transform.position.add(this.yAxisOffset);

        Window.getCurrentScene().addGameObject(this.xAxisGizmo);
        Window.getCurrentScene().addGameObject(this.yAxisGizmo);
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (this.activeGameObject != null)
        {
            this.yAxisGizmo.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisGizmo.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisGizmo.transform.position.add(this.xAxisOffset);
            this.yAxisGizmo.transform.position.add(this.yAxisOffset);
        }
        this.activeGameObject = Window.getEditor().getPropertiesWindow().getActiveGameObject(); //this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null)
        {
            this.activate();
        }
        else
        {
            this.inactivate();
        }
    }

    @Override
    public void start()
    {
        this.xAxisGizmo.transform.rotation = (float) (Math.PI / 2);
        this.yAxisGizmo.transform.rotation = (float) (Math.PI);
        this.xAxisGizmo.noSerialize();
        this.yAxisGizmo.noSerialize();
    }

    private void activate()
    {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void inactivate()
    {
        this.activeGameObject = null;
        this.xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        this.yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }
}
