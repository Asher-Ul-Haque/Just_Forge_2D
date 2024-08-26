package Just_Forge_2D.PhysicsSystem.Primitives;

import org.joml.Vector2f;

public class AABB extends Primitive
{
    // - - - Private variables
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();


    // - - - Functions - - -

    // - - - useless constructor
    public AABB() {}

    // - - - useful constructor
    public AABB(Vector2f MIN, Vector2f MAX)
    {
        this.size = new Vector2f(MAX).sub(MIN);
        this.halfSize = new Vector2f(size).mul(0.5f);
    }

    // - - - getters
    public Vector2f getMin()
    {
        return new Vector2f(this.rigidBody.getPosition()).sub(this.halfSize);
    }

    public Vector2f getMax()
    {
        return new Vector2f(this.rigidBody.getPosition()).add(this.halfSize);
    }

    // - - - setter
    public void setSize(Vector2f SIZE)
    {
        this.size.set(SIZE);
        this.halfSize.set(SIZE.x / 2f, SIZE.y / 2f);
    }
}
