package Just_Forge_2D.Core.ECS.Components.PhysicsComponents;

import Just_Forge_2D.Core.ECS.Components.Component;

public class CircleColliderComponent extends Component
{
    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
