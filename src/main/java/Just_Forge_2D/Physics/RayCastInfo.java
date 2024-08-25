package Just_Forge_2D.Physics;

import Just_Forge_2D.EntityComponentSystem.GameObject;
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
        fraction = 0f;
        hit = false;
        hitObject = null;
        this.requestee = OBJ;
    }


    @Override
    public float reportFixture(Fixture FIXTURE, Vec2 POINT, Vec2 NORMAL, float FRACTION)
    {
        if (FIXTURE.m_userData == requestee)
        {
            return 1;
        }
        this.fixture = FIXTURE;
        this.point = new Vector2f(POINT.x, POINT.y);
        this.normal = new Vector2f(NORMAL.x, NORMAL.y);
        this.fraction = FRACTION;
        this.hit = fraction != 0;
        this.hitObject = (GameObject) fixture.m_userData;

        return fraction;
    }
}
