package SampleMario.GameCode;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.AnimationSystem.AnimationState;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.Enums.BodyType;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.PrefabSystem.Prefab;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.AssetPool;
import SampleMario.Components.QuestionBlock;
import org.joml.Vector2f;

public class QuestionBlockPrefab implements Prefab
{
    @Override
    public GameObject create() {
        SpriteSheet playerSprites = AssetPool.getSpriteSheet("Items");
        GameObject questionBlock = PrefabManager.generateDefaultSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

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
        AnimationComponent stateMachine = new AnimationComponent();
        stateMachine.addState(flicker);
        stateMachine.addState(inactive);
        stateMachine.setDefaultState(flicker.title);
        stateMachine.addStateTrigger(flicker.title, inactive.title, "setInactive");
        questionBlock.addComponent(stateMachine);
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
