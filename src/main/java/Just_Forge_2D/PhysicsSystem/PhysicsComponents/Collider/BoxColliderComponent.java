package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.RenderingSystems.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;

// - - - Box Collider
public class BoxColliderComponent extends Component
{
    // - - - private variables
    private Vector2f halfSize = new Vector2f(0.25f);
    private final Vector2f origin = new Vector2f();

    private Vector2f offset = new Vector2f();

    public Vector2f getOffset()
    {
        return this.offset;
    }

    // - - - Functions - - -

    // - - - half size
    public Vector2f getHalfSize() {
        return halfSize;
    }

    public void setHalfSize(Vector2f HALF_SIZE)
    {
        this.halfSize = HALF_SIZE;
    }

    // - - - origin
    public Vector2f getOrigin()
    {
        return this.origin;
    }

    // - - - update
    public void setOffset(Vector2f OFFSET)
    {
        this.offset.set(OFFSET);
    }
}
