package Just_Forge_2D.PrefabSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.WindowSystem.GameWindow;

public class NonSpritePrefab implements Prefab
{
    private String name;
    private float sizeX, sizeY;

    public NonSpritePrefab(String NAME, float SIZE_X, float SIZE_Y)
    {
        this.name = NAME;
        this.sizeX = SIZE_X;
        this.sizeY = SIZE_Y;
    }

    @Override
    public GameObject create()
    {
        GameObject block = GameWindow.getCurrentScene().createGameObject(this.name);
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        return block;
    }
}
