package Just_Forge_2D.Core.EntityComponentSystem.Components.PhysicsComponents.Collider;

import Just_Forge_2D.Renderer.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;

// - - - Box Collider
public class BoxColliderComponent extends Collider
{
    // - - - private variables
    private Vector2f halfSize = new Vector2f(0.25f);
    private final Vector2f origin = new Vector2f();


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
        DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation, new Vector3f(1, 0, 0));
    }
}
