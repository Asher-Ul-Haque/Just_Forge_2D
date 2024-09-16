package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsManagers.ColliderManager;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class CylinderColliderComponent extends Component {
    private transient CircleColliderComponent topCircle = new CircleColliderComponent();
    private transient CircleColliderComponent bottomCircle = new CircleColliderComponent();
    private transient BoxColliderComponent box = new BoxColliderComponent();
    private transient boolean resetFixtureNextFrame = false;
    private boolean autoScale = true;
    private transient boolean useCollisionColor = false;
    private boolean debugDrawAtRuntime = false;
    private Vector4f hitboxColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    private Vector4f collisionColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
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
        if (debugDrawAtRuntime)
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
        float rotation = this.gameObject.transform.rotation;

        Vector3f color = useCollisionColor
                ? new Vector3f(collisionColor.x, collisionColor.y, collisionColor.z)
                : new Vector3f(hitboxColor.x, hitboxColor.y, hitboxColor.z);

        Vector2f topOffset = rotateVector(topCircle.getOffset(), rotation);
        Vector2f topCenter = new Vector2f(this.gameObject.transform.position).add(topOffset);
        DebugPencil.addCircle(topCenter, this.topCircle.getRadius(), color);

        Vector2f bottomOffset = rotateVector(bottomCircle.getOffset(), rotation);
        Vector2f bottomCenter = new Vector2f(this.gameObject.transform.position).add(bottomOffset);
        DebugPencil.addCircle(bottomCenter, this.bottomCircle.getRadius(), color);

        Vector2f boxCenter = new Vector2f(this.gameObject.transform.position);
        DebugPencil.addBox(boxCenter, this.box.getHalfSize(), this.gameObject.transform.rotation, color);
    }

    private Vector2f rotateVector(Vector2f VECTOR, float ANGLE)
    {
        float cos = (float) Math.cos(ANGLE);
        float sin = (float) Math.sin(ANGLE);

        float x = VECTOR.x * cos - VECTOR.y * sin;
        float y = VECTOR.x * sin + VECTOR.y * cos;

        return new Vector2f(x, y);
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
}
