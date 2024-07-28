package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import org.joml.Vector2f;

public class Prefabs
{
    public static GameObject generateSpriteObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        GameObject block = new GameObject("Sprite OBject Generated",
                new TransformComponent(new Vector2f(), new Vector2f(SIZE_X, SIZE_Y)), 0);
        SpriteComponent sprite = new SpriteComponent();
        sprite.setSprite(SPRITE);
        block.addComponent(sprite);
        return block;
    }
}
