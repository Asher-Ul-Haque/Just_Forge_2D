package Just_Forge_2D.PhysicsSystem;

import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CylinderColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsManagers.ColliderManager;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.particle.ParticleSystem;

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

    public ParticleSystem particleSystem;
}