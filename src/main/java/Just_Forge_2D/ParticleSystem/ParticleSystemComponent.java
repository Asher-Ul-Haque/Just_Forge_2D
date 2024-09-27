package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EditorSystem.Windows.SceneHierarchyWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;
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
    private float minLifespan = 0.05f;
    private float maxLifespan = 0.2f;
    private float fanStart = 0;
    private float fanEnd = (float) (2 * Math.PI);
    private float angularVelocity = 0.2f;
    private float minSpeed = 0.1f;
    private float maxSpeed = 0.5f;
    private boolean debugDrawAtRuntime = false;
    private transient ParticleGenerator generator;
    private Vector4f debugColor = new Vector4f(1.0f).sub(MainWindow.get().getClearColor());
    private boolean keepPhysics = false;
    private boolean useOnce = false;
    private Vector2f offset = new Vector2f();
    private int particleLayer;
    private String templateName;

    private Vector2f generateVelocity()
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

    private void resetParticle(Particle PARTICLE)
    {
        PARTICLE.lifeTime = randomizer.nextFloat(minLifespan, maxLifespan + 0.001f);
        PARTICLE.core.transform.position = new Vector2f(this.gameObject.transform.position).add(offset);
        SpriteComponent renderable = PARTICLE.core.getComponent(SpriteComponent.class);
        if (renderable != null) renderable.setColor(new Vector4f(startColor));
        PARTICLE.lifeSpan = PARTICLE.lifeTime;
        PARTICLE.angularVelocity = angularVelocity;
        PARTICLE.core.transform.scale = generateSize();
        PARTICLE.velocity = generateVelocity();
        PARTICLE.core.transform.rotation = randomizer.nextFloat();
        PARTICLE.core.transform.layer = particleLayer;
        if (keepPhysics)
        {
            RigidBodyComponent rb = PARTICLE.core.getComponent(RigidBodyComponent.class);
            rb.setTransform(PARTICLE.core.transform);
            rb.addImpulse(PARTICLE.velocity);
            rb.setAngularVelocity(PARTICLE.angularVelocity);
            rb.addTorque(PARTICLE.angularVelocity);
        }
    }


    @Override
    public void start()
    {
        if (templateName == null)
        {
            templateName = this.gameObject.name;
        }
        generator = new ParticleGenerator(MainWindow.getCurrentScene().getGameObject(templateName), keepPhysics);

        randomizer = new Random(this.gameObject.getUniqueID());
        for (int i = particles.size(); i < maxParticles; ++i)
        {
            Particle particle = this.generator.create();
            particles.add(particle);
            resetParticle(particle);
            MainWindow.getCurrentScene().addGameObject(particle.core);
        }
        Logger.FORGE_LOG_DEBUG("Starting");
    }

    @Override
    public void update(float DELTA_TIME)
    {
        clamp();
        for (Particle particle : this.particles)
        {
            GameObject core = particle.core;
            if (particle.lifeSpan < 0)
            {
                if (useOnce) core.destroy();
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


    private void clamp()
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
        String name = this.generator != null ? templateName : this.gameObject.name;
        String input = Widgets.inputText("Template", name);
        if (!input.equals(name))
        {
            for (GameObject g : SceneHierarchyWindow.gameObjectList())
            {
                if (g.name.equals(input))
                {
                    templateName = g.name;
                    break;
                }
            }
        }
        super.editorGUI();
    }

    public boolean keepingPhysics() { return this.keepPhysics; }
    public void setKeepPhysics(boolean REALLY) { this.keepPhysics = REALLY; }
}
