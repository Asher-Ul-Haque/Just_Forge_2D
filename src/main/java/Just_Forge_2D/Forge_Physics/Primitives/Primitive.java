package Just_Forge_2D.Forge_Physics.Primitives;

import Just_Forge_2D.Forge_Physics.RigidBody.RigidBody;

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
