package Just_Forge_2D.Core.ECS.Components.PhysicsComponents.Collider;

import Just_Forge_2D.Core.ECS.Components.Component;

public class CircleColliderComponent extends Collider
{
    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float RADIUS)
    {
        this.radius = RADIUS;
    }
}
