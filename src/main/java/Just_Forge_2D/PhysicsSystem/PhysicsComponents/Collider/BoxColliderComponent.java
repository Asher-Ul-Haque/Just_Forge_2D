package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;


import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;


// - - - Box Collider
public class BoxColliderComponent extends ColliderComponent
{
    // - - - private variables
    private boolean autoScale = true;
    private Vector2f halfSize = new Vector2f(0.25f);
    private final Vector2f origin = new Vector2f();
    private Vector2f offset = new Vector2f();


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
        debugDraw();
        if (autoScale)
        {
            setHalfSize(new Vector2f(this.gameObject.transform.scale.x, this.gameObject.transform.scale.y));
        }
    }

    @Override
    public void debugDraw()
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation, useCollisionColor ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z) : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();
        autoScale = Widgets.drawBoolControl(Icons.ExpandArrowsAlt+ "  Auto Scale", autoScale);
        if (autoScale) return;
        Widgets.drawVec2Control(Icons.Expand + "  Half Size", this.halfSize);
        Widgets.drawVec2Control(Icons.MapPin + "  Origin", this.origin);
        Widgets.drawVec2Control(Icons.LocationArrow + "  Offset", this.offset);
    }


    public void setOffset(Vector2f OFFSET)
    {
        this.offset.set(OFFSET);
    }
}