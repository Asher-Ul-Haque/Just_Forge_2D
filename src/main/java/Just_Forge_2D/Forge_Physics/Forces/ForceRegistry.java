package Just_Forge_2D.Forge_Physics.Forces;

import Just_Forge_2D.Forge_Physics.RigidBody.RigidBody;

import java.util.ArrayList;
import java.util.List;

public class ForceRegistry
{
    private List<ForceRegistrator> registry;

    public ForceRegistry()
    {
        this.registry = new ArrayList<>();
    }

    public void add(RigidBody BODY, ForceGenerator GENERATOR)
    {
        ForceRegistrator generator = new ForceRegistrator(GENERATOR, BODY);
        registry.add(generator);
    }

    public void remove(RigidBody BODY, ForceGenerator GENERATOR)
    {
        ForceRegistrator newRegistration = new ForceRegistrator(GENERATOR, BODY);
        registry.remove(GENERATOR);
    }

    public void clear()
    {
        registry.clear();
    }

    public void updateForces(float DELTA_TIME)
    {
        for (ForceRegistrator fr : registry)
        {
            fr.generator.updateForce(fr.body, DELTA_TIME);
        }
    }

    public void zeroForces()
    {
        for (ForceRegistrator fr : registry)
        {
            // FIXME: implement me
            //fr.body.zeroForces();
        }
    }
}
