package PhysicsSystem;

import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import PhysicsSystem.PhysicsComponents.Collider.*;
import PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import PhysicsSystem.PhysicsManagers.ColliderManager;
import SceneSystem.Scene;
import Utils.DefaultValues;
import Utils.Logger;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;

// - - - interface to communicate with Box2D
public class PhysicsSystemManager
{
    private Scene owner;
    private float fixedDelta = DefaultValues.PHYSICS_DELTA_TIME;
    public PhysicsWorld rawWorld;


    // - - - | Functions | - - -

    public PhysicsSystemManager(Scene SCENE, PhysicsWorld WORLD)
    {
        this.owner = SCENE;
        rawWorld = WORLD;
        Logger.FORGE_LOG_INFO("Created new Physics System Manager for : " + SCENE);
    }

    public PhysicsSystemManager(Scene SCENE, PhysicsWorld WORLD, float FIXED_DELTA)
    {
        this.owner = SCENE;
        rawWorld = WORLD;
        this.fixedDelta = FIXED_DELTA;
        Logger.FORGE_LOG_INFO("Created new Physics System Manager for : " + SCENE);
    }

    // - - - Game Objects - - -

    // - - - add
    public void add(GameObject OBJ)
    {
        Logger.FORGE_LOG_DEBUG("Registering: " + OBJ + " with physics system");
        RigidBodyComponent rb = OBJ.getComponent(RigidBodyComponent.class);
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

            CircleColliderComponent circleCollider = OBJ.getComponent(CircleColliderComponent.class);
            BoxColliderComponent boxCollider = OBJ.getComponent(BoxColliderComponent.class);
            CylinderColliderComponent pillCollider = OBJ.getComponent(CylinderColliderComponent.class);
            PolygonColliderComponent polygonColliderComponent = OBJ.getComponent(PolygonColliderComponent.class);
            EdgeColliderComponent edgeColliderComponent = OBJ.getComponent(EdgeColliderComponent.class);

            if (circleCollider != null)
            {
                ColliderManager.addCircleCollider(rb, circleCollider);
                body.resetMassData();
            }
            if (boxCollider != null)
            {
                ColliderManager.addBoxCollider(rb, boxCollider);
                body.resetMassData();
            }
            if (pillCollider != null)
            {
                ColliderManager.addCylinderCollider(rb, pillCollider);
                body.resetMassData();
            }
            if (polygonColliderComponent != null)
            {
                ColliderManager.addPolygonCollider(rb, polygonColliderComponent);
                body.resetMassData();
            }
            if (edgeColliderComponent != null)
            {
                ColliderManager.addEdgeCollider(rb, edgeColliderComponent);
                body.resetMassData();
            }
        }
        else
        {
            Logger.FORGE_LOG_ERROR("Cannot register : " + OBJ + " with Physics System : null game object");
        }
    }

    // - - - destroy
    public void destroyGameObject(GameObject GO)
    {
        Logger.FORGE_LOG_DEBUG("Removing: " + GO + " from physics system");
        RigidBodyComponent rb = GO.getComponent(RigidBodyComponent.class);
        if (rb != null)
        {
            if (rb.getRawBody() != null)
            {
                rawWorld.getWorld().destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
        else
        {
            Logger.FORGE_LOG_ERROR("Cannot remove : " + GO + " from physics system: null raw physics body");
        }
    }

    // - - - update
    public void update(float DELTA_TIME)
    {
        // - - - ensure that update happens only every 16 milliseconds. We increase only when we pass 16ms
        rawWorld.step(DELTA_TIME);
    }

    public void update()
    {
        // - - - ensure that update happens only every 16 milliseconds. We increase only when we pass 16ms
        rawWorld.step(this.fixedDelta);
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