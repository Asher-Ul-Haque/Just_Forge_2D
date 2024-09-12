package Just_Forge_2D.PhysicsSystem;

import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CylinderColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;

// - - - interface to communicate with Box2D
public class PhysicsSystemManager
{
    // - - - private variables - - -

    // - - - world settings

    private Scene owner;
    public PhysicsWorld rawWorld;


    // - - - | Functions | - - -

    public PhysicsSystemManager(Scene SCENE, PhysicsWorld WORLD)
    {
        this.owner = SCENE;
        rawWorld = WORLD;
    }

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

            Body body = rawWorld.getWorld().createBody(bodyDef);
            rb.setRawBody(body);

            CircleColliderComponent circleCollider = OBJ.getCompoent(CircleColliderComponent.class);
            BoxColliderComponent boxCollider = OBJ.getCompoent(BoxColliderComponent.class);
            CylinderColliderComponent pillCollider = OBJ.getCompoent(CylinderColliderComponent.class);

            if (circleCollider != null)
            {
                addCircleCollider(rb, circleCollider);
                body.resetMassData();
            }
            if (boxCollider != null)
            {
                addBoxCollider(rb, boxCollider);
                body.resetMassData();
            }
            if (pillCollider != null)
            {
                addCylinderCollider(rb, pillCollider);
                body.resetMassData();
            }

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
                rawWorld.getWorld().destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    // - - - update
    public void update(float DELTA_TIME)
    {
        // - - - ensure that update happens only every 16 milliseconds. We increase only when we pass 16ms
        rawWorld.step(DELTA_TIME);
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
        fixtureDef.friction = RB.getFrictionCoefficient();
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
        fixtureDef.friction = RB.getFrictionCoefficient();
        fixtureDef.restitution = RB.getRestitutionCoefficient();
        fixtureDef.userData = COLLIDER.gameObject;
        fixtureDef.isSensor = RB.isSensor();
        body.createFixture(fixtureDef);
    }

    public void addCylinderCollider(RigidBodyComponent RB, CylinderColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_WARNING("Raw body must not be null, while adding a collider");
            return;
        }

        addBoxCollider(RB, COLLIDER.getBox());
        addCircleCollider(RB, COLLIDER.getTopCircle());
        addCircleCollider(RB, COLLIDER.getBottomCircle());
    }


    // - - - Ray cast

    public RayCastInfo rayCast(GameObject REQUESTEE, Vector2f POINT_1, Vector2f POINT_2)
    {
        RayCastInfo callback = new RayCastInfo(REQUESTEE);
        rawWorld.getWorld().raycast(callback, new Vec2(POINT_1.x, POINT_1.y), new Vec2(POINT_2.x, POINT_2.y));
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

    public void resetCylinderCollider(RigidBodyComponent RB, CylinderColliderComponent COLLIDER)
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

        addCylinderCollider(RB, COLLIDER);
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

    public boolean isLocked()
    {
        return rawWorld.getWorld().isLocked();
    }
}