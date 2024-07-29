package Just_Forge_2D.Forge_Physics.RigidBody;

import Just_Forge_2D.Core.ECS.Components.Component;
import org.joml.Vector2f;

public class RigidBody2D extends Component
{
    private Vector2f position = new Vector2f();

    public float getRotation()
    {
        return Rotation;
    }

    public void setRotation(float rotation)
    {
        Rotation = rotation;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    private float Rotation = 0.0f; // degrees
}
