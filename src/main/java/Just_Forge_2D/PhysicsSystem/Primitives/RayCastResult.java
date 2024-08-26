package Just_Forge_2D.PhysicsSystem.Primitives;

import org.joml.Vector2f;

public class RayCastResult
{
    private Vector2f point;
    private Vector2f normal;
    private float t;
    private boolean hit;

    public RayCastResult()
    {
        this.point = new Vector2f();
        this.normal = new Vector2f();
        this.t = -1;
        this.hit = false;
    }

    public void init(Vector2f POINT, Vector2f NORMAL, float T, boolean HIT)
    {
        this.point.set(POINT);
        this.normal.set(NORMAL);
        this.t = T;
        this.hit = HIT;
    }

    public static void reset(RayCastResult RESULT)
    {
        if (RESULT != null)
        {
            RESULT.point.zero();
            RESULT.normal.set(0, 0);
            RESULT.t = -1;
            RESULT.hit = false;

        }
    }

}
