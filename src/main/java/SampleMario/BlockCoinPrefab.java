package SampleMario;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.AnimationSystem.AnimationState;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.Prefab;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.AssetPool;
import SampleMario.Components.BlockCoin;
import SampleMario.Components.QuestionBlock;

public class BlockCoinPrefab implements Prefab
{
    @Override
    public GameObject create() {
        SpriteSheet items = AssetPool.getSpriteSheet("Items");
        GameObject coin = PrefabManager.generateDefaultSpriteObject(items.getSprite(7), 0.25f, 0.25f);

        AnimationState coinFlip = new AnimationState();
        coinFlip.title = "CoinFlip";
        float defaultFrameTime = 0.23f;
        coinFlip.addFrame(items.getSprite(7), 0.57f);
        coinFlip.addFrame(items.getSprite(8), defaultFrameTime);
        coinFlip.addFrame(items.getSprite(9), defaultFrameTime);
        coinFlip.setLoop(true);

        AnimationComponent stateMachine = new AnimationComponent();
        stateMachine.addState(coinFlip);
        stateMachine.setDefaultState(coinFlip.title);
        coin.addComponent(stateMachine);
        coin.addComponent(new QuestionBlock());

        coin.addComponent(new BlockCoin());

        return coin;
    }
}
