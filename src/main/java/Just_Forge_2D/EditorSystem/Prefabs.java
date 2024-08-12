package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.Core.AnimationSystem.AnimationState;
import Just_Forge_2D.Core.AnimationSystem.StateMachine;
import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.EntityComponentSystem.GameObject;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Utils.AssetPool;

public class Prefabs
{
    // - - - Generate Game Objects

    public static GameObject generateSpriteObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        return generateSpriteObject("Auto Generated", SPRITE, SIZE_X, SIZE_Y);
    }

    public static GameObject generateSpriteObject(String NAME, Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        GameObject block = ForgeDynamo.getCurrentScene().createGameObject(NAME);
        block.transform.scale.x = SIZE_X;
        block.transform.scale.y = SIZE_Y;
        SpriteComponent sprite = new SpriteComponent();
        sprite.setSprite(SPRITE);
        block.addComponent(sprite);

        return block;
    }

    public static GameObject generateMario()
    {
        SpriteSheet playerSprites = AssetPool.getSpriteSheet("Spritesheet");
        SpriteSheet bigPlayerSprites = AssetPool.getSpriteSheet("Big Spritesheet");
        GameObject mario = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        // - - - Little Mario Animations
        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.2f;
        run.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        AnimationState switchDirections = new AnimationState();
        switchDirections.title = "Switch Directions";
        switchDirections.addFrame(playerSprites.getSprite(4), 0.1f);
        switchDirections.setLoop(false);

        AnimationState idle = new AnimationState();

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        mario.addComponent(stateMachine);


        return mario;
    }
}
