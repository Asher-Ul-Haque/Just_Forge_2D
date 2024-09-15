package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystemComponent extends Component implements Observer
{
    private transient List<Particle> particles = new ArrayList<>();
    private int maxParticles = 36;
    private Vector2f minSize = new Vector2f(0.1f);
    private Vector2f maxSize = new Vector2f(0.2f);
    private static Random randomizer;
    private Vector4f startColor = new Vector4f(1.0f, 0.0f, 1.0f, 1.0f);
    private Vector4f finalColor = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
    private float lifespan = 10;
    private float fanStart = 0;
    private float fanEnd = (float) (2 * Math.PI);
    private float angularVelocity = 0.2f;
    private float minSpeed = 0.01f;
    private float maxSpeed = 0.05f;
    private transient ParticleGenerator generator;

    private Vector2f generateVelocity()
    {
        float randomAngle = randomizer.nextFloat(fanStart, fanEnd);
        return new Vector2f((float) Math.cos(randomAngle), (float) Math.sin(randomAngle)).normalize().mul(randomizer.nextFloat(minSpeed, maxSpeed + 0.0001f));
    }

    public Vector2f generateSize()
    {
        return new Vector2f(randomizer.nextFloat(minSize.x, maxSize.x + 0.001f), randomizer.nextFloat(minSize.y, maxSize.y + 0.001f));
    }

    public ParticleSystemComponent()
    {
        EventManager.addObserver(this);
    }

    public ParticleSystemComponent(ParticleGenerator GENERATOR)
    {
        EventManager.addObserver(this);
        setGenerator(GENERATOR);
    }

    public void setGenerator(ParticleGenerator TEMPLATE)
    {
        this.generator = TEMPLATE;
    }

    private void resetParticle(Particle PARTICLE)
    {
        PARTICLE.lifeTime = lifespan;
        PARTICLE.core.transform.position = new Vector2f(this.gameObject.transform.position);
        SpriteComponent renderable = PARTICLE.core.getCompoent(SpriteComponent.class);
        if (renderable != null) renderable.setColor(new Vector4f(startColor));
        PARTICLE.lifeSpan = PARTICLE.lifeTime;
        PARTICLE.angularVelocity = angularVelocity;
        PARTICLE.core.transform.scale = generateSize();
        PARTICLE.velocity = generateVelocity();
        PARTICLE.core.transform.rotation = randomizer.nextFloat();
    }


    @Override
    public void start()
    {
        generator = new ParticleGenerator(this.gameObject);
        particles = new ArrayList<>(maxParticles);

        randomizer = new Random(this.gameObject.getUniqueID());
        for (int i = 0; i < maxParticles; ++i)
        {
            Particle particle = this.generator.create();
            resetParticle(particle);
            particles.add(particle);
            MainWindow.getCurrentScene().addGameObject(particle.core);
        }
        Logger.FORGE_LOG_DEBUG("Starting");
    }

    @Override
    public void update(float DELTA_TIME)
    {
        for (Particle particle : this.particles)
        {
            GameObject core = particle.core;
            if (particle.lifeSpan < 0)
            {
                core.destroy();
            }
            else
            {
                core.transform.rotation += particle.angularVelocity;
                core.transform.position.add(particle.velocity);
                SpriteComponent renderable = core.getCompoent(SpriteComponent.class);
                if (renderable != null) renderable.setColor(new Vector4f(startColor).lerp(finalColor, 1.0f - (particle.lifeSpan / particle.lifeTime)));
                particle.lifeSpan -= DELTA_TIME;
            }
        }
    }

    @Override
    public void onNotify(GameObject OBJECT, Event EVENT)
    {
        switch (EVENT.type)
        {
            case ForgeStop:
                for (Particle particle : particles)
                {
                    Logger.FORGE_LOG_DEBUG("Destroying particle : " + particle.core.name);
                    particle.core.destroy();
                }
                break;
        }
    }
}
