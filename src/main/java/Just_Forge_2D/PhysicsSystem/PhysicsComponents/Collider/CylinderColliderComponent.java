package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsManagers.ColliderManager;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CylinderColliderComponent extends Component {
    private transient CircleColliderComponent topCircle = new CircleColliderComponent();
    private transient CircleColliderComponent bottomCircle = new CircleColliderComponent();
    private transient BoxColliderComponent box = new BoxColliderComponent();
    private transient boolean resetFixtureNextFrame = false;
    private Vector4f hitboxColor = new Vector4f(1.0f).sub(MainWindow.get().getClearColor());
    private boolean autoScale = true;
    private boolean showHitboxAtRuntime = false;
    public float width = 0.25f;
    public float height = 0.25f;
    public Vector2f offset = new Vector2f();

    @Override
    public void start()
    {
        this.topCircle.gameObject = this.gameObject;
        this.bottomCircle.gameObject = this.gameObject;
        this.box.gameObject = this.gameObject;
        recalculateColliders();
    }

    public void recalculateColliders()
    {
        float circleRadius = width / 2.0f;
        float boxHeight = height - (2 * circleRadius);
        topCircle.setRadius(circleRadius);
        bottomCircle.setRadius(circleRadius);
        topCircle.setOffset(new Vector2f(new Vector2f(offset).add(0, boxHeight / 2f)));
        bottomCircle.setOffset(new Vector2f(new Vector2f(offset).sub(0, boxHeight / 2f)));
        box.setHalfSize(new Vector2f(width, boxHeight));
        box.setOffset(new Vector2f(offset));
    }


    public CircleColliderComponent getTopCircle() {
        return topCircle;
    }

    public CircleColliderComponent getBottomCircle() {
        return bottomCircle;
    }

    public BoxColliderComponent getBox() {
        return box;
    }

    public void resetFixtures()
    {
        if (MainWindow.getPhysicsSystem().isLocked())
        {
            resetFixtureNextFrame = true;
            return;
        }

        resetFixtureNextFrame = false;

        if (gameObject != null)
        {
            RigidBodyComponent rb = gameObject.getCompoent(RigidBodyComponent.class);
            if (rb != null)
            {
                ColliderManager.resetCylinderCollider(rb, this);
            }
        }
    }

    public void setWidth(float WIDTH)
    {
        this.width = WIDTH;
        recalculateColliders();
        resetFixtures();
    }

    public void setHeight(float HEIGHT)
    {
        this.height = HEIGHT;
        recalculateColliders();
        resetFixtures();
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (showHitboxAtRuntime)
        {
            displayHitBox();
        }
        if (resetFixtureNextFrame)
        {
            resetFixtures();
            resetFixtureNextFrame = false;
        }
    }

    private void displayHitBox()
    {
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(topCircle.getOffset());
        Vector3f color = new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z);
        DebugPencil.addCircle(center, this.topCircle.getRadius(), color);
        center = new Vector2f(this.gameObject.transform.position).add(bottomCircle.getOffset());
        DebugPencil.addCircle(center, this.bottomCircle.getRadius(), color);
        center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addBox(center, this.box.getHalfSize(), this.gameObject.transform.rotation, color);
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (topCircle.gameObject == null || bottomCircle.gameObject == null || box.gameObject == null) start();
        displayHitBox();

        if (resetFixtureNextFrame)
        {
            resetFixtures();
            resetFixtureNextFrame = false;
        }

        if (autoScale)
        {
            setWidth(this.gameObject.transform.scale.x);
            setHeight(this.gameObject.transform.scale.y);
        }
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();
        recalculateColliders();
    }
}
