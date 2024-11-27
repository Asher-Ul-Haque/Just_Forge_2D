package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EditorSystem.EditorComponents.NonPickableComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.Sprite;
import Just_Forge_2D.Utils.Logger;

public class ParticleGenerator
{
    protected Sprite sprite;
    protected String name;
    protected float size_x;
    protected float size_y;
    protected transient Class<? extends ParticleBehaviorComponent> behaviorComponent;

    protected transient GameObject template;

    public ParticleGenerator(Sprite SPRITE, String NAME, float SIZE_X, float SIZE_Y, Class<? extends ParticleBehaviorComponent> BEHAVIOUR)
    {
        sprite = SPRITE;
        name = NAME;
        size_x = SIZE_X;
        size_y = SIZE_Y;
        template = PrefabManager.generateObject(SPRITE, SIZE_X, SIZE_Y);
        template.name = NAME + " particle: ";
        behaviorComponent = BEHAVIOUR;
    }

    public Particle create()
    {
        if (template == null)
        {
            template = PrefabManager.generateObject(sprite, size_x, size_y);
            template.name = name + " particle: ";
            template.addComponent(new NonPickableComponent());
            if (behaviorComponent != null)
            {
                try
                {
                    template.addComponent(behaviorComponent.getDeclaredConstructor().newInstance());
                }
                catch (Exception e)
                {
                    Logger.FORGE_LOG_ERROR("Cant find parameterless contructor for behaviour class : " + behaviorComponent.getName());
                }
            }
            template.noSerialize();
        }
        return new Particle(this.template.copy());
    }

    public void setSprite(Sprite SPRITE)
    {
        if (this.sprite == SPRITE) return;
        this.sprite = SPRITE;
    }
}
