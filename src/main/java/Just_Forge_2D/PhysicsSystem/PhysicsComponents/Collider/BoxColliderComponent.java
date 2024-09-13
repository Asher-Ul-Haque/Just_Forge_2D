package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;


// - - - Box Collider
public class BoxColliderComponent extends Component {
    // - - - private variables
    private boolean autoScale = true;
    private boolean showHitboxAtRuntime = false;
    private Vector2f halfSize = new Vector2f(0.25f);
    private final Vector2f origin = new Vector2f();

    private Vector2f offset = new Vector2f();
    private Vector4f hitboxColor = new Vector4f(1.0f).sub(MainWindow.get().getClearColor());

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
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation, new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
        if (autoScale)
        {
            setHalfSize(new Vector2f(this.gameObject.transform.scale.x, this.gameObject.transform.scale.y));
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (showHitboxAtRuntime)
        {
            Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
            DebugPencil.addBox(center, this.halfSize, this.gameObject.transform.rotation, new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z));
        }
    }

    public void setOffset(Vector2f OFFSET)
    {
        this.offset.set(OFFSET);
    }
}
