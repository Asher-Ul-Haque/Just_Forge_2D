package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import imgui.ImGui;
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
    private boolean autoScale = true;

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
    public void editorGUI()
    {
        super.editorGUI();
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        if (ImGui.checkbox("Auto Scale", this.autoScale))
        {
            this.autoScale = !this.autoScale;
        }
        Theme.resetDefaultTextColor();
        this.radius = Widgets.drawFloatControl("Radius", this.radius);
        Widgets.drawVec2Control("Offset", this.offset);
    }


    @Override
    public void debugDraw()
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addCircle(center, this.radius, useCollisionColor ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z) : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
    }
}