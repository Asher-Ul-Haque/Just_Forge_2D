package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Math;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CircleColliderComponent extends Component {

    private Vector2f offset = new Vector2f();

    public Vector2f getOffset()
    {
        return this.offset;
    }
    private boolean autoScale = true;
    private boolean debugDrawAtRuntime = false;
    private transient boolean useCollisionColor = false;
    private Vector4f hitboxColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    private Vector4f collisionColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);

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
        debugDraw();
        if (autoScale)
        {
            setRadius(Math.max(this.gameObject.transform.scale.x, this.gameObject.transform.scale.y) / 2f);
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (debugDrawAtRuntime)
        {
            debugDraw();
        }
    }

    private void debugDraw()
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addCircle(center, this.radius, useCollisionColor ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z) : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
    }

    @Override
    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        useCollisionColor = true;
    }

    @Override
    public void endCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        useCollisionColor = false;
    }
}
