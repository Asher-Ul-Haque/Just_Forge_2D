package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class PolygonColliderComponent extends ColliderComponent
{
    private List<CustomCollider> colliders = new ArrayList<>();


    public void addCollider()
    {
        Vector2f scale = this.gameObject.transform.scale;

        float sideLength = scale.x;

        Vector2f v1 = new Vector2f(0, sideLength);
        Vector2f v2 = new Vector2f(-sideLength / 2, 0);
        Vector2f v3 = new Vector2f(sideLength / 2, 0);
        List<Vector2f> give = new ArrayList<>(3);

        give.add(v1);
        give.add(v2);
        give.add(v3);

        CustomCollider collider = new CustomCollider(give);
        collider.resetToRegularPolygon(3);
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
    public void debugDraw()
    {
        for (CustomCollider collider : colliders)
        {
            collider.debugDraw(this.gameObject, useCollisionColor ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z) : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
        }
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
            if (ImGui.collapsingHeader("Collider : " + (i + 1)))
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
