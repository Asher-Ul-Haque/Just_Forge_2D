package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.InputSystem.MouseButtons;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;

public class MouseControlComponent extends Component
{
    private float lerpSpeed = 10f;
    private Mode mode = Mode.NONE;
    static enum Mode
    {
        LEFT_CLICK,
        RIGHT_CLICK,
        MIDDLE_BUTTON,
        BUTTON_4,
        BUTTON_5,
        BUTTON_6,
        BUTTON_7,
        BUTTON_8,
        SCROLL_WHEEL,
        ANY,
        NONE
    }

    @Override
    public void update(float DELTA_TIME)
    {
        // - - -Get mouse world position
        float mouseX = Mouse.getWorldX();
        float mouseY = Mouse.getWorldY();

        // - - - Get the current position of the GameObject
        Vector2f currentPosition = this.gameObject.transform.position;

        // - - - Create a target position based on the mouse position
        Vector2f targetPosition = new Vector2f(mouseX, mouseY);

        // - - - lerp between current position and target position
        if (moveCondition())
        {
            Logger.FORGE_LOG_TRACE("Detected");
            currentPosition.lerp(targetPosition, lerpSpeed * DELTA_TIME);
        }

        // - - - Update the GameObject's position
        this.gameObject.transform.position = currentPosition;
        RigidBodyComponent rb = this.gameObject.getComponent(RigidBodyComponent.class);
        if (rb != null)
        {
            rb.setTransform(this.gameObject.transform);
        }
        Logger.FORGE_LOG_INFO(gameObject.transform.position + " Game OBject pos");
        Logger.FORGE_LOG_INFO(targetPosition + " Target Position");
    }

    protected boolean moveCondition()
    {
        switch (mode)
        {
            case LEFT_CLICK:
                return Mouse.isMouseButtonDown(MouseButtons.LEFT);

            case RIGHT_CLICK:
                return Mouse.isMouseButtonDown(MouseButtons.RIGHT);

            case MIDDLE_BUTTON:
                return Mouse.isMouseButtonDown(MouseButtons.MIDDLE);

            case BUTTON_4:
                return Mouse.isMouseButtonDown(MouseButtons.BUTTON_4);

            case BUTTON_5:
                return Mouse.isMouseButtonDown(MouseButtons.BUTTON_5);

            case BUTTON_6:
                return Mouse.isMouseButtonDown(MouseButtons.BUTTON_6);

            case BUTTON_7:
                return Mouse.isMouseButtonDown(MouseButtons.BUTTON_7);

            case BUTTON_8:
                return Mouse.isMouseButtonDown(MouseButtons.BUTTON_8);

            case SCROLL_WHEEL:
                return Mouse.getScrollY() != 0;

            case ANY:
                return Mouse.isAnyMouseButtonDown() || Mouse.getScrollY() != 0;

            default:
                mode = Mode.NONE;
                return true;
        }
    }


    @Override
    public void editorGUI()
    {
        super.deleteButton();
        lerpSpeed = Widgets.drawFloatControl(Icons.Running + "  Speed", lerpSpeed);
        mode = Widgets.drawEnumControls(Mode.class, Icons.Mouse + "  Input Mode", mode);
    }
}