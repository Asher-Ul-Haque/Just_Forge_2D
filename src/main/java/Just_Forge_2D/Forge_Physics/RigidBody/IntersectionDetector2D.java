package Just_Forge_2D.Forge_Physics.RigidBody;

import Just_Forge_2D.Forge_Physics.Primitives.AABB;
import Just_Forge_2D.Forge_Physics.Primitives.Box;
import Just_Forge_2D.Forge_Physics.Primitives.Circle;
import Just_Forge_2D.Renderer.Line2D;
import org.joml.Vector2f;

public class IntersectionDetector2D
{
    public static boolean pointOnLine(Vector2f POINT, Line2D LINE)
    {
        float dy = LINE.getTo().y - LINE.getFrom().y;
        float dx = LINE.getTo().x - LINE.getFrom().x;
        float m = dy / dx;
        float b = LINE.getTo().y - (m * LINE.getTo().x);

        return POINT.y == m * POINT.x + b;
    }

    public static boolean pointInCircle(Vector2f POINT, Circle CIRCLE)
    {
        Vector2f center = CIRCLE.getCenter();
        Vector2f lineFromCenterToPoint = new Vector2f(POINT).sub(center);
        return lineFromCenterToPoint.lengthSquared() <= CIRCLE.getRadius() * CIRCLE.getRadius();
    }

    public static boolean pointInAABB(Vector2f POINT, AABB BOX)
    {
        Vector2f min = BOX.getMin();
        Vector2f max = BOX.getMax();
        return POINT.x <= max.x && min.x <= POINT.x && POINT.y <= max.y && min.y <= POINT.y;
    }

    public static boolean pointInBox(Vector2f POINT, Box BOX)
    {
        // TODO: implement this
        return false;
    }
}
