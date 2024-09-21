package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugPencil
{
    // - - - Private Variables - - -

    // - - - lines
    private static final int MAX_LINES = DefaultValues.DEBUG_PENCIL_MAX_LINES;
    private static final List<Line> lines = new ArrayList<>();

    // - - - rendering info
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static final Shader shader = AssetPool.getShader("Debug");
    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    // - - - Defaults
    private static final int defaultLifetime = DefaultValues.DEBUG_PENCIL_DEFAULT_LIFE;
    private static final Vector3f defaultColor = DefaultValues.DEBUG_PENCIL_DEFAULT_COLOR;
    private static final int defaultWidth = DefaultValues.DEBUG_PENCIL_DEFAULT_WIDTH;
    private static final int defaultSegments = DefaultValues.DEBUG_PENCIL_DEFAULT_CIRCLE_PRECISION;
    private static final float defaultRotation = DefaultValues.DEBUG_PENCIL_DEFAULT_ROTATION;


    // - - - | Functions | - - -


    // - - - start
    public static void start()
    {
        // - - - Generate the vao
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // - - - Enable the vertex array attributes
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertexArray.length * Float.BYTES, GL_DYNAMIC_DRAW);


        // - - - Enable the vertex array attributes - - -

        // - - - position
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
        glEnableVertexAttribArray(0);

        // - - - color
        glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
        glEnableVertexAttribArray(1);

        // - - - set width
        glLineWidth(defaultWidth);
    }

    // - - - frame start
    public static void beginFrame()
    {
        // - - - make sure started
        if (!started)
        {
            start();
            started = true;
        }

        // - - - remove lines that got timed out
        for (int i = 0; i < lines.size(); ++i)
        {
            if (lines.get(i).beginFrame() < 0)
            {
                lines.remove(i);
                i--;
            }
        }
    }

    public static void draw()
    {
        if (lines.isEmpty()) return;

        int index = 0;
        for (Line line: lines)
        {
            for (int i = 0; i < 2; ++i)
            {
                Vector2f position = i == 0 ? line.getFrom() : line.getTo();
                Vector3f color = line.getColor();

                // - - - load the position
                vertexArray[index] = position.x;
                vertexArray[index + 1] = position.y;
                vertexArray[index + 2] = -10.0f; //this is pointless

                // - - - load the color
                vertexArray[index + 3] = color.x;
                vertexArray[index + 4] = color.y;
                vertexArray[index + 5] = color.z;

                index += 6;
            }
        }

        // - - - bind and upload
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2)); // we don't have maximum number of lines always

        // - - - draw
        shader.use();
        shader.uploadMatrix4f("uProjection", MainWindow.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMatrix4f("uView", MainWindow.getCurrentScene().getCamera().getViewMatrix());

        // - - - bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // - - - draw the batch
        glDrawArrays(GL_LINES, 0, lines.size()); // openGL uses bresenham line algo

        //  - - - Disable the location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // - - - unbind shader
        shader.detach();
    }


    // - - - | Add Stuff | - - -
    // TODO: Add other primitives and other constants for common values like colors


    // - - - Add Lines - - -

    // - - - default
    public static void addLine(Vector2f FROM, Vector2f TO, Vector3f COLOR, int LIFETIME)
    {
        if (lines.size() >= MAX_LINES) return;
        DebugPencil.lines.add(new Line(FROM, TO, COLOR, LIFETIME));
    }

    // - - - no lifetime
    public static void addLine(Vector2f FROM, Vector2f TO, Vector3f COLOR)
    {
        DebugPencil.addLine(FROM, TO, COLOR, defaultLifetime);
    }

    // - - - no color
    public static void addLine(Vector2f FROM, Vector2f TO, int LIFETIME)
    {
        addLine(FROM, TO, defaultColor, LIFETIME);
    }

    // - - - neither color nor lifetime
    public static void addLine(Vector2f FROM, Vector2f TO)
    {
        addLine(FROM, TO, defaultColor, defaultLifetime);
    }


    // - - - Add Box - - -

    // - - - default
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION, Vector3f COLOR, int LIFETIME)
    {
        Vector2f min = new Vector2f(CENTER).sub(new Vector2f(DIMENSIONS).mul(0.5f));
        Vector2f max = new Vector2f(CENTER).add(new Vector2f(DIMENSIONS).mul(0.5f));

        Vector2f[] vertices = {
            new Vector2f(min.x, min.y), new Vector2f(min.x, max.y),
            new Vector2f(max.x, max.y), new Vector2f(max.x, min.y)};

        if (ROTATION != 0.0f)
        {
            for (Vector2f vert : vertices)
            {
                ForgeMath.rotate(vert, ROTATION, CENTER);
            }
        }

        addLine(vertices[0], vertices[1], COLOR, LIFETIME);
        addLine(vertices[1], vertices[2], COLOR, LIFETIME);
        addLine(vertices[2], vertices[3], COLOR, LIFETIME);
        addLine(vertices[3], vertices[0], COLOR, LIFETIME);
    }

    // - - - no lifetime
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION, Vector3f COLOR)
    {
        addBox(CENTER, DIMENSIONS, ROTATION, COLOR, defaultLifetime);
    }

    // - - - no color
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION, int LIFETIME)
    {
        addBox(CENTER, DIMENSIONS, ROTATION, defaultColor, LIFETIME);
    }

    // - - - no rotation
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, Vector3f COLOR, int LIFETIME)
    {
        addBox(CENTER, DIMENSIONS, 0f, COLOR, LIFETIME);
    }

    // - - - no color and lifetime
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION)
    {
        addBox(CENTER, DIMENSIONS, ROTATION, defaultColor, defaultLifetime);
    }

    // - - - no rotation and lifetime
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, Vector3f COLOR)
    {
        addBox(CENTER, DIMENSIONS, defaultRotation, COLOR, defaultLifetime);
    }

    // - - - no rotation or color
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS, int LIFETIME)
    {
        addBox(CENTER, DIMENSIONS, defaultRotation, defaultColor, LIFETIME);
    }

    // - - - no rotation, color and lifetime
    public static void addBox(Vector2f CENTER, Vector2f DIMENSIONS)
    {
        addBox(CENTER, DIMENSIONS, defaultRotation, defaultColor, defaultLifetime);
    }


    // - - - add Circle - - -

    // - - - default
    public static void addCircle(Vector2f CENTER, float RADIUS, Vector3f COLOR, int LIFETIME)
    {
        Vector2f[] points = new Vector2f[defaultSegments];
        float increment = (float) (2 * Math.PI / defaultSegments);
        float currentAngle = 0;

        for (int i = 0; i < defaultSegments; ++i)
        {
            Vector2f temp = new Vector2f(RADIUS, 0);
            ForgeMath.rotate(temp, currentAngle, new Vector2f());
            points[i] = new Vector2f(temp).add(CENTER);

            if (i > 0)
            {
                addLine(points[i - 1], points[i], COLOR, LIFETIME);
            }
            currentAngle += increment;
        }
        addLine(points[points.length - 1], points[0], COLOR, LIFETIME);
    }

    // - - - no color
    public static void addCircle(Vector2f CENTER, float RADIUS, int LIFETIME)
    {
        addCircle(CENTER, RADIUS, defaultColor, LIFETIME);
    }

    // - - - no lifetime
    public static void addCircle(Vector2f CENTER, float RADIUS, Vector3f COLOR)
    {
        addCircle(CENTER, RADIUS, COLOR, defaultLifetime);
    }

    // - - - neither lifetime nor color
    public static void addCircle(Vector2f CENTER, float RADIUS)
    {
        addCircle(CENTER, RADIUS, defaultColor, defaultLifetime);
    }

    // - - - add Polygon - - -

    // - - - default
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, float ROTATION, Vector3f COLOR, int LIFETIME)
    {
        Vector2f[] points = new Vector2f[SIDE_COUNT];
        float increment = (float) (2 * Math.PI / SIDE_COUNT);
        float currentAngle = 0;

        for (int i = 0; i < SIDE_COUNT; ++i)
        {
            Vector2f temp = new Vector2f(HALF_DIAGONAL, 0);
            ForgeMath.rotate(temp, currentAngle + ROTATION, new Vector2f());
            points[i] = new Vector2f(temp).add(CENTER);

            if (i > 0)
            {
                addLine(points[i - 1], points[i], COLOR, LIFETIME);
            }
            currentAngle += increment;
        }
        addLine(points[points.length - 1], points[0], COLOR, LIFETIME);
    }

    // - - - without lifetime
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, float ROTATION, Vector3f COLOR)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, ROTATION, COLOR, defaultLifetime);
    }

    // - - - without color
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, float ROTATION, int LIFETIME)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, ROTATION, defaultColor, LIFETIME);
    }

    // - - - without rotation
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, Vector3f COLOR, int LIFETIME)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, defaultRotation, COLOR, LIFETIME);
    }

    // - - - without color and lifetime
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, float ROTATION)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, ROTATION, defaultColor, defaultLifetime);
    }

    // - - - without rotation and color
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, int LIFETIME)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, defaultRotation, defaultColor, LIFETIME);
    }

    // - - - without rotation and lifetime
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT, Vector3f COLOR)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, defaultRotation, COLOR, defaultLifetime);
    }

    // - - - with only necessities
    public static void addPolygon(Vector2f CENTER, float HALF_DIAGONAL, int SIDE_COUNT)
    {
        addPolygon(CENTER, HALF_DIAGONAL, SIDE_COUNT, defaultRotation, defaultColor, defaultLifetime);
    }


    // - - - Non-Regular Polygons - - -

    // - - - With Everything
    public static void addPolygonFromVertices(List<Vector2f> VERTICES, float ROTATION, Vector3f COLOR, int LIFETIME)
    {
        if (VERTICES == null || VERTICES.size() < 3)
        {
            Logger.FORGE_LOG_ERROR("Invalid vertices list: A polygon requires at least 3 vertices.");
            return;
        }

        Vector2f center = ForgeMath.calculateCentroid(VERTICES);
        Vector2f previous = ForgeMath.rotateVertex(VERTICES.get(0), center, ROTATION);

        for (int i = 1; i < VERTICES.size(); i++)
        {
            Vector2f current = ForgeMath.rotateVertex(VERTICES.get(i), center, ROTATION);
            addLine(previous, current, COLOR, LIFETIME);
            previous = current;
        }

        Vector2f first = ForgeMath.rotateVertex(VERTICES.get(0), center, ROTATION);
        addLine(previous, first, COLOR, LIFETIME);
    }

    // - - - Without rotation
    public static void addPolygonFromVertices(List<Vector2f> VERTICES, Vector3f COLOR, int LIFETIME)
    {
        addPolygonFromVertices(VERTICES, defaultRotation, COLOR, LIFETIME);
    }

    // - - - without lifetime
    public static void addPolygonFromVertices(List<Vector2f> VERTICES, float ROTATION, Vector3f COLOR)
    {
        addPolygonFromVertices(VERTICES, ROTATION, COLOR, defaultLifetime);
    }

    // - - - Without color and lifetime
    public static void addPolygonFromVertices(List<Vector2f> vertices, float rotation)
    {
        addPolygonFromVertices(vertices, rotation, defaultColor, defaultLifetime);
    }

    // - - - With default color and no rotation
    public static void addPolygonFromVertices(List<Vector2f> vertices)
    {
        addPolygonFromVertices(vertices, defaultRotation, defaultColor, defaultLifetime);
    }
}