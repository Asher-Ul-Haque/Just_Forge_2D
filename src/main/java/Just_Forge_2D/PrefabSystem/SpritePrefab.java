package Just_Forge_2D.PrefabSystem;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;

public class SpritePrefab implements Prefab
{
    private String name;
    private Sprite sprite;
    private float sizeX, sizeY;

    public SpritePrefab(String NAME, Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        this.name = NAME;
        this.sprite = SPRITE;
        this.sizeX = SIZE_X;
        this.sizeY = SIZE_Y;
    }

    @Override
    public GameObject create()
    {
        GameObject block = MainWindow.getCurrentScene().createGameObject(this.name);
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteComponent spriteComponent = new SpriteComponent();
        spriteComponent.setSprite(this.sprite);
        block.addComponent(spriteComponent);
        return block;
    }
}
