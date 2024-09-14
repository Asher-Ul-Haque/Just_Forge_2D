package Just_Forge_2D.ParticleSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.Prefab;

public class ParticlePrefab implements Prefab
{
    private String name;
    private SpriteComponent spriteComponent;
    private float sizeX, sizeY;
    @Override
    public GameObject create()
    {
        return null;
    }
}
