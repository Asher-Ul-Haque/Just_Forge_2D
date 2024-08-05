package Just_Forge_2D.Core.ECS.Components.PhysicsComponents;

import Just_Forge_2D.Core.ECS.Components.Component;
import org.joml.Vector2f;

public class BoxColliderComponent extends Component
{
    private Vector2f halfSize = new Vector2f(1);

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f HALF_SIZE)
    {
        this.halfSize = HALF_SIZE;
    }
}
