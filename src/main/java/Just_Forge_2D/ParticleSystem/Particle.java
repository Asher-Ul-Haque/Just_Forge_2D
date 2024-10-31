package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class Particle
{
    protected GameObject core;
    protected int ID;
    protected float lifeTime;
    protected float lifeSpan;
    protected Vector2f velocity;
    protected float angularVelocity;

    protected Particle(GameObject CORE)
    {
        this.core = CORE;
        this.core.noSerialize();
    }

    protected void wake(boolean REALLY)
    {
        SpriteComponent sprite = this.core.getComponent(SpriteComponent.class);
        if (sprite != null) sprite.setShowAtRuntime(REALLY);
    }

    protected void wake(Vector4f COLOR)
    {
        SpriteComponent sprite = this.core.getComponent(SpriteComponent.class);
        if (sprite == null) return;
        sprite.setShowAtRuntime(true);
        sprite.setColor(COLOR);
    }
}
