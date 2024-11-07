package Just_Forge_2D.PrefabSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;

public class CopyCatPrefab implements Prefab
{
    private String name;
    private GameObject template;

    public CopyCatPrefab(String NAME, GameObject OBJ)
    {
        this.name = NAME;
        this.template = OBJ.copy();
    }

    @Override
    public GameObject create()
    {
        return template.copy();
    }
}
