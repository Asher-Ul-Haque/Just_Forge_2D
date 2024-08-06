package Just_Forge_2D.Physics.Primitives;

import Just_Forge_2D.Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

public class Circle extends Primitive
{
    private float radius = 1.0f;

    public float getRadius()
    {
        return this.radius;
    }

    public Vector2f getCenter()
    {
        return this.rigidBody.getPosition();
    }

    public void setRadius(float RADIUS)
    {
        this.radius = RADIUS;
    }
}
