package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CylinderColliderComponent extends Component
{
    private transient CircleColliderComponent topCircle = new CircleColliderComponent();
    private transient CircleColliderComponent bottomCircle = new CircleColliderComponent();
    private transient BoxColliderComponent box = new BoxColliderComponent();
    private transient boolean resetFixtureNextFrame = false;

    public float width = 0.25f;
    public float height = 0.25f;
    public Vector2f offset = new Vector2f();
    public static boolean showHitboxInMain = false;

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
        float circleRadius = width / 4.0f;
        float boxHeight = height - 2 * circleRadius;
        topCircle.setRadius(circleRadius);
        bottomCircle.setRadius(circleRadius);
        topCircle.setOffset(new Vector2f(new Vector2f(offset).add(0, boxHeight / 4.0f)));
        bottomCircle.setOffset(new Vector2f(new Vector2f(offset).sub(0, boxHeight / 4.0f)));
        box.setHalfSize(new Vector2f(width / 2.0f, boxHeight / 2.0f));
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
                MainWindow.getPhysicsSystem().resetCylinderCollider(rb, this);
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
        if (showHitboxInMain)
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
        DebugPencil.addCircle(center, this.topCircle.getRadius(), new Vector3f(1, 0, 0));
        center = new Vector2f(this.gameObject.transform.position).add(bottomCircle.getOffset());
        DebugPencil.addCircle(center, this.bottomCircle.getRadius(), new Vector3f(1, 0, 0));
        center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        DebugPencil.addBox(center, this.box.getHalfSize(), this.gameObject.transform.rotation, new Vector3f(1, 0, 0));
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
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();
        recalculateColliders();
    }
}
