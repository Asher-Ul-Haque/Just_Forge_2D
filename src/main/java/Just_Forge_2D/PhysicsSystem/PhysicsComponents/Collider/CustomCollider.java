package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.Icons;
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


    protected void debugDraw(GameObject OBJ, Vector3f COLOR)
    {
        int vertexCount = this.vertices.size();
        if (vertexCount < 3) return;

        // - - - Transform the vertices based on the GameObject's position, rotation, and scale
        ArrayList<Vector2f> transformedVertices = new ArrayList<>();
        for (Vector2f vertex : this.vertices)
        {
            Vector2f scaledVertex = new Vector2f(vertex);
            ForgeMath.rotate(scaledVertex, OBJ.transform.rotation, new Vector2f());
            scaledVertex.add(OBJ.transform.position);

            transformedVertices.add(scaledVertex);
        }

        // - - - Draw lines between consecutive vertices
        for (int i = 0; i < transformedVertices.size(); i++)
        {
            Vector2f start = transformedVertices.get(i);
            Vector2f end = transformedVertices.get((i + 1) % transformedVertices.size()); // Wrap around to the first vertex
            DebugPencil.addLine(start, end, COLOR);
        }
    }



    @Override
    public void editorGUI()
    {
        for (int i = 0; i < vertices.size(); ++i)
        {
            Vector2f copy = new Vector2f(vertices.get(i));
            Widgets.drawVec2Control(Icons.Crosshairs  +"  Vertex: " + (i + 1), copy);
            setVertex(i, copy);
        }


        if (Widgets.button(Icons.Undo + "  Reset To Regular Polygon##"  + toString()))
        {
            resetToRegularPolygon(vertices.size());
        }

        ArrayList<Vector2f> previousVertices = new ArrayList<>(this.vertices);
        if (vertices.size() < 8 && Widgets.button(Icons.PlusCircle + " Add Vertex##" + this.toString()))
        {
            resetToRegularPolygon(vertices.size() + 1);
            for (int i = 0; i < previousVertices.size(); ++i)
            {
                setVertex(i, previousVertices.get(i));
            }
        }

        if (vertices.size() > 3)
        {
            ImGui.sameLine();
            if (Widgets.button(Icons.MinusCircle + " Remove Vertex##" + toString()))
            {
                resetToRegularPolygon(vertices.size() - 1);
                for (int i = 0; i < vertices.size() - 1; ++i)
                {
                    setVertex(i, previousVertices.get(i));
                }
            }
        }
    }

    public void resetToRegularPolygon(int SIDES)
    {
        vertices.clear();
        float radius = 1.0f;
        float angleIncrement = (float) (2 * Math.PI / SIDES);

        for (int i = 0; i < SIDES; i++)
        {
            float angle = i * angleIncrement;
            vertices.add(new Vector2f((float) Math.cos(angle) * radius, (float) Math.sin(angle) * radius));
        }
        Logger.FORGE_LOG_INFO("Shape reset to a regular " + SIDES + "-sided polygon.");
    }
}