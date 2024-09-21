package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import imgui.ImGui;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class PolygonColliderComponent extends Component
{
    private List<CustomCollider> colliders = new ArrayList<>();
    private boolean debugDrawAtRuntime = false;
    private transient boolean useCollisionColor = false;
    private Vector4f hitboxColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    private Vector4f collisionColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);


    public void addCollider()
    {
        Vector2f scale = this.gameObject.transform.scale;

        float sideLength = scale.x;
        float height = (float) (Math.sqrt(3) / 2 * sideLength);

        Vector2f v1 = new Vector2f(0, height);                    // Top vertex
        Vector2f v2 = new Vector2f(-sideLength / 2, 0);            // Bottom left vertex
        Vector2f v3 = new Vector2f(sideLength / 2, 0);
        List<Vector2f> give = new ArrayList<>(3);

        give.add(v1);
        give.add(v2);
        give.add(v3);

        CustomCollider collider = new CustomCollider(give);
        collider.gameObject = this.gameObject;
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
            collider.debugDraw(this.gameObject, useCollisionColor ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z) : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
        }
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

    public List<CustomCollider> getColliders()
    {
        return this.colliders;
    }


    @Override
    public void editorGUI()
    {
        super.editorGUI();
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        ImGui.text("Collider Count: " + this.colliders.size());
        Theme.resetDefaultTextColor();

        for (int i = 0; i < colliders.size(); ++i)
        {
            if (ImGui.collapsingHeader("Collider : " + i + 1))
            {
                if (ImGui.button("Delete Collider" +"##i"))
                {
                    removeCollider(i);
                    continue;
                }
                colliders.get(i).editorGUI();
            }
        }
        if (ImGui.button("Add Collider"))
        {
            addCollider();
        }
    }
}
