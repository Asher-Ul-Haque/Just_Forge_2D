package Just_Forge_2D.PhysicsSystem.PhysicsManagers;

import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.*;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector2f;

public class ColliderManager
{

    // - - - Box Colliders - - -

    public static void addBoxCollider(RigidBodyComponent RB, BoxColliderComponent COLLIDER)
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

        createFixture(shape, RB, COLLIDER, body);
    }

    public static void resetBoxCollider(RigidBodyComponent RB, BoxColliderComponent COLLIDER)
    {
        resetCollider(RB, COLLIDER, () -> addBoxCollider(RB, COLLIDER));
    }


    // - - - Circle Colliders - - -

    public static void addCircleCollider(RigidBodyComponent RB, CircleColliderComponent COLLIDER)
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

        createFixture(shape, RB, COLLIDER, body);
    }

    public static void resetCircleCollider(RigidBodyComponent RB, CircleColliderComponent COLLIDER)
    {
        resetCollider(RB, COLLIDER, () -> addCircleCollider(RB, COLLIDER));
    }


    // - - - Cylinder Colliders - - -

    public static void addCylinderCollider(RigidBodyComponent RB, CylinderColliderComponent COLLIDER)
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

    public static void resetCylinderCollider(RigidBodyComponent RB, CylinderColliderComponent COLLIDER)
    {
        resetCollider(RB, COLLIDER, () -> addCylinderCollider(RB, COLLIDER));
    }


    // - - - Polygon Colliders - - -

    public static void addPolygonCollider(RigidBodyComponent RB, PolygonColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_WARNING("Raw body must not be null while adding a collider");
            return;
        }

        for (CustomCollider collider : COLLIDER.getColliders())
        {
            PolygonShape shape = new PolygonShape();
            Vec2[] physicsVertices = new Vec2[collider.getVertices().size()];
            for (int i = 0; i < physicsVertices.length; ++i)
            {
                physicsVertices[i] = new Vec2(collider.getVertices().get(i).x, collider.getVertices().get(i).y);
            }

            shape.set(physicsVertices, physicsVertices.length);

            createFixture(shape, RB, COLLIDER, body);

        }
    }

    public static void resetPolygonCollider(RigidBodyComponent RB, PolygonColliderComponent COLLIDER)
    {
        resetCollider(RB, COLLIDER, () -> addPolygonCollider(RB, COLLIDER));
    }


    // - - - Edge Colliders - - -

    public static void addEdgeCollider(RigidBodyComponent RB, EdgeColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_WARNING("Raw body must not be null while adding a collider");
            return;
        }

        for (EdgeCollider collider : COLLIDER.getColliders())
        {
            EdgeShape shape = new EdgeShape();

            shape.set(new Vec2(collider.getEdgeStart().x, collider.getEdgeStart().y), new Vec2(collider.getEdgeEnd().x, collider.getEdgeEnd().y));

            createFixture(shape, RB, COLLIDER, body);
        }
    }

    public static void resetEdgeCollider(RigidBodyComponent RB, EdgeColliderComponent COLLIDER)
    {
        resetCollider(RB, COLLIDER, () -> addEdgeCollider(RB, COLLIDER));
    }


    // - - - Helper Functions - - -

    private static void createFixture(Shape SHAPE, RigidBodyComponent RB, ColliderComponent COLLIDER, Body BODY)
    {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = SHAPE;
        fixtureDef.density = RB.getDensity();
        fixtureDef.friction = RB.getFrictionCoefficient();
        fixtureDef.userData = COLLIDER.gameObject;
        fixtureDef.isSensor = RB.isSensor();
        fixtureDef.filter.categoryBits = COLLIDER.collisionLayer.getCategoryBits();
        fixtureDef.filter.maskBits = COLLIDER.collisionLayer.getMaskBits();
        BODY.createFixture(fixtureDef);
    }

    public static void resetCollider(RigidBodyComponent rb, ColliderComponent collider, Runnable addCollider)
    {
        Body body = rb.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_ERROR("Cannot Reset collider on null body");
            return;
        }
        destroyAllFixtures(body);
        addCollider.run();
        body.resetMassData();
    }

    private static void destroyAllFixtures(Body BODY)
    {
        while (BODY.getFixtureList() != null)
        {
            BODY.destroyFixture(BODY.getFixtureList());
        }
    }
}