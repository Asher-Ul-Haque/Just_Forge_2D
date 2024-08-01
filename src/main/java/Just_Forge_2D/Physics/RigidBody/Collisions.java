package Just_Forge_2D.Physics.RigidBody;

import Just_Forge_2D.Physics.Primitives.Circle;
import org.joml.Vector2f;

public class Collisions
{
    public static CollisionManifold findCollisionFeatures(Circle A, Circle B)
    {
        CollisionManifold result = new CollisionManifold();
        float sumRadii = A.getRadius() + B.getRadius();
        Vector2f distance = new Vector2f(B.getCenter()).sub(A.getCenter());
        if (distance.lengthSquared() - (sumRadii * sumRadii) > 0)
        {
            return result;
        }

        // - - - multiply by 0.5f to seperate each circle the same amount. Consider updating to factor in momentum and velocity
        float depth = Math.abs(distance.length() - sumRadii) * 0.5f;
        Vector2f normal = new Vector2f(distance);
        normal.normalize();
        float distanceToPoint = A.getRadius() - depth;
        Vector2f contactPoint = new Vector2f(A.getCenter()).add(new Vector2f(normal).mul(distanceToPoint));
        result =  new CollisionManifold(normal, depth);
        result.addContactPoint(contactPoint);
        return result;
    }
}
