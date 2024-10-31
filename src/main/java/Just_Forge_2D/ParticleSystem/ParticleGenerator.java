package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.Sprite;

public class ParticleGenerator
{
    protected Sprite sprite;
    protected final GameObject template;

    public ParticleGenerator(Sprite SPRITE, String NAME, float SIZE_X, float SIZE_Y)
    {
        sprite = SPRITE;
        int index = 0;
        template = PrefabManager.generateObject(SPRITE, SIZE_X, SIZE_Y);
        template.name = NAME + " particle: " + index;
    }

    public Particle create()
    {
        return new Particle(this.template.copy());
    }
}
