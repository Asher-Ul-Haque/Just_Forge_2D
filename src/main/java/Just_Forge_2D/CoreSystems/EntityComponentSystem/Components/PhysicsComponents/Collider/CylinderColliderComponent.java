package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.PhysicsComponents.Collider;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.Component;
import org.joml.Vector2f;

public class CylinderColliderComponent extends Component
{
    private transient CircleColliderComponent topCircle = new CircleColliderComponent();
    private transient CircleColliderComponent bottomCircle = new CircleColliderComponent();
    private transient BoxColliderComponent box = new BoxColliderComponent();
    private transient boolean resetFixtureNextFrame = false;

    public float width = 0.1f;
    public float height = 0.2f;
    public Vector2f offset = new Vector2f();
}
