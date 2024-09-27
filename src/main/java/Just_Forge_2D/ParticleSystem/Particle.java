package ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import org.joml.Vector2f;

public class Particle
{
    protected GameObject core;
    protected float lifeTime;
    protected float lifeSpan;
    protected Vector2f velocity;
    protected float angularVelocity;

    protected Particle(GameObject CORE)
    {
        this.core = CORE;
        this.core.noSerialize();
    }
}
