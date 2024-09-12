package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CircleColliderComponent extends ColliderComponent
{

    private Vector2f offset = new Vector2f();

    public Vector2f getOffset()
    {
        return this.offset;
    }

    private Vector3f hitboxColor = new Vector3f(super.baseColor.x, super.baseColor.y, super.baseColor.z);

    public void setOffset(Vector2f OFFSET)
    {
        this.offset.set(OFFSET);
    }

    private float radius = 0.125f;

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
        DebugPencil.addCircle(center, this.radius, hitboxColor);
        if (autoScale)
        {
            setRadius(Math.max(this.gameObject.transform.scale.x, this.gameObject.transform.scale.y) / 2f);
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (showHitboxAtRuntime)
        {
            Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
            DebugPencil.addCircle(center, this.radius, hitboxColor);
        }
    }
}
