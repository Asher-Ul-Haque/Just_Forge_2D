package Just_Forge_2D.Core.ECS.Components.PhysicsComponents.Collider;

import Just_Forge_2D.Core.ECS.Components.Component;
import org.joml.Vector2f;

public class BoxColliderComponent extends Collider
{
    private Vector2f halfSize = new Vector2f(1);
    private Vector2f origin = new Vector2f();

    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f HALF_SIZE)
    {
        this.halfSize = HALF_SIZE;
    }

    public Vector2f getOrigin()
    {
        return this.origin;
    }
}
