package Just_Forge_2D.PhysicsSystem.PhysicsManagers;

import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.*;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.EdgeShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.joml.Vector2f;

public class ColliderManager
{
    public static PolygonShape debugShape;
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

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = RB.getDensity();
        fixtureDef.friction = RB.getFrictionCoefficient();
        fixtureDef.userData = COLLIDER.gameObject;
        fixtureDef.isSensor = RB.isSensor();
        body.createFixture(fixtureDef);
    }

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

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = RB.getDensity();
        fixtureDef.friction = RB.getFrictionCoefficient();
        fixtureDef.restitution = RB.getRestitutionCoefficient();
        fixtureDef.userData = COLLIDER.gameObject;
        fixtureDef.isSensor = RB.isSensor();
        body.createFixture(fixtureDef);
    }

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

    public static void resetCircleCollider(RigidBodyComponent RB, CircleColliderComponent COLLIDER)
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

    public static void resetBoxCollider(RigidBodyComponent RB, BoxColliderComponent COLLIDER)
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

    public static void resetCylinderCollider(RigidBodyComponent RB, CylinderColliderComponent COLLIDER)
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

    public static void addCustomCollider(RigidBodyComponent RB, CustomColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null)
        {
            Logger.FORGE_LOG_WARNING("Raw body must not be null while adding a collider");
            return;
        }

        for (CustomCollider collider : COLLIDER.getColliders())
        {
            EdgeShape shape = new EdgeShape();
            shape.
            PolygonShape shape = new PolygonShape();
            Vec2[] physicsVertices = new Vec2[collider.getVertices().size()];
            physicsVertices[0] = new Vec2(collider.getVertices().get(0).x, collider.getVertices().get(0).y);
            physicsVertices[1] = new Vec2(collider.getVertices().get(1).x, collider.getVertices().get(1).y);
            physicsVertices[2] = new Vec2(collider.getVertices().get(2).x, collider.getVertices().get(2).y);

            shape.set(physicsVertices, physicsVertices.length);
            debugShape = shape;

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = shape;
            fixtureDef.density = 1.0f;
            fixtureDef.friction = RB.getFrictionCoefficient();
            fixtureDef.userData = COLLIDER.gameObject;
            fixtureDef.isSensor = RB.isSensor();
            body.createFixture(fixtureDef);
        }
    }

    public static void resetCustomCollider(RigidBodyComponent RB, CustomColliderComponent COLLIDER)
    {
        Body body = RB.getRawBody();
        if (body == null) {
            Logger.FORGE_LOG_ERROR("Tried to reset Convex Collider on a null physics body");
            return;
        }

        int size = fixtureListSize(body);
        for (int i = 0; i < size; ++i) {
            body.destroyFixture(body.getFixtureList());
        }

        addCustomCollider(RB, COLLIDER);
        body.resetMassData();
    }

    private static int fixtureListSize(Body BODY)
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

    public static PolygonShape getDebugShape()
    {
        return debugShape;
    }
}