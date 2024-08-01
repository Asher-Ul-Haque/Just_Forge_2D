package Just_Forge_2D.Physics;

import Just_Forge_2D.Physics.Forces.ForceRegistry;
import Just_Forge_2D.Physics.Forces.Gravity;
import Just_Forge_2D.Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class PhysicsSystem
{
    private ForceRegistry forceRegistry;
    private List<RigidBody> rigidBodies;
    private Gravity gravity;
    private float fixedUpdate;

    public PhysicsSystem(float UPDATE_DT, Vector2f GRAVITY)
    {
        forceRegistry = new ForceRegistry();
        rigidBodies = new ArrayList<>();
        fixedUpdate = UPDATE_DT;
        gravity = new Gravity(GRAVITY);
    }

    public void update(float DELTA_TIME)
    {
        fixedUpdate();
    }

    public void fixedUpdate()
    {
        forceRegistry.updateForces(fixedUpdate);

        // update the velocites of all rigid bodies
        for (RigidBody rigidBody : rigidBodies)
        {
            rigidBody.physicsUpdate(fixedUpdate);
        }
    }

    public void addRigidBody(RigidBody BODY)
    {
        this.rigidBodies.add(BODY);
        // - -- register gravity
        this.forceRegistry.add(BODY, gravity);
    }
}
