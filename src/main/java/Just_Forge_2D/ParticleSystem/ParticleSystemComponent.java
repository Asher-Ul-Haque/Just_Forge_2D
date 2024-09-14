package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.CameraControlComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.SimpleCharacterController;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ParticleSystemComponent extends Component implements Observer
{
    private List<GameObject> particles = new ArrayList<>();
    private int maxParticles = 3;
    private int nextAvailableIndex = 0;
    private Vector2f size = new Vector2f(0.1f);
    private static Random randomizer;
    private float maxRandomNess = 0.1f;
    private Vector4f finalColor = new Vector4f(1.0f, 1.0f, 0.0f, 1.0f);
    private float lifespan = 10;

    public ParticleSystemComponent()
    {
        EventManager.addObserver(this);
    }


    @Override
    public void start()
    {
        particles = new ArrayList<>(maxParticles);
        randomizer = new Random(this.gameObject.getUniqueID());
        for (int i = 0; i < maxParticles; ++i)
        {
            GameObject particle = this.gameObject.copy();
            particle.name = "particle: " + this.gameObject + " " + i;
            particle.noSerialize();
            particle.removeComponent(ParticleSystemComponent.class);
            particle.removeComponent(CameraControlComponent.class);
            particle.removeComponent(SimpleCharacterController.class);
            particle.transform.scale.add(randomizer.nextFloat(-maxRandomNess, maxRandomNess), randomizer.nextFloat(-maxRandomNess, maxRandomNess));
            particles.add(particle);
            MainWindow.getCurrentScene().addGameObject(particle);
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        for (GameObject particle : this.particles)
        {
            if (lifespan < 0) particle.destroy();
            particle.transform.rotation += randomizer.nextFloat(-maxRandomNess, maxRandomNess);
            particle.transform.position.add(randomizer.nextFloat(-maxRandomNess, maxRandomNess), randomizer.nextFloat(-maxRandomNess, maxRandomNess));
            particle.getCompoent(SpriteComponent.class).setColor(particle.getCompoent(SpriteComponent.class).getColor().lerp(finalColor, lifespan));
            lifespan -= DELTA_TIME;
        }
    }

    @Override
    public void onNotify(GameObject OBJECT, Event EVENT)
    {
        switch (EVENT.type)
        {
            case ForgeStop:
                for (GameObject particle : particles)
                {
                    Logger.FORGE_LOG_DEBUG("Destroying particle : " + particle.name);
                    particle.destroy();
                }
                break;
        }
    }
}
