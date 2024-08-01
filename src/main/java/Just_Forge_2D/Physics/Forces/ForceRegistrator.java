package Just_Forge_2D.Physics.Forces;

import Just_Forge_2D.Physics.RigidBody.RigidBody;

public class ForceRegistrator
{
    public ForceGenerator generator;
    public RigidBody body;

    public ForceRegistrator(ForceGenerator GENERATOR, RigidBody BODY)
    {
        this.generator = GENERATOR;
        this.body = BODY;
    }

    @Override
    public boolean equals(Object OTHER)
    {
        if (OTHER == null) return false;
        if (OTHER.getClass() != ForceRegistrator.class) return false;

        ForceRegistrator registrator = (ForceRegistrator) OTHER;
        return registrator.body == this.body && registrator.generator == this.generator;
    }
}
