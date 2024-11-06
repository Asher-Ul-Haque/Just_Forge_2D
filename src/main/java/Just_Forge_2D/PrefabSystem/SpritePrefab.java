package Just_Forge_2D.PrefabSystem;

import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.Sprite;
import Just_Forge_2D.WindowSystem.GameWindow;

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
        GameObject block = GameWindow.getCurrentScene().createGameObject(this.name);
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteComponent spriteComponent = new SpriteComponent();
        Sprite sprite = new Sprite();
        sprite.setHeight(this.sprite.getHeight());
        sprite.setWidth(this.sprite.getWidth());
        sprite.setTexture(this.sprite.getTexture());
        sprite.setTextureCoordinates(this.sprite.getTextureCoordinates());
        spriteComponent.setSprite(sprite);
        block.addComponent(spriteComponent);
        return block;
    }
}
