package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.RenderingSystem.Sprite;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystemComponent extends Component implements Observer
{
    protected transient List<Particle> particles = new ArrayList<>();
    protected int maxParticles = 36;
    protected final Vector2f minSize = new Vector2f(0.1f);
    protected final Vector2f maxSize = new Vector2f(0.2f);
    protected static Random randomizer;
    protected final Vector4f startColor = new Vector4f(1.0f, 0.0f, 1.0f, 1.0f);
    protected final Vector4f finalColor = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
    protected float minLifespan = 0.05f;
    protected float maxLifespan = 0.2f;
    protected float fanStart = 0;
    protected float fanEnd = (float) (2 * Math.PI);
    protected float angularVelocity = 0.2f;
    protected float minSpeed = 0.1f;
    protected float maxSpeed = 0.5f;
    protected boolean debugDrawAtRuntime = false;
    protected transient ParticleGenerator generator;
    protected Vector4f debugColor = new Vector4f(1.0f).sub(GameWindow.get().getClearColor());
    protected boolean useOnce = false;
    protected Vector2f offset = new Vector2f();
    protected int particleLayer;
    protected boolean resetAll;

    protected Vector2f generateVelocity()
    {
        float randomAngle = randomizer.nextFloat(fanStart, fanEnd + 0.0001f);
        return new Vector2f((float) Math.cos(randomAngle), (float) Math.sin(randomAngle)).normalize().mul(randomizer.nextFloat(minSpeed, maxSpeed + 0.1f));
    }

    public Vector2f generateSize()
    {
        return new Vector2f(randomizer.nextFloat(minSize.x, maxSize.x + 0.001f), randomizer.nextFloat(minSize.y, maxSize.y + 0.1f));
    }

    public ParticleSystemComponent()
    {
        EventManager.addObserver(this);
        particles = new ArrayList<>(maxParticles);
    }

    public ParticleSystemComponent(ParticleGenerator GENERATOR)
    {
        EventManager.addObserver(this);
        setGenerator(GENERATOR);
        particles = new ArrayList<>(maxParticles);
    }

    @Override
    public void debugDraw()
    {
        Vector2f center = this.gameObject.transform.position;
        Vector3f debuColor = new Vector3f(debugColor.x, debugColor.y, debugColor.z);
        DebugPencil.addCircle(center, minSpeed, debuColor);
        DebugPencil.addCircle(center, maxSpeed, debuColor);
        Vector2f lineEnd = new Vector2f((float) (center.x + maxSpeed * Math.cos(fanStart)), (float) (center.y + maxSpeed * Math.sin(fanStart)));
        DebugPencil.addLine(center, lineEnd, new Vector3f(1.0f, 0.0f, 1.0f));
        lineEnd = new Vector2f((float) (center.x + maxSpeed * Math.cos(fanEnd)), (float) (center.y + maxSpeed * Math.sin(fanEnd)));
        DebugPencil.addLine(center, lineEnd, new Vector3f(0.0f, 1.0f, 1.0f));
    }

    public void setGenerator(ParticleGenerator TEMPLATE)
    {
        this.generator = TEMPLATE;
    }

    protected void resetParticle(Particle PARTICLE)
    {
        PARTICLE.lifeTime = randomizer.nextFloat(minLifespan, maxLifespan + 0.001f);
        PARTICLE.core.transform.position = new Vector2f(this.gameObject.transform.position).add(offset);
        PARTICLE.lifeSpan = PARTICLE.lifeTime;
        PARTICLE.angularVelocity = angularVelocity;
        PARTICLE.core.transform.scale = generateSize();
        PARTICLE.velocity = generateVelocity();
        PARTICLE.core.transform.rotation = randomizer.nextFloat();
        PARTICLE.core.transform.layer = particleLayer;
        PARTICLE.wake(startColor);
    }

    @Override
    public void start()
    {
        SpriteComponent spr = this.gameObject.getComponent(SpriteComponent.class);
        if (spr == null)
        {
            Logger.FORGE_LOG_WARNING("No Sprite Component for Particle Component");
            return;
        }
        Sprite sprite = spr.getSpriteCopy();
        if (sprite == null)
        {
            Logger.FORGE_LOG_WARNING("No Sprite Component for Particle Component");
            return;
        }

        generator = new ParticleGenerator(sprite, this.gameObject.name, maxSize.x, maxSize.y);
        randomizer = new Random(this.gameObject.getUniqueID());

        for (int i = particles.size(); i < maxParticles; ++i)
        {
            Particle particle = this.generator.create();
            particles.add(particle);
            resetParticle(particle);
            particle.wake(false);
            GameWindow.getCurrentScene().addGameObject(particle.core);
        }
        Logger.FORGE_LOG_DEBUG("Starting");
    }

    public void resetAll()
    {
        this.resetAll = true;
    }

    @Override
    public void update(float DELTA_TIME)
    {
        clamp();
        if (resetAll)
        {
            for (Particle p : particles)
            {
                resetParticle(p);
            }
            resetAll = false;
        }
        for (int i = 0; i < particles.size(); ++i)
        {
            Particle particle = particles.get(i);
            GameObject core = particle.core;
            if (particle.lifeSpan < 0)
            {
                if (useOnce)
                {
                    particle.wake(false);
                }
                else resetParticle(particle);
            }
            else
            {
                core.transform.rotation += particle.angularVelocity * DELTA_TIME;
                core.transform.position.add(new Vector2f(particle.velocity).mul(DELTA_TIME));
                SpriteComponent renderable = core.getComponent(SpriteComponent.class);
                if (renderable != null) renderable.setColor(new Vector4f(startColor).lerp(finalColor, 1.0f - (particle.lifeSpan / particle.lifeTime)));
                particle.lifeSpan -= DELTA_TIME;
            }
        }
        if (debugDrawAtRuntime) debugDraw();
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        debugDraw();
        clamp();
    }

    protected void clamp()
    {
        fanStart = Math.min(fanEnd, fanStart);
        fanEnd = Math.max(fanStart, fanEnd);
        maxLifespan = Math.max(0.001f, maxLifespan);
        minLifespan = Math.max(Math.min(maxLifespan, minLifespan), 0f);
        maxSpeed = Math.max(0f, maxSpeed);
        minSpeed = Math.max(Math.min(minSpeed, maxSpeed), 0f);
        maxParticles = Math.max(0, maxParticles);
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
                particles.clear();
                break;
        }
    }

    @Override
    public void editorGUI()
    {
    }
}