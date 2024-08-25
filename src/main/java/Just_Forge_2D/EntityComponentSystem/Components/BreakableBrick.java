package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.Utils.AssetPool;

public class BreakableBrick extends Block
{
    @Override
    void playerHit(PlayerControllerComponent CONTROLLER)
    {
        if (!CONTROLLER.isSmol())
        {
            AssetPool.getSound("Break Block").play();
            gameObject.destroy();
        }
    }
}
