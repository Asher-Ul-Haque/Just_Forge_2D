package Just_Forge_2D.Forge_Physics.RigidBody;

import Just_Forge_2D.Forge_Physics.Primitives.AABB;
import Just_Forge_2D.Forge_Physics.Primitives.Box;
import Just_Forge_2D.Forge_Physics.Primitives.Circle;
import Just_Forge_2D.Renderer.Line2D;
import Just_Forge_2D.Utils.ForgeMath;
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
        // - - - translate point in locale space
        Vector2f pointLocalBoxSpace = new Vector2f(POINT);
        ForgeMath.rotate(pointLocalBoxSpace, BOX.getRigidBody().getRotation(), BOX.getRigidBody().getPosition());
 
        Vector2f min = BOX.getMin();
        Vector2f max = BOX.getMax();

        return pointLocalBoxSpace.x <= max.x && min.x <= pointLocalBoxSpace.x && pointLocalBoxSpace.y <= max.y && min.y <= pointLocalBoxSpace.y;
    }

    public static boolean lineAndCircle(Line2D LINE, Circle CIRCLE)
    {
        if (pointInCircle(LINE.getFrom(), CIRCLE) || pointInCircle(LINE.getTo(), CIRCLE))
        {
            return true;
        }

        Vector2f ab = new Vector2f(LINE.getTo()).sub(LINE.getFrom());
        Vector2f circleCenter = CIRCLE.getCenter();
        Vector2f centerToLineStart = new Vector2f(circleCenter).sub(LINE.getFrom());
        float t = centerToLineStart.dot(ab) / ab.dot(ab);

        if (t < 0.0f || t > 1.0f)
        {
            return false;
        }

        // - - - find the closest point to the line segment
        Vector2f closestPoint = new Vector2f(LINE.getFrom()).add(ab.mul(t));
        return pointInCircle(closestPoint, CIRCLE);
    }
}
