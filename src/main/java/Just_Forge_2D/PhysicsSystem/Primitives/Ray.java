package Just_Forge_2D.PhysicsSystem.Primitives;

import org.joml.Vector2f;

public class Ray
{
    private Vector2f origin;
    private Vector2f direction;

    public Ray(Vector2f ORIGIN, Vector2f DIRECTION)
    {
        this.origin = ORIGIN;
        this.direction = DIRECTION;
        this.direction.normalize();
    }

    public Vector2f getOrigin()
    {
        return this.origin;
    }

    public Vector2f getDirection()
    {
        return this.direction;
    }
}
