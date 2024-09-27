package ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.Components.CameraControlComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import PhysicsSystem.PhysicsComponents.Collider.*;
import PhysicsSystem.PhysicsComponents.KeyboardControllerComponent;
import PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Utils.Logger;

public class ParticleGenerator
{
    private GameObject template;
    private final int index = 0;

    public ParticleGenerator(GameObject TEMPLATE, boolean KEEP_PHYSICS)
    {
        GameObject template = TEMPLATE.copy();
        template.name = TEMPLATE.name + " particle: " + index;
        template.noSerialize();
        template.removeComponent(CameraControlComponent.class);
        template.removeComponent(ParticleSystemComponent.class);
        template.removeComponent(KeyboardControllerComponent.class);
        if (!KEEP_PHYSICS)
        {
            template.removeComponent(RigidBodyComponent.class);
            template.removeComponent(BoxColliderComponent.class);
            template.removeComponent(CircleColliderComponent.class);
            template.removeComponent(PolygonColliderComponent.class);
            template.removeComponent(EdgeColliderComponent.class);
            template.removeComponent(CylinderColliderComponent.class);
        }
        this.template = template;
        Logger.FORGE_LOG_TRACE("Template created");
    }

    public Particle create()
    {
        return new Particle(this.template.copy());
    }

}
