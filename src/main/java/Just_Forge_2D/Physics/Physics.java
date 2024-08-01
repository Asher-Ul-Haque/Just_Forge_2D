package Just_Forge_2D.Physics;

import Just_Forge_2D.Physics.Forces.ForceRegistry;
import Just_Forge_2D.Physics.Forces.Gravity;
import Just_Forge_2D.Physics.RigidBody.RigidBody;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

// - - - Physics Manager
public class Physics
{
    // - - - Private Variables - - -

    private final ForceRegistry forceRegistry;
    private final List<RigidBody> rigidBodies;
    private final Gravity gravity;
    private final float fixedDeltaTime;


    // - - - Functions - - -

    // - - - constructor
    public Physics(float UPDATE_DT, Vector2f GRAVITY)
    {
        forceRegistry = new ForceRegistry();
        rigidBodies = new ArrayList<>();
        fixedDeltaTime = UPDATE_DT;
        gravity = new Gravity(GRAVITY);
    }


    // - - - Updating - - -

    // - - - update with regard to DELTA_TIME
    public void update(float DELTA_TIME)
    {
        fixedUpdate();
    }

    // - - - update without caring about DELTA_TIME
    public void fixedUpdate()
    {
        forceRegistry.updateForces(fixedDeltaTime);

        // - - - update the velocities of all rigid bodies
        for (RigidBody rigidBody : rigidBodies)
        {
            rigidBody.physicsUpdate(fixedDeltaTime);
        }

        // - - - update angular velocity of all rigid bodies
    }

    // - - - add a rigid body to the physics simulation
    public void addRigidBody(RigidBody BODY)
    {
        this.rigidBodies.add(BODY);
        // - - - register gravity
        this.forceRegistry.add(BODY, gravity);
    }
}
