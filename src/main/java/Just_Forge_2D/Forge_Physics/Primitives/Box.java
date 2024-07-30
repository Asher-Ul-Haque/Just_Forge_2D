package Just_Forge_2D.Forge_Physics.Primitives;

import Just_Forge_2D.Forge_Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

public class Box
{
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private RigidBody rigidBody = null;

    public Box()
    {

    }

    public Box(Vector2f MIN, Vector2f MAX)
    {
        this.size = new Vector2f(MAX).sub(MIN);
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    public Vector2f getMin()
    {
        return new Vector2f(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax()
    {
        return new Vector2f(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public Vector2f[] getVertices()
    {
        Vector2f min = getMin();
        Vector2f max = getMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y), new Vector2f(max.x, max.y)
        };

        if (rigidBody.getRotation() != 0.0f)
        {
            for (Vector2f vert : vertices)
            {
                // TODO: Implement
                // Rotates point(Vector2f about center) by rtoation(float in degrees)
                //ForgeMath.rotate(vert, this.rigidBody.getPosition(), this.rigidBody.getRotation());
            }
        }

        return vertices;
    }

    public RigidBody getRigidBody()
    {
        return this.rigidBody;
    }
}
