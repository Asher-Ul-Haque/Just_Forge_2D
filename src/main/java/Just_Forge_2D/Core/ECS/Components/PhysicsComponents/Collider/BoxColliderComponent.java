package Just_Forge_2D.Core.ECS.Components.PhysicsComponents.Collider;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Renderer.DebugPencil;
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

    @Override
    public void editorUpdate(float DELTA_tIME)
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation);
    }
}
