package Just_Forge_2D.Physics;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.CoreSystems.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.collision.shapes.CircleShape;
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
            bodyDef.gravityScale = rb.gravityScale;
            bodyDef.angularVelocity = rb.angularVelocity;
            bodyDef.linearVelocity = rb.getLinearVelocity();

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
            body.m_mass = rb.getMass();
            CircleColliderComponent circleCollider = OBJ.getCompoent(CircleColliderComponent.class);
            BoxColliderComponent boxCollider = OBJ.getCompoent(BoxColliderComponent.class);

            if (circleCollider != null)
            {
                addCircleCollider(rb, circleCollider);
            }
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
        fixtureDef.friction = RB.getLinearDamping();
        fixtureDef.userData = COLLIDER.gameObject;
        fixtureDef.isSensor = RB.isSensor();
        body.createFixture(fixtureDef);
    }

    public void addCircleCollider(RigidBodyComponent RB, CircleColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_WARNING("Raw body must not be null, while adding a collider");
            return;
        }
        CircleShape shape = new CircleShape();
        shape.setRadius(COLLIDER.getRadius());
        shape.m_p.set(COLLIDER.getOffset().x, COLLIDER.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = RB.getLinearDamping();
        fixtureDef.userData = COLLIDER.gameObject;
        fixtureDef.isSensor = RB.isSensor();
        body.createFixture(fixtureDef);
    }


    // - - - Ray cast

    public RayCastInfo rayCast(GameObject REQUESTEE, Vector2f POINT_1, Vector2f POINT_2)
    {
        RayCastInfo callback = new RayCastInfo(REQUESTEE);
        world.raycast(callback, new Vec2(POINT_1.x, POINT_1.y), new Vec2(POINT_2.x, POINT_2.y));
        return callback;
    }

    public void resetCircleCollider(RigidBodyComponent RB, CircleColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_ERROR("Tried to reset Circle Collider on a null physics body");
            return;
        }

        int size = fixtureListSize(body);
        for (int i = 0; i < size; ++i)
        {
            body.destroyFixture(body.getFixtureList());
        }

        addCircleCollider(RB, COLLIDER);
        body.resetMassData();
    }

    public void resetBoxCollider(RigidBodyComponent RB, BoxColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_ERROR("Tried to reset Circle Collider on a null physics body");
            return;
        }

        int size = fixtureListSize(body);
        for (int i = 0; i < size; ++i)
        {
            body.destroyFixture(body.getFixtureList());
        }

        addBoxCollider(RB, COLLIDER);
        body.resetMassData();
    }

    private int fixtureListSize(Body BODY)
    {
        int size = 0;
        Fixture fixture = BODY.getFixtureList();
        while (fixture != null)
        {
            size++;
            fixture = fixture.m_next;
        }

        return size;
    }

    public void setSensor(RigidBodyComponent RB, boolean REALLY)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_ERROR("Cant set sensor on a null raw body");
            return;
        }
        Fixture fixture = body.getFixtureList();
        while (fixture != null)
        {
            fixture.m_isSensor = REALLY;
            fixture = fixture.m_next;
        }
    }

}