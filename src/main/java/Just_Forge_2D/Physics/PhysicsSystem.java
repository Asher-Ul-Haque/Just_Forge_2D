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
import org.jbox2d.dynamics.*;
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
            bodyDef.userData = rb.gameObject;
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

            Body body = this.world.createBody(bodyDef);
            CircleColliderComponent circleCollider = OBJ.getCompoent(CircleColliderComponent.class);
            BoxColliderComponent boxCollider = OBJ.getCompoent(BoxColliderComponent.class);

            /*
            if (circleCollider != null)
            {
                shape.setRadius(circleCollider.getRadius());
            }
            */
            if (boxCollider != null)
            {
                addBoxCollider(rb, boxCollider);
            }

            rb.setRawBody(body);
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
        if (physicsTime > 0.0f)
        {
            physicsTime -= physicsTimeDelta;
            world.step(physicsTimeDelta, velocityIterations, positionIterations);
        }
    }

    public void addBoxCollider(RigidBodyComponent RB, BoxColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_WARNING("Raw body must not be null, while adding a collider");
            return;
        }
        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(COLLIDER.getHalfSize()).mul(0.5f); // Dont ask me why half. I fine tuned in testing
        Vector2f offset = COLLIDER.getOffset();
        Vector2f origin = new Vector2f(COLLIDER.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
//        fixtureDef.friction = RB.getLinearDamping();
        fixtureDef.userData = COLLIDER.gameObject;
        // fixtureDef.isSensor = RB.isSensor();
        body.createFixture(fixtureDef);
    }


    // - - - Raycast

    public RayCastInfo rayCast(GameObject REQUESTEE, Vector2f POINT_1, Vector2f POINT_2)
    {
        RayCastInfo callback = new RayCastInfo(REQUESTEE);
        world.raycast(callback, POINT_1, POINT_2);
    }
}