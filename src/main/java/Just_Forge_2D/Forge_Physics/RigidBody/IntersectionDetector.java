package Just_Forge_2D.Forge_Physics.RigidBody;

import Just_Forge_2D.Forge_Physics.Primitives.*;
import Just_Forge_2D.Utils.ForgeMath;
import org.joml.Vector2f;


public class IntersectionDetector
{
    public static boolean pointOnLine(Vector2f POINT, Line LINE)
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

    public static boolean lineAndCircle(Line LINE, Circle CIRCLE)
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

    public static boolean lineAndAABB(Line LINE, AABB BOX)
    {
        if (pointInAABB(LINE.getFrom(), BOX) || pointInAABB(LINE.getTo(), BOX))
        {
            return true;
        }

        Vector2f unitVector = new Vector2f(LINE.getTo()).sub(LINE.getFrom());
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y) != 0 ? 1.0f / unitVector.y : 0f;

        Vector2f min = BOX.getMin();
        min.sub(LINE.getFrom()).mul(unitVector);

        Vector2f max = BOX.getMax();
        max.sub(LINE.getFrom()).mul(unitVector);

        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tMax <0 || tMin > tMax)
        {
            return false;
        }

        float t = (tMin < 0f) ? tMax : tMin;
        return t > 0f && t * t < LINE.lengthSquared();
    }

    public static boolean lineAndBox(Line LINE, Box BOX)
    {
        float theta = -BOX.getRigidBody().getRotation();

        Vector2f center = BOX.getRigidBody().getPosition();
        Vector2f localStart = new Vector2f(LINE.getFrom());
        Vector2f localEnd = new Vector2f(LINE.getTo());

        ForgeMath.rotate(localStart, theta, center);
        ForgeMath.rotate(localEnd, theta, center);

        Line localLine = new Line(localStart, localEnd);
        AABB aabb = new AABB(BOX.getMin(), BOX.getMax());

        return lineAndAABB(localLine, aabb);
    }

    public static boolean raycast(Circle CIRLCE, Ray RAY, RayCastResult RESULT)
    {
        RayCastResult.reset(RESULT);
        Vector2f originToCirlce = new Vector2f(CIRLCE.getCenter()).sub(RAY.getOrigin());
        float radiusSquared = CIRLCE.getRadius();
        float originToCircleLengthSquared = originToCirlce.lengthSquared();

        // - - - project the vector from ray origin onto the direction of the ray
        float a = originToCirlce.dot(RAY.getDirection());
        float bSq = originToCircleLengthSquared - (a * a);
        if (radiusSquared - bSq < 0.0f)
        {
            return false;
        }

        float f = (float)Math.sqrt(radiusSquared - bSq);
        float t = 0;
        if (originToCircleLengthSquared < radiusSquared)
        {
            // - - - ray starts inside the circle
            t = a + f;
        }
        else
        {
            t = a - f;
        }

        if (RESULT != null)
        {
            Vector2f point = new Vector2f(RAY.getOrigin()).add(new Vector2f(RAY.getDirection()).mul(t));
            Vector2f normal = new Vector2f(point).sub(CIRLCE.getCenter());
            normal.normalize();

            RESULT.init(point, normal, t, true);
        }
        return true;
    }

    public static boolean raycast(AABB BOX, Ray RAY, RayCastResult RESULT)
    {
        RayCastResult.reset(RESULT);
        Vector2f unitVector = RAY.getDirection();
        unitVector.normalize();
        unitVector.x = (unitVector.x != 0) ? 1.0f / unitVector.x : 0f;
        unitVector.y = (unitVector.y) != 0 ? 1.0f / unitVector.y : 0f;

        Vector2f min = BOX.getMin();
        min.sub(RAY.getOrigin()).mul(unitVector);

        Vector2f max = BOX.getMax();
        max.sub(RAY.getOrigin()).mul(unitVector);

        float tMin = Math.max(Math.min(min.x, max.x), Math.min(min.y, max.y));
        float tMax = Math.min(Math.max(min.x, max.x), Math.max(min.y, max.y));
        if (tMax <0 || tMin > tMax)
        {
            return false;
        }

        float t = (tMin < 0f) ? tMax : tMin;
        boolean hit = t > 0f;
        if (!hit)
        {
            return false;
        }

        if (RESULT != null)
        {
            Vector2f point = new Vector2f(RAY.getOrigin()).add(new Vector2f(RAY.getDirection())).mul(t);
            Vector2f normal = new Vector2f(RAY.getOrigin()).sub(point);
            normal.normalize();

            RESULT.init(point, normal, t, true);
        }
        return true;
    }


    public static boolean raycast(Box BOX, Ray RAY, RayCastResult RESULT)
    {
        RayCastResult.reset(RESULT);

        Vector2f size = BOX.getHalfSize();
        Vector2f xAxis = new Vector2f(1, 0);
        Vector2f yAxis = new Vector2f(0, 1);
        ForgeMath.rotate(xAxis, -BOX.getRigidBody().getRotation(), new Vector2f(0, 0));
        ForgeMath.rotate(yAxis, -BOX.getRigidBody().getRotation(), new Vector2f(0, 0));

        Vector2f p = new Vector2f(BOX.getRigidBody().getPosition()).sub(new Vector2f(RAY.getOrigin()));

        // - - - project the direction of the ray onto each axis of the box
        Vector2f f = new Vector2f(xAxis.dot(RAY.getDirection()), yAxis.dot(RAY.getDirection()));

        // - - - project p onto every axis of the box
        Vector2f e = new Vector2f(xAxis.dot(p), yAxis.dot(p));

        float[] tArray = {0, 0, 0, 0};
        for (int i = 0; i < 2; ++i)
        {
            if (ForgeMath.compare(f.get(i), 0))
            {
                // - - - if the ray is parallele to the current axis and the origin of the ray is not inside, we have no hit
                if (-e.get(i) - size.get(i) < 0 || -e.get(i) < 0)
                {
                    return false;
                }
                f.setComponent(i, 0.0001f); // realy small to avoid division by 0
            }
            tArray[i * 2 + 0] = (e.get(i) + size.get(i)) / f.get(i); // tMax
            tArray[i * 2 + 1] = (e.get(i) - size.get(i)) / f.get(i); // tMin
        }

        float tMin = Math.max(Math.min(tArray[0], tArray[1]), Math.min(tArray[2], tArray[3]));
        float tMax = Math.min(Math.max(tArray[0], tArray[1]), Math.max(tArray[2], tArray[3]));

        float t = (tMin < 0f) ? tMax : tMin;
        boolean hit = t > 0f;
        if (!hit)
        {
            return false;
        }
        if (RESULT != null)
        {
            Vector2f point = new Vector2f(RAY.getOrigin()).add(new Vector2f(RAY.getDirection()).mul(t));
            Vector2f normal = new Vector2f(RAY.getOrigin()).sub(point);
            normal.normalize();

            RESULT.init(point, normal, t, true);
        }
        return true;
    }

    public static boolean circleAndLine(Circle CIRCLE, Line LINE)
    {
        return lineAndCircle(LINE, CIRCLE);
    }

    public static boolean circleAndCircle(Circle CIRCLE_1, Circle CIRCLE_2)
    {
        Vector2f vecBetweenCenters = new Vector2f(CIRCLE_1.getCenter()).sub(new Vector2f(CIRCLE_2.getCenter()));
        float radiiSum = CIRCLE_1.getRadius() + CIRCLE_2.getRadius();
        return  vecBetweenCenters.lengthSquared() <= radiiSum * radiiSum;
    }

    public static boolean circleAndAABB(Circle CIRCLE, AABB BOX)
    {
        Vector2f min = BOX.getMin();
        Vector2f max = BOX.getMax();

        Vector2f closestPointToCirlce = new Vector2f(CIRCLE.getCenter());
        if (closestPointToCirlce.x < min.x)
        {
            closestPointToCirlce.x = min.x;;
        }
        else if (closestPointToCirlce.x > max.x)
        {
            closestPointToCirlce.x = max.x;
        }

        if (closestPointToCirlce.y < min.y)
        {
            closestPointToCirlce.y = min.y;
        }
        else if (closestPointToCirlce.y > max.y)
        {
            closestPointToCirlce.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(CIRCLE.getCenter()).sub(closestPointToCirlce);
        return circleToBox.lengthSquared() <= CIRCLE.getRadius() * CIRCLE.getRadius();
    }

    public static boolean circleAndBox2D(Circle CIRCLE, Box BOX)
    {
        // treat box as AABB after rotating
        Vector2f min = new Vector2f();
        Vector2f max = new Vector2f(BOX.getHalfSize()).mul(2.0f);

        // - - - create a circle in box local space
        Vector2f r = new Vector2f(CIRCLE.getCenter()).sub(BOX.getRigidBody().getPosition());
        ForgeMath.rotate(r, -BOX.getRigidBody().getRotation(), new Vector2f(0f, 0f));
        Vector2f localCirclePOs = new Vector2f(r).add(BOX.getHalfSize());

        Vector2f closestPointToCirlce = new Vector2f(localCirclePOs);
        if (closestPointToCirlce.x < min.x)
        {
            closestPointToCirlce.x = min.x;;
        }
        else if (closestPointToCirlce.x > max.x)
        {
            closestPointToCirlce.x = max.x;
        }

        if (closestPointToCirlce.y < min.y)
        {
            closestPointToCirlce.y = min.y;
        }
        else if (closestPointToCirlce.y > max.y)
        {
            closestPointToCirlce.y = max.y;
        }

        Vector2f circleToBox = new Vector2f(localCirclePOs).sub(closestPointToCirlce);
        return circleToBox.lengthSquared() <= CIRCLE.getRadius() * CIRCLE.getRadius();
    }

    public static boolean AABBandCircle(AABB BOX, Circle CIRCLE)
    {
        return circleAndAABB(CIRCLE, BOX);
    }

    public static boolean AABBandAABB(AABB BOX_1, AABB BOX_2)
    {
        // NOTE: axis aligned on x and y
        Vector2f[] axesToTest = {new Vector2f(0, 1), new Vector2f(1, 0)};
        for (Vector2f axis : axesToTest)
        {
            if (!overlapOnAxis(BOX_1, BOX_2, axis))
            {
                return false;
            }
        }
        return true;
    }

    public static boolean AABBandBOX(AABB BOX_1, Box BOX_2)
    {
        Vector2f[] axesToTest = {
                new Vector2f(0, 1), new Vector2f(1, 0),
                new Vector2f(0, 1), new Vector2f(1, 0)
        };
        ForgeMath.rotate(axesToTest[2], BOX_2.getRigidBody().getRotation(), new Vector2f(0, 0));
        ForgeMath.rotate(axesToTest[3], BOX_2.getRigidBody().getRotation(), new Vector2f(0, 0));
        for (Vector2f axis : axesToTest)
        {
            if (!overlapOnAxis(BOX_1, BOX_2, axis))
            {
                return false;
            }
        }
        return true;
    }

    private static Vector2f getInterval(AABB RECTANGLE, Vector2f AXIS)
    {
        Vector2f result = new Vector2f(0, 0);
        Vector2f min = RECTANGLE.getMin();
        Vector2f max = RECTANGLE.getMax();

        Vector2f[] vertices = {
                new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
                new Vector2f(max.x, min.y), new Vector2f(max.x, max.y)
        };

        result.x = AXIS.dot(vertices[0]);
        result.y = result.x;

        for (int i = 1; i < 4; i++)
        {
            float projection = AXIS.dot(vertices[i]);
            result.x = Math.min(result.x, projection);
            result.y = Math.max(result.y, projection);
        }
        return result;
    }

    private static boolean overlapOnAxis(AABB BOX_1, AABB BOX_2, Vector2f AXIS)
    {
        // - - - NOTE: Assuming axis is a unit vector
        Vector2f interval = getInterval(BOX_1, AXIS);
        Vector2f interval2 = getInterval(BOX_2, AXIS);
        return ((interval2.x <= interval.y) && (interval.x <= interval2.y));
    }

    private static boolean overlapOnAxis(AABB BOX_1, Box BOX_2, Vector2f AXIS)
    {
        // - - - NOTE: Assuming axis is a unit vector
        Vector2f interval = getInterval(BOX_1, AXIS);
        Vector2f interval2 = getInterval(BOX_2, AXIS);
        return ((interval2.x <= interval.y) && (interval.x <= interval2.y));
    }

    private static boolean overlapOnAxis(Box BOX_1, Box BOX_2, Vector2f AXIS)
    {
        // - - - NOTE: Assuming axis is a unit vector
        Vector2f interval = getInterval(BOX_1, AXIS);
        Vector2f interval2 = getInterval(BOX_2, AXIS);
        return ((interval2.x <= interval.y) && (interval.x <= interval2.y));
    }

    private static Vector2f getInterval(Box RECTANGLE, Vector2f AXIS)
    {
        Vector2f result = new Vector2f(0, 0);

        Vector2f[] vertices = RECTANGLE.getVertices();

        result.x = AXIS.dot(vertices[0]);
        result.y = result.x;

        for (int i = 1; i < 4; i++)
        {
            float projection = AXIS.dot(vertices[i]);
            result.x = Math.min(result.x, projection);
            result.y = Math.max(result.y, projection);
        }
        return result;
    }
}