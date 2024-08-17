package Just_Forge_2D.EntityComponentSystem.Components.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Forge;
import Just_Forge_2D.WindowSystem.EditorWindow;
import org.joml.Vector2f;

public class CylinderColliderComponent extends Component
{
    private transient CircleColliderComponent topCircle = new CircleColliderComponent();
    private transient CircleColliderComponent bottomCircle = new CircleColliderComponent();
    private transient BoxColliderComponent box = new BoxColliderComponent();
    private transient boolean resetFixtureNextFrame = false;

    public float width = 0.1f;
    public float height = 0.2f;
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
        if (Forge.currentScene.getPhysics().isLocked())
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
                Forge.currentScene.getPhysics().resetCylinderCollider(rb, this);
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
        if (resetFixtureNextFrame)
        {
            resetFixtures();
            resetFixtureNextFrame = false;
        }
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        topCircle.editorUpdate(DELTA_TIME);
        bottomCircle.editorUpdate(DELTA_TIME);
        box.editorUpdate(DELTA_TIME);

        if (resetFixtureNextFrame)
        {
            resetFixtures();
            resetFixtureNextFrame = false;
        }
    }
}
