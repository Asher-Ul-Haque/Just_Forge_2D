package Just_Forge_2D.Physics.Primitives;

import Just_Forge_2D.Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

public class AABB
{
    private Vector2f size = new Vector2f();
    private Vector2f halfSize = new Vector2f();
    private RigidBody rigidBody = null;

    public AABB()
    {

    }

    public AABB(Vector2f MIN, Vector2f MAX)
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

    public void setSize(Vector2f SIZE)
    {
        this.size.set(SIZE);
        this.halfSize.set(SIZE.x / 2f, SIZE.y / 2f);
    }
}
