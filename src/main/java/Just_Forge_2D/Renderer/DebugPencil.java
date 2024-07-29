package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.justForgeAssetPool;
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
    private static final int MAX_LINES = 480;
    private static final List<Line2D> lines = new ArrayList<>();

    // 6 floats per vertex, 3 for position, 3 for color, 2 vertices per line
    private static final float[] vertexArray = new float[MAX_LINES * 6 * 2];
    private static final justForgeShader shader = justForgeAssetPool.getShader("Assets/Shaders/debug.glsl");

    private static int vaoID;
    private static int vboID;

    private static boolean started = false;

    private static final int defaultLifetime = 120; //120 frames
    private static final Vector3f defaultColor = new Vector3f(0, 0, 1);
    private static final int defaultWidth = 2;

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
        for (Line2D line: lines)
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
    public static void addLine2D(Vector2f FROM, Vector2f TO, Vector3f COLOR, int LIFETIME)
    {
        if (lines.size() >= MAX_LINES) return;
        DebugPencil.lines.add(new Line2D(FROM, TO, COLOR, LIFETIME));
    }

    public static void addLine2D(Vector2f FROM, Vector2f TO, Vector3f COLOR)
    {
    }

    public static void addLine2D(Vector2f FROM, Vector2f TO, int LIFETIME)
    {
        addLine2D(FROM, TO, defaultColor, LIFETIME);
    }

    public static void addLine2D(Vector2f FROM, Vector2f TO)
    {
        addLine2D(FROM, TO, defaultColor, defaultLifetime);
    }
}
