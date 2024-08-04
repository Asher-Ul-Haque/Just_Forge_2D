package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Window;
import org.joml.Vector2f;

public class Prefabs
{
    public static GameObject generateSpriteObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        return generateSpriteObject("Auto Generated", SPRITE, SIZE_X, SIZE_Y);
    }

    public static GameObject generateSpriteObject(String NAME, Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        GameObject block = Window.getCurrentScene().createGameObject(NAME);
        block.transform.scale.x = SIZE_X;
        block.transform.scale.y = SIZE_Y;
        block.transform.layer = 1;
        SpriteComponent sprite = new SpriteComponent();
        sprite.setSprite(SPRITE);
        block.addComponent(sprite);
        return block;
    }
}
