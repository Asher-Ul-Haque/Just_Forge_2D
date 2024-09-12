package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;


// - - - Box Collider
public class BoxColliderComponent extends ColliderComponent
{
    // - - - private variables
    private Vector2f halfSize = new Vector2f(0.25f);
    private final Vector2f origin = new Vector2f();

    private Vector2f offset = new Vector2f();
    private Vector3f hitboxColor = new Vector3f(super.baseColor.x, super.baseColor.y, super.baseColor.z);

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
    @Override
    public void editorUpdate(float DELTA_tIME)
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation, hitboxColor);
        if (autoScale)
        {
            setHalfSize(new Vector2f(this.gameObject.transform.scale.x, this.gameObject.transform.scale.y));
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (showHitboxAtRuntime)
        {
            Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
            DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation, hitboxColor);
        }
    }

    public void setOffset(Vector2f OFFSET)
    {
        this.offset.set(OFFSET);
    }
}
