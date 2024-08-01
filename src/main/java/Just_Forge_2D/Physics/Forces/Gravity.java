package Just_Forge_2D.Physics.Forces;

import Just_Forge_2D.Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

// - - - the gravity force
public class Gravity implements ForceGenerator
{
    private final Vector2f gravity;

    @Override
    public void updateForce(RigidBody BODY, float DELTA_TIME)
    {
        BODY.addForce(new Vector2f(gravity).mul(BODY.getMass()));
    }

    public Gravity(Vector2f FORCE)
    {
        this.gravity = new Vector2f(FORCE);
    }
}
