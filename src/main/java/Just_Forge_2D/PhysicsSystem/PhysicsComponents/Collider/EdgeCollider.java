package PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import org.joml.Vector2f;

public class EdgeCollider extends Component
{
    private Vector2f edgeStart;
    private Vector2f edgeEnd;

    public EdgeCollider(Vector2f EDGE_START, Vector2f EDGE_END)
    {
        this.edgeStart = EDGE_START;
        this.edgeEnd = EDGE_END;
    }

    public Vector2f getEdgeStart()
    {
        return this.edgeStart;
    }

    public Vector2f getEdgeEnd()
    {
        return this.edgeEnd;
    }
}
