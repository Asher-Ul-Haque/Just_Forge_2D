package Just_Forge_2D.Core.EntityComponentSystem.Components.PhysicsComponents.Collider;

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
