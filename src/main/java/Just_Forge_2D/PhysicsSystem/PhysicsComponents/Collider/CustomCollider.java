package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class CustomCollider extends Component
{
    public CustomCollider(List<Vector2f> INITIAL_VERTICES)
    {
        vertices.addAll(INITIAL_VERTICES);
    }

    protected List<Vector2f> vertices = new ArrayList<>();

    public boolean addVertex(Vector2f VERTEX)
    {
        if (vertices.size() == 8)
        {
            Logger.FORGE_LOG_ERROR("Can only have upto 8 vertices in an Collider");
            return false;
        }
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


    protected void debugDraw(GameObject gameObject, Vector3f color) {
        int vertexCount = this.vertices.size();
        if (vertexCount < 3) return; // Ensure we have a valid polygon

        // Transform the vertices based on the GameObject's position, rotation, and scale
        ArrayList<Vector2f> transformedVertices = new ArrayList<>();
        for (Vector2f vertex : this.vertices) {
            // Apply scale
            Vector2f scaledVertex = new Vector2f(vertex);

            // Rotate around the GameObject's center
            ForgeMath.rotate(scaledVertex, gameObject.transform.rotation, new Vector2f());

            // Apply position
            scaledVertex.add(gameObject.transform.position);

            transformedVertices.add(scaledVertex);
        }

        // Draw lines between consecutive vertices
        for (int i = 0; i < transformedVertices.size(); i++) {
            Vector2f start = transformedVertices.get(i);
            Vector2f end = transformedVertices.get((i + 1) % transformedVertices.size()); // Wrap around to the first vertex

            // Draw the line between the current vertex and the next
            DebugPencil.addLine(start, end, color);
        }
    }



    @Override
    public void editorGUI()
    {
        for (int i = 0; i < vertices.size(); ++i)
        {
            Vector2f copy = new Vector2f(vertices.get(i));
            Widgets.drawVec2Control("Vertex: " + (i + 1), copy);
            setVertex(i, copy);
        }

        if (vertices.size() < 8 && ImGui.button("Add Vertex##" + this.toString()))
        {
            resetToRegularPolygon(vertices.size() + 1);
        }

        if (vertices.size() > 3 && ImGui.button("Remove Vertex##"  + toString()))
        {
            resetToRegularPolygon(vertices.size() - 1);
        }

        if (ImGui.button("Reset To Regular Polygon##"  + toString()))
        {
            resetToRegularPolygon(vertices.size());
        }
    }

    private void resetToRegularPolygon(int sides)
    {
        vertices.clear();
        float radius = 1.0f; // Choose a suitable scale or radius
        float angleIncrement = (float) (2 * Math.PI / sides);

        for (int i = 0; i < sides; i++)
        {
            float angle = i * angleIncrement;
            vertices.add(new Vector2f((float) Math.cos(angle) * radius, (float) Math.sin(angle) * radius));
        }
        Logger.FORGE_LOG_INFO("Shape reset to a regular " + sides + "-sided polygon.");
    }
}