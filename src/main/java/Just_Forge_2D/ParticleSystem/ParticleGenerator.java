package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.Components.CameraControlComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.*;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.KeyboardControllerComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.jbox2d.particle.ParticleDef;
import org.jbox2d.particle.ParticleSystem;

public class ParticleGenerator
{
    private GameObject template;
    private ParticleSystem particleSystem;
    private final int index = 0;

    public ParticleGenerator(GameObject TEMPLATE, boolean KEEP_PHYSICS, ParticleSystem SYSTEM)
    {
        this.particleSystem = SYSTEM;
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

    public Particle create(ParticleDef DEF)
    {
        int particleID = GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createParticle(DEF);
        return new Particle(this.template.copy(), particleID);
    }
}
