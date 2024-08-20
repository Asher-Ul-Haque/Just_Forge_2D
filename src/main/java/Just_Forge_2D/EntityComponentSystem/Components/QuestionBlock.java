package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Prefabs;
import Just_Forge_2D.EntityComponentSystem.GameObject;

public class QuestionBlock extends Block
{
    private enum BlockType
    {
        Coin,
        PowerUp
    }

    public BlockType blockType = BlockType.Coin;

    @Override
    void playerHit(PlayerControllerComponent CONTROLLER)
    {
        switch (blockType)
        {
            case Coin:
                doCoin(CONTROLLER);
                break;

            case PowerUp:
                doPowerup(CONTROLLER);
                break;
        }

        AnimationComponent machine = gameObject.getComponent(AnimationComponent.class);
        if (machine != null)
        {
            machine.trigger("setInactive");
            this.setInactive();
        }
    }

    private void doCoin(PlayerControllerComponent CONTROLLER)
    {
        GameObject coin = Prefabs.generateBlockCoin();
        coin.transform.position.set(this.gameObject.transform.position);
        coin.transform.position.y += 0.25f;
        EditorSystemManager.editorScene.addGameObject(coin);
    }

    private void doPowerup(PlayerControllerComponent CONTROLLER)
    {

    }


}
