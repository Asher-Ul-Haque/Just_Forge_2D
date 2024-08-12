package Just_Forge_2D.Physics;

import Just_Forge_2D.Core.EntityComponentSystem.Components.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.Core.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

// - - - interface to communicate with Box2D
public class PhysicsSystem
{
    // - - - private variables - - -

    // - - - world settings
    private final Vec2 gravity = Configurations.GRAVITY;
    private final World world = new World(gravity);
    private float physicsTime = 0.0f;

    // - - - world precisions
    private float physicsTimeDelta = Configurations.PHYSICS_DELTA_TIME;
    private int velocityIterations = Configurations.VELOCITY_ITERATIONS;
    private int positionIterations = Configurations.POSITION_ITERATIONS;


    // - - - | Functions | - - -


    // - - - Game Objects - - -

    // - - - add
    public void add(GameObject OBJ)
    {
        RigidBodyComponent rb = OBJ.getCompoent(RigidBodyComponent.class);
        if (rb != null && rb.getRawBody() == null)
        {
            TransformComponent transform = OBJ.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = transform.rotation;
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();

            switch (rb.getBodyType())
            {
                case Kinematic:
                    bodyDef.type = BodyType.KINEMATIC;
                    break;

                case Static:
                    bodyDef.type = BodyType.STATIC;
                    break;

                case Dynamic:
                    bodyDef.type = BodyType.DYNAMIC;
                    break;

                default:
                    Logger.FORGE_LOG_ERROR("Bro how did we get here: advancement unlocked");
                    break;
            }

            PolygonShape shape = new PolygonShape();
            CircleColliderComponent circleCollider = OBJ.getCompoent(CircleColliderComponent.class);
            BoxColliderComponent boxCollider = OBJ.getCompoent(BoxColliderComponent.class);

            if (circleCollider != null)
            {
                shape.setRadius(circleCollider.getRadius());
            }
            else if (boxCollider != null)
            {
                Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f); // Dont ask me why half. I fine tuned in testing
                Vector2f offset = boxCollider.getOffset();
                Vector2f origin = new Vector2f(boxCollider.getOrigin());
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;
                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);
            rb.setRawBody(body);
            body.createFixture(shape, rb.getMass());

            Logger.FORGE_LOG_DEBUG("Linked Box2D with " + OBJ);
        }
    }

    // - - - destroy
    public void destroyGameObject(GameObject GO)
    {
        Logger.FORGE_LOG_DEBUG("Unlinked Box2D with " + GO);
        RigidBodyComponent rb = GO.getCompoent(RigidBodyComponent.class);
        if (rb != null)
        {
            if (rb.getRawBody() != null)
            {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    // - - - update
    public void update(float DELTA_TIME)
    {
        // - - - ensure that update happens only every 16 milliseconds. We increase only when we pass 16ms
        physicsTime += DELTA_TIME;
        if (physicsTime >= 0.0f)
        {
            physicsTime -= physicsTimeDelta;
            world.step(physicsTimeDelta, velocityIterations, positionIterations);
        }
    }
}