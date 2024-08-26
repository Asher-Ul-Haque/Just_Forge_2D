package Just_Forge_2D.PhysicsSystem.Primitives;

import Just_Forge_2D.PhysicsSystem.RigidBody.RigidBody;

// - - - Abstract Class for all primitives
public class Primitive
{
    protected RigidBody rigidBody = null;

    public void setRigidBody(RigidBody BODY)
    {
        this.rigidBody = BODY;
    }

    public RigidBody getRigidBody()
    {
        return this.rigidBody;
    }
}