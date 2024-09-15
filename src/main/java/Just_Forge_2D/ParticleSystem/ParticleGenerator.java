package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.Components.CameraControlComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.SimpleCharacterController;
import Just_Forge_2D.Utils.Logger;

public class ParticleGenerator
{
    private final GameObject template;
    private final int index = 0;

    public ParticleGenerator(GameObject TEMPLATE)
    {
        GameObject template = TEMPLATE.copy();
        template.name = TEMPLATE.name + " particle: " + index;
        template.noSerialize();
        template.removeComponent(ParticleSystemComponent.class);
        template.removeComponent(CameraControlComponent.class);
        template.removeComponent(SimpleCharacterController.class);
        this.template = template;
        Logger.FORGE_LOG_TRACE("Template created");
    }

    public Particle create()
    {
        return new Particle(this.template.copy());
    }
}
