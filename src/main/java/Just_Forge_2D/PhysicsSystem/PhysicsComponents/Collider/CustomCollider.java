package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

public class CustomCollider
{
    private Vector4f collisionColor = new Vector4f();
    private Vector4f hitboxColor = new Vector4f();
    private boolean useCollisionColor = false;

    public CustomCollider(List<Vector2f> INITIAL_VERTICES)
    {
        vertices.addAll(INITIAL_VERTICES);
    }

    protected List<Vector2f> vertices = new ArrayList<>();

    public boolean addVertex(Vector2f VERTEX)
    {
        vertices.add(VERTEX);
        if (!isConvex())
        {
            vertices.remove(vertices.size() - 1);
            Logger.FORGE_LOG_ERROR("Adding this vertex would make the shape non-convex");
            return false;
        }
        return true;
    }

    public boolean setVertex(int INDEX, Vector2f VERTEX)
    {
        if (INDEX >= 0 && INDEX < vertices.size())
        {
            Vector2f oldVertex = vertices.get(INDEX);
            vertices.set(INDEX, VERTEX);
            if (!isConvex())
            {
                vertices.set(INDEX, oldVertex);
                Logger.FORGE_LOG_ERROR("Setting this vertex would make the shape non-convex");
                return false;
            }
            return true;
        }
        Logger.FORGE_LOG_ERROR("Index out of bounds for vertices. Length : " + vertices.size() + " Index: " + INDEX);
        return false;
    }

    public List<Vector2f> getVertices()
    {
        return vertices;
    }

    private boolean isConvex()
    {
        if (vertices.size() < 3) return false;

        boolean isPositive = false;
        boolean firstCheck = true;

        int n = vertices.size();
        for (int i = 0; i < n; ++i)
        {
            Vector2f a = vertices.get(i);
            Vector2f b = vertices.get((i + 1) % n);
            Vector2f c = vertices.get((i + 2) % n);

            // - - - Vector ab and bc
            Vector2f ab = new Vector2f(b).sub(a);
            Vector2f bc = new Vector2f(c).sub(b);

            // - - - Cross product of ab and bc
            float cross = ForgeMath.cross(ab, bc);

            // - - - Determine the sign
            if (firstCheck)
            {
                isPositive = cross > 0f;
                firstCheck = false;
            }
            else if ((cross > 0) != isPositive) return false;
        }
        return true;
    }


    protected void debugDraw(PolygonShape polygonShape, GameObject gameObject)
    {
        if (polygonShape == null) return;
        ArrayList<Vector2f> display = new ArrayList<>();

        // Use m_count to determine the actual number of vertices in the polygon shape
        int vertexCount = polygonShape.m_count;
        if (vertexCount < 3) return;

        // Get the position, scale, and rotation of the game object
        Vector2f position = gameObject.transform.position;
        float rotation = gameObject.transform.rotation; // in radians, assuming it's in the right format

        // Iterate only through the actual vertices count
        for (int i = 0; i < vertexCount; i++) {
            Vec2 v = polygonShape.m_vertices[i];
            // Apply the GameObject's transform to the vertex
            Vector2f transformedVertex = new Vector2f(v.x, v.y).add(position);
            display.add(transformedVertex);
        }

        // Call the debug drawing function with the transformed vertices
        DebugPencil.addPolygonFromVertices(display, rotation);
    }



    protected void beginCollision()
    {
        useCollisionColor = true;
    }

    protected void endCollision()
    {
        useCollisionColor = false;
    }
}
