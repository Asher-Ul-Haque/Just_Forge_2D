package Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.NonPickableComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Editor.Prefabs;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

// - - - Gizmo
public class GizmoComponent extends Component
{
    // - - - Private Variables - - -

    // - - - xAxis Gizmo
    private final Vector4f xAxisColor = new Vector4f(1, 0.3f, 0.3f, 1);
    private final Vector4f xAxisHoverColor = new Vector4f(1, 0, 0, 1);
    private final Vector2f xAxisOffset = new Vector2f(24f / 80f, -6f / 80f);
    private final SpriteComponent xAxisSprite;
    protected boolean xAxisActive = false;
    private final GameObject xAxisGizmo;

    // - - - yAxis Gizmo
    private final Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private final Vector4f yAxisHoverColor = new Vector4f(0, 1, 0, 1);
    private final Vector2f yAxisOffset = new Vector2f(-7f / 80f, 21f / 80f);
    private final SpriteComponent yAxisSprite;
    protected boolean yAxisActive = false;
    private final GameObject yAxisGizmo;

    // - - - Size
    private final float gizmoWidth = 16f / 80f;
    private final float gizmoHeight = 48f / 80f;

    // - - - state
    private boolean using = false;
    protected GameObject activeGameObject = null;


    // - - - | Functions | - - -


    // - - - Constructor - - -

    public GizmoComponent(Sprite ARROW)
    {
        this.xAxisGizmo = Prefabs.generateSpriteObject("X axis Gizmo", ARROW, gizmoWidth, gizmoHeight);
        this.yAxisGizmo = Prefabs.generateSpriteObject("Y axis Gizmo", ARROW, gizmoWidth, gizmoHeight);
        this.xAxisSprite = this.xAxisGizmo.getCompoent(SpriteComponent.class);
        this.yAxisSprite = this.yAxisGizmo.getCompoent(SpriteComponent.class);
        this.xAxisGizmo.transform.position.add(this.xAxisOffset);
        this.yAxisGizmo.transform.position.add(this.yAxisOffset);
        this.xAxisGizmo.addComponent(new NonPickableComponent());
        this.yAxisGizmo.addComponent(new NonPickableComponent());

        ForgeDynamo.getCurrentScene().addGameObject(this.xAxisGizmo);
        ForgeDynamo.getCurrentScene().addGameObject(this.yAxisGizmo);
    }


    // - - - Usage - - -

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (!using) return;

        // TODO: refactor
        this.activeGameObject = ForgeDynamo.getEditor().getPropertiesWindow().getActiveGameObject(); //this.propertiesWindow.getActiveGameObject();
        if (this.activeGameObject != null)
        {
            this.activate();
            if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && Keyboard.isKeyBeginPress(GLFW_KEY_C))
            {
                GameObject newObj = this.activeGameObject.copy();
                ForgeDynamo.getCurrentScene().addGameObject(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                ForgeDynamo.getEditor().getPropertiesWindow().setActiveGameObject(newObj);
                return;
            }
            else if (Keyboard.isKeyPressed(GLFW_KEY_DELETE))
            {
                activeGameObject.destroy();
                this.inactivate();
                ForgeDynamo.getEditor().getPropertiesWindow().setActiveGameObject(null);
            }
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

    @Override
    public void update(float DELTA_TIME)
    {
        if (this.using) inactivate();
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

        if (mousePos.x <= xAxisGizmo.transform.position.x + (gizmoHeight / 2.0f) &&
                mousePos.x >= xAxisGizmo.transform.position.x - (gizmoWidth / 2f) &&
                mousePos.y >= xAxisGizmo.transform.position.y - (gizmoHeight /2f) &&
                mousePos.y <= xAxisGizmo.transform.position.y + (gizmoWidth / 2f))
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

        if (mousePos.x <= yAxisGizmo.transform.position.x + (gizmoWidth / 2f)&&
                mousePos.x >= yAxisGizmo.transform.position.x - (gizmoWidth / 2f)&&
                mousePos.y <= yAxisGizmo.transform.position.y + (gizmoHeight / 2.0f)&&
                mousePos.y >= yAxisGizmo.transform.position.y - (gizmoHeight / 2f))
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
