package Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.NonPickableComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Editor.Prefabs;
import org.joml.Vector2f;
import org.joml.Vector4f;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

// - - - Gizmo
public class GizmoComponent extends Component
{
    // - - - Private Variables - - -

    // - - - xAxis Gizmo
    private final Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private final Vector4f xAxisHoverColor = new Vector4f(1, 0, 0, 1);
    private final Vector2f xAxisOffset = new Vector2f(64, -5);
    private final SpriteComponent xAxisSprite;
    protected boolean xAxisActive = false;
    private final GameObject xAxisGizmo;

    // - - - yAxis Gizmo
    private final Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private final Vector4f yAxisHoverColor = new Vector4f(0, 1, 0, 1);
    private final Vector2f yAxisOffset = new Vector2f(16, 61);
    private final SpriteComponent yAxisSprite;
    protected boolean yAxisActive = false;
    private final GameObject yAxisGizmo;

    // - - - Size
    private final int gizmoWidth = 16;
    private final int gizmoHeight = 48;

    // - - - state
    private boolean using = false;
    protected GameObject activeGameObject = null;


    // - - - | Functions | - - -


    // - - - Constructor - - -

    public GizmoComponent(Sprite ARROW)
    {
        this.xAxisGizmo = Prefabs.generateSpriteObject("X axis Gizmo", ARROW, 16, 48);
        this.yAxisGizmo = Prefabs.generateSpriteObject("Y axis Gizmo", ARROW, 16, 48);
        this.xAxisSprite = this.xAxisGizmo.getCompoent(SpriteComponent.class);
        this.yAxisSprite = this.yAxisGizmo.getCompoent(SpriteComponent.class);
        this.xAxisGizmo.transform.position.add(this.xAxisOffset);
        this.yAxisGizmo.transform.position.add(this.yAxisOffset);
        this.xAxisGizmo.addComponent(new NonPickableComponent());
        this.yAxisGizmo.addComponent(new NonPickableComponent());

        Window.getCurrentScene().addGameObject(this.xAxisGizmo);
        Window.getCurrentScene().addGameObject(this.yAxisGizmo);
    }


    // - - - Usage - - -

    @Override
    public void update(float DELTA_TIME)
    {
        if (!using) return;

        this.activeGameObject = Window.getEditor().getPropertiesWindow().getActiveGameObject(); //this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null)
        {
            this.activate();
        }
        else
        {
            this.inactivate();
            return;
        }

        boolean xAxisHot = checkXHoverState();
        boolean yAxisHot = checkYHoverState();

        if (xAxisHot && Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            xAxisActive = true;
            yAxisActive = false;
        }
        else if (yAxisHot && Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
        {
            xAxisActive = false;
            yAxisActive = true;
        }
        else
        {
            xAxisActive = false;
            yAxisActive = false;
        }

        if (this.activeGameObject != null)
        {
            this.yAxisGizmo.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisGizmo.transform.position.set(this.activeGameObject.transform.position);
            this.xAxisGizmo.transform.position.add(this.xAxisOffset);
            this.yAxisGizmo.transform.position.add(this.yAxisOffset);
        }
    }

    @Override
    public void start()
    {
        this.xAxisGizmo.transform.rotation = (float) (Math.PI / 2);
        this.yAxisGizmo.transform.rotation = (float) (Math.PI);
        this.xAxisGizmo.transform.layer = 2;
        this.yAxisGizmo.transform.layer = 2;
        this.xAxisGizmo.noSerialize();
        this.yAxisGizmo.noSerialize();
    }


    // - - - Activate or Inactivate - - -

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


    // - - - Hover - - -

    private boolean checkXHoverState()
    {
        Vector2f mousePos = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());

        if (mousePos.x <= xAxisGizmo.transform.position.x &&
                mousePos.x >= xAxisGizmo.transform.position.x - gizmoHeight &&
                mousePos.y >= xAxisGizmo.transform.position.y &&
                mousePos.y <= xAxisGizmo.transform.position.y + gizmoWidth)
        {
            xAxisSprite.setColor(xAxisHoverColor);
            return true;
        }

        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState()
    {
        Vector2f mousePos = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());

        if (mousePos.x <= yAxisGizmo.transform.position.x &&
                mousePos.x >= yAxisGizmo.transform.position.x - gizmoWidth &&
                mousePos.y <= yAxisGizmo.transform.position.y &&
                mousePos.y >= yAxisGizmo.transform.position.y - gizmoHeight)
        {
            yAxisSprite.setColor(yAxisHoverColor);
            return true;
        }

        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    // - - - usage
    public void setUsing(boolean REALLY)
    {
        this.using = REALLY;
        if (!REALLY) this.inactivate();
    }
}
