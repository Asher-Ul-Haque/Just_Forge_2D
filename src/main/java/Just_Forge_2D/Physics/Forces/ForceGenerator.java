package Just_Forge_2D.Physics.Forces;

import Just_Forge_2D.Physics.RigidBody.RigidBody;

public interface ForceGenerator
{
    void updateForce(RigidBody BODY, float DELTA_TIME);
}
