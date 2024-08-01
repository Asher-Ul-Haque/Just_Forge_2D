package Just_Forge_2D.Physics.Forces;

import Just_Forge_2D.Physics.RigidBody.RigidBody;

import java.util.ArrayList;
import java.util.List;


// - - - What did you think would be bookept
public class ForceRegistry
{
    // - - - private variable
    private List<ForceRegistration> registry;


    // - - - | Functions | - - -


    // - - - constructor
    public ForceRegistry()
    {
        this.registry = new ArrayList<>();
    }


    // - - - bookeeping - - -

    // - - - add a force to an body
    public void add(RigidBody BODY, ForceGenerator GENERATOR)
    {
        ForceRegistration generator = new ForceRegistration(GENERATOR, BODY);
        registry.add(generator);
    }

    // - - - remove a force remove a force
    public void remove(RigidBody BODY, ForceGenerator GENERATOR)
    {
        ForceRegistration newRegistration = new ForceRegistration(GENERATOR, BODY);
        registry.remove(GENERATOR);
    }

    // - - - clear the book
    public void clear()
    {
        registry.clear();
    }


    // - - - Deal with forces - - -

    public void updateForces(float DELTA_TIME)
    {
        for (ForceRegistration fr : registry)
        {
            fr.generator.updateForce(fr.body, DELTA_TIME);
        }
    }

    public void zeroForces()
    {
        for (ForceRegistration fr : registry)
        {
            // FIXME: implement me
            //fr.body.zeroForces();
        }
    }
}
