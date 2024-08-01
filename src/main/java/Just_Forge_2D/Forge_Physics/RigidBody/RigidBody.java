package Just_Forge_2D.Forge_Physics.RigidBody;

import Just_Forge_2D.Core.ECS.Components.Component;
import org.joml.Vector2f;

public class RigidBody extends Component
{
    private Vector2f position = new Vector2f();
    private float rotation = 0.0f; // degrees
    private Vector2f velocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float friction = 0.0f;
    private float angularFriction = 0.0f;
    private boolean fixedRotation;

    public float getRotation()
    {
        return rotation;
    }

    public void setTransform(Vector2f POSITION, float ROTATION)
    {
        this.position.set(POSITION);
        this.rotation = ROTATION;
    }

    public void setTransform(Vector2f POSITION)
    {
        this.position.set(POSITION);
    }

    public Vector2f getPosition()
    {
        return position;
    }
}
