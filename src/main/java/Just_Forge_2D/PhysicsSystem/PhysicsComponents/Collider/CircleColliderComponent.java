package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.RenderingSystem.DebugPencil;
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
    private boolean showHitboxAtRuntime = false;

    private Vector4f hitboxColor = new Vector4f(1.0f).sub(MainWindow.get().getClearColor());

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
        DebugPencil.addCircle(center, this.radius, new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
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
            DebugPencil.addCircle(center, this.radius, new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
        }
    }
}
