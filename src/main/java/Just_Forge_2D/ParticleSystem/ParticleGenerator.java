package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EditorSystem.EditorComponents.NonPickableComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.Sprite;

public class ParticleGenerator
{
    protected Sprite sprite;
    protected String name;
    protected float size_x;
    protected float size_y;

    protected transient GameObject template;

    public ParticleGenerator(Sprite SPRITE, String NAME, float SIZE_X, float SIZE_Y)
    {
        sprite = SPRITE;
        name = NAME;
        size_x = SIZE_X;
        size_y = SIZE_Y;
        template = PrefabManager.generateObject(SPRITE, SIZE_X, SIZE_Y);
        template.name = NAME + " particle: ";
    }

    public Particle create()
    {
        if (template == null)
        {
            template = PrefabManager.generateObject(sprite, size_x, size_y);
            template.name = name + " particle: ";
            template.addComponent(new NonPickableComponent());
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
