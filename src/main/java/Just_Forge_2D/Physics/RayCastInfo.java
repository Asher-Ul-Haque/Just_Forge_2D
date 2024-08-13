package Just_Forge_2D.Physics;

import Just_Forge_2D.Core.EntityComponentSystem.GameObject;
import org.jbox2d.callbacks.RayCastCallback;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Fixture;
import org.joml.Vector2f;

public class RayCastInfo implements RayCastCallback
{
    public Fixture fixture;
    public Vector2f point;
    public Vector2f normal;
    public float fraction;
    public boolean hit;
    private GameObject requestee;
    public GameObject hitObject;

    public RayCastInfo(GameObject OBJ)
    {
        fixture = null;
        point = new Vector2f();
        normal = new Vector2f();
    }

    @Override
    public float reportFixture(Fixture FIXTURE, Vec2 POINT_1, Vec2 POINT_2, float v)
    {
        return 0;
    }
}
