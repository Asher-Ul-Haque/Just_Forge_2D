package Just_Forge_2D.Utils;

import org.joml.Vector2f;

import java.util.List;

public class ForgeMath
{
    public static void rotate(Vector2f VECTOR, float ANGLE_RADIAN, Vector2f ORIGIN)
    {
        float x = VECTOR.x - ORIGIN.x;
        float y = VECTOR.y - ORIGIN.y;

        float cos = (float) Math.cos(ANGLE_RADIAN);
        float sin = (float) Math.sin(ANGLE_RADIAN);

        float xPrime = (x * cos) - (y * sin);
        float yPrime = (x * sin) + (y * cos);

        xPrime += ORIGIN.x;
        yPrime += ORIGIN.y;

        VECTOR.x = xPrime;
        VECTOR.y = yPrime;
    }

    public static boolean compare(float A, float B, float EPSILON)
    {
        return Math.abs(A - B) <= EPSILON * Math.max(Math.abs(A), Math.abs(B));
    }

    public static boolean compare(Vector2f VEC_1, Vector2f VEC_2, float EPSILON)
    {
        return compare(VEC_1.x, VEC_2.x, EPSILON) && compare(VEC_1.y, VEC_2.y, EPSILON);
    }

    public static boolean compare(float A, float B)
    {
        return Math.abs(A - B) <= Float.MIN_VALUE * Math.max(Math.abs(A), Math.abs(B));
    }

    public static boolean compare(Vector2f VEC_1, Vector2f VEC_2)
    {
        return compare(VEC_1.x, VEC_2.x, Float.MIN_VALUE) && compare(VEC_1.y, VEC_2.y, Float.MIN_VALUE);
    }


    public static float lerp(float start, float end, float factor)
    {
        return start + factor * (end - start);
    }

    public static float cross(Vector2f A, Vector2f B)
    {
        return A.x * B.y - A.y * B.x;
    }

    public static Vector2f rotateVertex(Vector2f VERTEX, Vector2f CENTER, float ROTATION)
    {
        Vector2f result = new Vector2f(VERTEX).sub(CENTER);
        ForgeMath.rotate(result, ROTATION, new Vector2f());
        return result.add(CENTER);
    }

    public static Vector2f calculateCentroid(List<Vector2f> VERTICES)
    {
        Vector2f centroid = new Vector2f();
        for (Vector2f vertex : VERTICES)
        {
            centroid.add(vertex);
        }
        return centroid.div(VERTICES.size());
    }

    public static float clamp(float VALUE, float MIN, float MAX)
    {
        return Math.max(MIN, Math.min(MAX, VALUE));
    }

}