package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.InputSystem.MouseButtons;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import org.joml.Vector2f;

public class MouseControlComponent extends Component
{
    private float lerpSpeed = 0.1f;  // Speed of interpolation, can be adjusted
    MouseButtons controlButton = MouseButtons.LEFT;

    @Override
    public void update(float DELTA_TIME)
    {
        // Get mouse world position
        float mouseX = Mouse.getWorldX();
        float mouseY = Mouse.getWorldY();

        // Get the current position of the GameObject
        Vector2f currentPosition = this.gameObject.transform.position;

        // Create a target position based on the mouse position
        Vector2f targetPosition = new Vector2f(mouseX, mouseY);

        // Interpolate (lerp) between current position and target position
        if (Mouse.isMouseButtonDown(controlButton))
        {
            currentPosition.lerp(targetPosition, lerpSpeed * DELTA_TIME);
        }

        // Update the GameObject's position
        this.gameObject.transform.position = currentPosition;
        RigidBodyComponent rb = this.gameObject.getComponent(RigidBodyComponent.class);
        if (rb != null)
        {
            rb.setTransform(this.gameObject.transform);
        }
    }
}