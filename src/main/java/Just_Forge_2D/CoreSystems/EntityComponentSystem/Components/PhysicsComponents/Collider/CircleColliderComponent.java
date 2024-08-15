package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.PhysicsComponents.Collider;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Renderer.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CircleColliderComponent extends Component
{

    private Vector2f offset = new Vector2f();

    public Vector2f getOffset()
    {
        return this.offset;
    }
    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float RADIUS)
    {
        this.radius = RADIUS;
    }

    @Override
    public void editorUpdate(float DELTA_tIME)
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addCircle(center, this.radius, new Vector3f(1, 0, 0));
    }
}
