package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Forge_Physics.Primitives.Line;
import Just_Forge_2D.Utils.ForgeMath;
import Just_Forge_2D.Utils.justForgeAssetPool;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class DebugPencil
{
    private static final int MAX_LINES = 480;
    private static final List<Line> lines = new ArrayList<>();

    // 6 floats per vertex, 3 for position, 3 for color, 2 vertices per line
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static final justForgeShader shader = justForgeAssetPool.getShader("Assets/Shaders/debug.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    private static final int defaultLifetime = 120; //120 frames
    private static final Vector3f defaultColor = new Vector3f(0, 0, 1);
    private static final int defaultWidth = 2;
    private static final int defaultSegments = 360;
    private static final float defaultRotation = 0f;

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

    public static void beginFrame()
    {
        // - - - make sure started
        if (!started)
        {
            start();
            started = true;
        }

        // - - - remove dead lines
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
        if (lines.size() <= 0) return;

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
        glBufferSubData(GL_ARRAY_BUFFER, 0, Arrays.copyOfRange(vertexArray, 0, lines.size() * 6 * 2)); // we dont have maximum number of lines always

        // - - - draw
        shader.use();
        shader.uploadMatrix4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMatrix4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        // - - - bind the vao
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // - - - draw the batch
        glDrawArrays(GL_LINES, 0, lines.size() * 6 * 2); // openGL uses bresenham line algo

        //  - - - Disable the location
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        // - - - unbind shader
        shader.detach();
    }

    // TODO: Add other primitives and other constatns for common values like colors
    public static void addLine(Vector2f FROM, Vector2f TO, Vector3f COLOR, int LIFETIME)
    {
        if (lines.size() >= MAX_LINES) return;
        DebugPencil.lines.add(new Line(FROM, TO, COLOR, LIFETIME));
    }

    public static void addLine(Vector2f FROM, Vector2f TO, Vector3f COLOR)
    {
    }

    public static void addLine(Vector2f FROM, Vector2f TO, int LIFETIME)
    {
        addLine(FROM, TO, defaultColor, LIFETIME);
    }

    public static void addLine(Vector2f FROM, Vector2f TO)
    {
        addLine(FROM, TO, defaultColor, defaultLifetime);
    }

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION, Vector3f COLOR, int LIFETIME)
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

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION, Vector3f COLOR)
    {
        addBox2D(CENTER, DIMENSIONS, ROTATION, COLOR, defaultLifetime);
    }

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION, int LIFETIME)
    {
        addBox2D(CENTER, DIMENSIONS, ROTATION, defaultColor, LIFETIME);
    }

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, Vector3f COLOR, int LIFETIME)
    {
        addBox2D(CENTER, DIMENSIONS, 0f, COLOR, LIFETIME);
    }


    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, float ROTATION)
    {
        addBox2D(CENTER, DIMENSIONS, ROTATION, defaultColor, defaultLifetime);
    }

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, Vector3f COLOR)
    {
        addBox2D(CENTER, DIMENSIONS, defaultRotation, COLOR, defaultLifetime);
    }

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS, int LIFETIME)
    {
        addBox2D(CENTER, DIMENSIONS, defaultRotation, defaultColor, LIFETIME);
    }

    public static void addBox2D(Vector2f CENTER, Vector2f DIMENSIONS)
    {
        addBox2D(CENTER, DIMENSIONS, defaultRotation, defaultColor, defaultLifetime);
    }

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
}
