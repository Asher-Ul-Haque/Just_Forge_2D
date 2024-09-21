package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.ForgeMath;
import imgui.ImGui;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class EdgeColliderComponent extends Component
{
    private List<EdgeCollider> colliders = new ArrayList<>();
    private boolean debugDrawAtRuntime = false;
    private transient boolean useCollisionColor = false;
    private Vector4f hitboxColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    private Vector4f collisionColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);

    public void addCollider()
    {
        EdgeCollider collider = new EdgeCollider((new Vector2f(this.gameObject.transform.position)).sub(new Vector2f(this.gameObject.transform.scale)).div(2), (new Vector2f(this.gameObject.transform.position)).add(new Vector2f(this.gameObject.transform.scale)).div(2));
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
        for (EdgeCollider collider : colliders)
        {
            Vector2f start = new Vector2f(collider.getEdgeStart()).add(this.gameObject.transform.position);
            Vector2f end = new Vector2f(collider.getEdgeEnd()).add(this.gameObject.transform.position);
            ForgeMath.rotate(start, this.gameObject.transform.rotation, this.gameObject.transform.position);
            ForgeMath.rotate(end, this.gameObject.transform.rotation, this.gameObject.transform.position);
            DebugPencil.addLine(start, end, useCollisionColor ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z) : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
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

    public List<EdgeCollider> getColliders()
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
