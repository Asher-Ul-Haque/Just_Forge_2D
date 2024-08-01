package Just_Forge_2D.Physics.Forces;

import Just_Forge_2D.Physics.RigidBody.RigidBody;
import Just_Forge_2D.Utils.justForgeLogger;

// - - - A class for bookeeping of forces
public class ForceRegistration
{
    // - - - private variables
    public ForceGenerator generator;
    public RigidBody body;

    // - - - constructor
    public ForceRegistration(ForceGenerator GENERATOR, RigidBody BODY)
    {
        this.generator = GENERATOR;
        this.body = BODY;
        justForgeLogger.FORGE_LOG_DEBUG("New Force Registered: " + GENERATOR.toString());
    }

    // - - - necessary
    @Override
    public boolean equals(Object OTHER)
    {
        if (OTHER == null) return false;
        if (OTHER.getClass() != ForceRegistration.class) return false;

        ForceRegistration registrator = (ForceRegistration) OTHER;
        return registrator.body == this.body && registrator.generator == this.generator;
    }
}
