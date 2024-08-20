package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.AnimationSystem.AnimationState;
import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EntityComponentSystem.Components.BlockCoin;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CylinderColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.EntityComponentSystem.Components.PlayerControllerComponent;
import Just_Forge_2D.EntityComponentSystem.Components.QuestionBlock;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.RenderingSystems.SpriteSheet;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.Enums.BodyType;
import Just_Forge_2D.Utils.AssetPool;
import org.joml.Vector2f;

public class Prefabs
{
    // - - - Generate Game Objects

    public static GameObject generateSpriteObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        return generateSpriteObject("Auto Generated", SPRITE, SIZE_X, SIZE_Y);
    }

    public static GameObject generateSpriteObject(String NAME, Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        GameObject block = EditorSystemManager.editorScene.createGameObject(NAME);
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
        idle.title = "Idle";
        idle.addFrame(playerSprites.getSprite(0), 0.1f);
        idle.setLoop(false);

        AnimationState jump = new AnimationState();
        jump.title = "Jump";
        jump.addFrame(playerSprites.getSprite(5), 0.1f);
        jump.setLoop(false);

        AnimationState bigRun = new AnimationState();
        bigRun.title = "BigRun";
        bigRun.addFrame(playerSprites.getSprite(5), 0.1f);
        bigRun.addFrame(bigPlayerSprites.getSprite(1), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(2), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(3), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(2), defaultFrameTime);
        bigRun.addFrame(bigPlayerSprites.getSprite(1), defaultFrameTime);
        bigRun.setLoop(true);

        AnimationState bigSwitchDirection = new AnimationState();
        bigSwitchDirection.title = "Big Switch Direction";
        bigSwitchDirection.addFrame(bigPlayerSprites.getSprite(4),0.1f);
        bigSwitchDirection.setLoop(true);

        AnimationState bigIdle = new AnimationState();
        bigIdle.title = "BigIdle";
        bigIdle.addFrame(bigPlayerSprites.getSprite(0), 0.1f);
        bigIdle.setLoop(false);

        AnimationState bigJump = new AnimationState();
        bigJump.title = "BigJump";
        bigJump.addFrame(bigPlayerSprites.getSprite(5), 0.1f);
        bigJump.setLoop(false);

        int fireOffset = 21;
        AnimationState fireRun = new AnimationState();
        fireRun.title = "FireRun";
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 1), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 2), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 3), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 2), defaultFrameTime);
        fireRun.addFrame(bigPlayerSprites.getSprite(fireOffset + 1), defaultFrameTime);
        fireRun.setLoop(true);

        AnimationState fireSwitchDirection = new AnimationState();
        fireSwitchDirection.title = "Fire Switch Direction";
        fireSwitchDirection.addFrame(bigPlayerSprites.getSprite(fireOffset + 4), 0.1f);
        fireSwitchDirection.setLoop(false);

        AnimationState fireIdle = new AnimationState();
        fireIdle.title = "FireIdle";
        fireIdle.addFrame(bigPlayerSprites.getSprite(fireOffset + 0), 0.1f);
        fireIdle.setLoop(false);

        AnimationState fireJump = new AnimationState();
        fireJump.title = "FireJump";
        fireJump.addFrame(bigPlayerSprites.getSprite(fireOffset + 5), 0.1f);
        fireJump.setLoop(false);

        AnimationState die = new AnimationState();
        die.title = "Die";
        die.addFrame(playerSprites.getSprite(6), 0.1f);
        die.setLoop(false);

        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.addState(run);
        animationComponent.addState(idle);
        animationComponent.addState(switchDirections);
        animationComponent.addState(jump);
        animationComponent.addState(die);

        animationComponent.addState(bigRun);
        animationComponent.addState(bigIdle);
        animationComponent.addState(bigSwitchDirection);
        animationComponent.addState(bigJump);

        animationComponent.setDefaultState(idle.title);
        animationComponent.addStateTrigger(run.title, switchDirections.title, "Switch directions");
        animationComponent.addStateTrigger(run.title, idle.title, "stopRunning");
        animationComponent.addStateTrigger(run.title, jump.title, "jump");
        animationComponent.addStateTrigger(switchDirections.title, idle.title, "stopRunning");
        animationComponent.addStateTrigger(switchDirections.title, run.title, "startRunning");
        animationComponent.addStateTrigger(switchDirections.title, jump.title, "jump");
        animationComponent.addStateTrigger(idle.title, run.title, "startRunning");
        animationComponent.addStateTrigger(idle.title, jump.title, "jump");
        animationComponent.addStateTrigger(jump.title, idle.title, "stopJumping");

        animationComponent.addStateTrigger(bigRun.title, bigSwitchDirection.title, "switchDirection");
        animationComponent.addStateTrigger(bigRun.title, bigIdle.title, "stopRunning");
        animationComponent.addStateTrigger(bigRun.title, bigJump.title, "jump");
        animationComponent.addStateTrigger(bigSwitchDirection.title, bigIdle.title, "stopRunning");
        animationComponent.addStateTrigger(bigSwitchDirection.title, bigRun.title, "startRunning");
        animationComponent.addStateTrigger(bigSwitchDirection.title, bigJump.title, "jump");
        animationComponent.addStateTrigger(bigIdle.title, bigRun.title, "startRunning");
        animationComponent.addStateTrigger(bigIdle.title, bigJump.title, "jump");
        animationComponent.addStateTrigger(bigJump.title, bigIdle.title, "stopJumping");

        animationComponent.addStateTrigger(fireRun.title, fireSwitchDirection.title, "switchDirection");
        animationComponent.addStateTrigger(fireRun.title, fireIdle.title, "stopRunning");
        animationComponent.addStateTrigger(fireRun.title, fireJump.title, "jump");
        animationComponent.addStateTrigger(fireSwitchDirection.title, fireIdle.title, "stopRunning");
        animationComponent.addStateTrigger(fireSwitchDirection.title, fireRun.title, "startRunning");
        animationComponent.addStateTrigger(fireSwitchDirection.title, fireJump.title, "jump");
        animationComponent.addStateTrigger(fireIdle.title, fireRun.title, "startRunning");
        animationComponent.addStateTrigger(fireIdle.title, fireJump.title, "jump");
        animationComponent.addStateTrigger(fireJump.title, fireIdle.title, "stopJumping");

        animationComponent.addStateTrigger(run.title, bigRun.title, "powerup");
        animationComponent.addStateTrigger(idle.title, bigIdle.title, "powerup");
        animationComponent.addStateTrigger(switchDirections.title, bigSwitchDirection.title, "powerup");
        animationComponent.addStateTrigger(jump.title, bigJump.title, "powerup");
        animationComponent.addStateTrigger(bigRun.title, fireRun.title, "powerup");
        animationComponent.addStateTrigger(bigIdle.title, fireIdle.title, "powerup");
        animationComponent.addStateTrigger(bigSwitchDirection.title, fireSwitchDirection.title, "powerup");
        animationComponent.addStateTrigger(bigJump.title, fireJump.title, "powerup");

        animationComponent.addStateTrigger(bigRun.title, run.title, "damage");
        animationComponent.addStateTrigger(bigIdle.title, idle.title, "damage");
        animationComponent.addStateTrigger(bigSwitchDirection.title, switchDirections.title, "damage");
        animationComponent.addStateTrigger(bigJump.title, jump.title, "damage");
        animationComponent.addStateTrigger(fireRun.title, bigRun.title, "damage");
        animationComponent.addStateTrigger(fireIdle.title, bigIdle.title, "damage");
        animationComponent.addStateTrigger(fireSwitchDirection.title, bigSwitchDirection.title, "damage");
        animationComponent.addStateTrigger(fireJump.title, bigJump.title, "damage");

        animationComponent.addStateTrigger(run.title, die.title, "die");
        animationComponent.addStateTrigger(switchDirections.title, die.title, "die");
        animationComponent.addStateTrigger(idle.title, die.title, "die");
        animationComponent.addStateTrigger(jump.title, die.title, "die");
        animationComponent.addStateTrigger(bigRun.title, run.title, "die");
        animationComponent.addStateTrigger(bigSwitchDirection.title, switchDirections.title, "die");
        animationComponent.addStateTrigger(bigIdle.title, idle.title, "die");
        animationComponent.addStateTrigger(bigJump.title, jump.title, "die");
        animationComponent.addStateTrigger(fireRun.title, bigRun.title, "die");
        animationComponent.addStateTrigger(fireSwitchDirection.title, bigSwitchDirection.title, "die");
        animationComponent.addStateTrigger(fireIdle.title, bigIdle.title, "die");
        animationComponent.addStateTrigger(fireJump.title, bigJump.title, "die");

        mario.addComponent(animationComponent);

        CylinderColliderComponent pb = new CylinderColliderComponent();
        pb.width = 0.39f;
        pb.height = 0.31f;
        RigidBodyComponent rb = new RigidBodyComponent();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);

        mario.addComponent(rb);
        //mario.addComponent(pb);
        mario.addComponent(new BoxColliderComponent());
        mario.addComponent(new PlayerControllerComponent());

        return mario;
    }

    public static GameObject generateBlockCoin() {
        SpriteSheet items = AssetPool.getSpriteSheet("Items");
        GameObject coin = generateSpriteObject(items.getSprite(7), 0.25f, 0.25f);

        AnimationState coinFlip = new AnimationState();
        coinFlip.title = "CoinFlip";
        float defaultFrameTime = 0.23f;
        coinFlip.addFrame(items.getSprite(7), 0.57f);
        coinFlip.addFrame(items.getSprite(8), defaultFrameTime);
        coinFlip.addFrame(items.getSprite(9), defaultFrameTime);
        coinFlip.setLoop(true);

        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.addState(coinFlip);
        animationComponent.setDefaultState(coinFlip.title);
        coin.addComponent(animationComponent);
        coin.addComponent(new QuestionBlock());

        coin.addComponent(new BlockCoin());

        return coin;
    }

    public static GameObject generateQuestionBlock() {
        SpriteSheet playerSprites = AssetPool.getSpriteSheet("Items");
        GameObject questionBlock = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        AnimationState flicker = new AnimationState();
        flicker.title = "Question";
        float defaultFrameTime = 0.23f;
        flicker.addFrame(playerSprites.getSprite(0), 0.57f);
        flicker.addFrame(playerSprites.getSprite(1), defaultFrameTime);
        flicker.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        flicker.setLoop(true);

        AnimationState inactive = new AnimationState();
        inactive.title = "Inactive";
        inactive.addFrame(playerSprites.getSprite(3), 0.1f);
        inactive.setLoop(false);
        AnimationComponent animationComponent = new AnimationComponent();
        animationComponent.addState(flicker);
        animationComponent.addState(inactive);
        animationComponent.setDefaultState(flicker.title);
        animationComponent.addStateTrigger(flicker.title, inactive.title, "setInactive");
        questionBlock.addComponent(animationComponent);
        questionBlock.addComponent(new QuestionBlock());

        RigidBodyComponent rb = new RigidBodyComponent();
        rb.setBodyType(BodyType.Static);
        questionBlock.addComponent(rb);
        BoxColliderComponent b2d = new BoxColliderComponent();
        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
        questionBlock.addComponent(b2d);

        return questionBlock;
    }
}