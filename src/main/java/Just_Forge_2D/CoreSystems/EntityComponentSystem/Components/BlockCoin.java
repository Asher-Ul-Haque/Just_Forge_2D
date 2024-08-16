package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components;

import Just_Forge_2D.Utils.AssetPool;
import org.joml.Vector2f;

public class BlockCoin extends Component
{
    private Vector2f topY;
    private float coinSpeed = 1.4f;

    @Override
    public void start()
    {
        topY = new Vector2f(this.gameObject.transform.position.y).add(0, 0.5f);
        AssetPool.getSound("Coin").play();
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (this.gameObject.transform.position.y < topY.y)
        {
            this.gameObject.transform.position.y += DELTA_TIME * coinSpeed;
            this.gameObject.transform.scale.x -= (0.5f * DELTA_TIME) % -1.0f;
        }
        else
        {
            gameObject.destroy();
        }
    }
}
