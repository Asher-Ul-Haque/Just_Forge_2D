package Just_Forge_2D.Forge_Physics.Primitives;

import Just_Forge_2D.Forge_Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

public class Circle
{
    private float radius = 1.0f;
    private RigidBody body = null;

    public float getRadius()
    {
        return this.radius;
    }

    public Vector2f getCenter()
    {
        return body.getPosition();
    }

    public void setRadius(float RADIUS)
    {
        this.radius = RADIUS;
    }
}
