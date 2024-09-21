package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsManagers.ColliderManager;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class CustomColliderComponent extends Component
{
    private List<CustomCollider> colliders = new ArrayList<>();
    private boolean debugDrawAtRuntime = false;
    private transient boolean useCollisionColor = false;
    private Vector4f hitboxColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    private Vector4f collisionColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);

    @Override
    public void start()
    {
        addCollider();
    }

    public void addCollider()
    {
        Vector2f pos = this.gameObject.transform.position;
        Vector2f scale = this.gameObject.transform.scale;

        Vector2f v1 = new Vector2f(scale.x * 0.5f, scale.y);
        Vector2f v2 = new Vector2f(0.5f * -scale.x, scale.y);
        Vector2f v3 = new Vector2f(5f, 5f);

        List<Vector2f> give = new ArrayList<>(3);

        give.add(v1);
        give.add(v2);
        give.add(v3);

        CustomCollider collider = new CustomCollider(give);
        colliders.add(collider);
    }

    public void removeCollider(int INDEX)
    {
        if (INDEX >= 0 && INDEX < colliders.size())
        {
            colliders.remove(INDEX);
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

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        debugDraw();
    }

    private void debugDraw()
    {
        for (CustomCollider collider : colliders)
        {
            collider.debugDraw(ColliderManager.getDebugShape(), this.gameObject);
        }
    }

    @Override
    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        for (CustomCollider collider : this.colliders)
        {
            collider.beginCollision();
        }
    }

    @Override
    public void endCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        for (CustomCollider collider : this.colliders)
        {
            collider.endCollision();
        }
    }

    public List<CustomCollider> getColliders()
    {
        return this.colliders;
    }
}
