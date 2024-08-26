package Just_Forge_2D.PhysicsSystem.Primitives;

import Just_Forge_2D.PhysicsSystem.RigidBody.RigidBody;
import Just_Forge_2D.Utils.ForgeMath;
import org.joml.Vector2f;

public class Box extends Primitive
{
    // - - - primitive variables
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();


    // - - - Functions - - -

    // - - - useless constructor
    public Box() {}

    // - - - useful constructor
    public Box(Vector2f MIN, Vector2f MAX)
    {
        this.size = new Vector2f(MAX).sub(MIN);
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    // - - - Getters
    public Vector2f getLocalMin()
    {
        return new Vector2f(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getLocalMax()
    {
        return new Vector2f(this.rigidBody.getPosition()).add(this.halfSize);
    }

    public Vector2f[] getVertices()
    {
        Vector2f min = getLocalMin();
        Vector2f max = getLocalMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y), new Vector2f(max.x, max.y)
        };

        if (rigidBody.getRotation() != 0.0f)
        {
            for (Vector2f vert : vertices)
            {
                // Rotates point(Vector2f about center) by rtoation(float in degrees)
                ForgeMath.rotate(vert, this.rigidBody.getRotation(), this.rigidBody.getPosition());
            }
        }

        return vertices;
    }

    public Vector2f getHalfSize()
    {
        return this.halfSize;
    }

    public RigidBody getRigidBody()
    {
        return this.rigidBody;
    }
}
