package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.Animations.AnimationState;
import Just_Forge_2D.Core.Animations.StateMachine;
import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Utils.AssetPool;

public class Prefabs
{
    public static GameObject generateSpriteObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        return generateSpriteObject("Auto Generated", SPRITE, SIZE_X, SIZE_Y);
    }

    public static GameObject generateSpriteObject(String NAME, Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        GameObject block = ForgeDynamo.getCurrentScene().createGameObject(NAME);
        block.transform.scale.x = SIZE_X;
        block.transform.scale.y = SIZE_Y;
        block.transform.layer = 1;
        SpriteComponent sprite = new SpriteComponent();
        sprite.setSprite(SPRITE);
        block.addComponent(sprite);

        return block;
    }

    public static GameObject generateMario()
    {
        SpriteSheet playerSprites = AssetPool.getSpriteSheet("Spritesheet.png");
        GameObject mario = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.25f;
        run.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        mario.addComponent(stateMachine);


        return mario;
    }
}
