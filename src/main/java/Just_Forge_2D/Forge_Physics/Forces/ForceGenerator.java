package Just_Forge_2D.Forge_Physics.Forces;

import Just_Forge_2D.Forge_Physics.RigidBody.RigidBody;

public interface ForceGenerator
{
    void updateForce(RigidBody BODY, float DELTA_TIME);
}
