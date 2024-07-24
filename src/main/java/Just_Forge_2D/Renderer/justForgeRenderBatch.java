package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.ECS.Components.justForgeSpriteRendererComponent;
import Just_Forge_2D.Core.justForgeWindow;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Utils.justForgeLogger;
import org.joml.Vector4f;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.*;

public class justForgeRenderBatch
{
    /* C struct
        Vertex
        Position
        Color
     */
    private final int POSITION_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 6;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private justForgeSpriteRendererComponent[] sprites;
    private int spriteCount;
    protected boolean hasRoom;
    private float[] vertices;

    private int vaoID, vboID;
    private int maxBatchSize;
    private justForgeShader shader;

    public justForgeRenderBatch(int MAX_BATCH_SIZE)
    {
        shader = justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
        this.maxBatchSize = MAX_BATCH_SIZE;
        this.sprites = new justForgeSpriteRendererComponent[maxBatchSize];

        //  4 vertices quadrants
        this.vertices = new float[maxBatchSize * VERTEX_SIZE * 4];

        this.spriteCount = 0;
        this.hasRoom = true;
    }

    public void start()
    {
        // - - - Generate and bind a vertex array object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // - - - Allocate space for vertices on the GPU
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, (long) vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // - - - Create and upload the indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // - - - Enable the buffer attribute pointers
        glVertexAttribPointer(0, POSITION_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POSITION_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    private int[] generateIndices()
    {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];

        for (int i = 0; i < maxBatchSize; ++i)
        {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] ELEMENTS, int INDEX)
    {
        int offsetArrayIndex = 6 * INDEX;
        int offset = 4 * INDEX;

        // 3, 2, 0, 0, 2, 1         7, 6, 4, 4, 6, 5
        // - - - Create the first triangle
        ELEMENTS[offsetArrayIndex] = offset + 3;
        ELEMENTS[offsetArrayIndex + 1] = offset + 2;
        ELEMENTS[offsetArrayIndex + 2] = offset + 0;

        // - - - Create the second triangle
        ELEMENTS[offsetArrayIndex + 3] = offset + 0;
        ELEMENTS[offsetArrayIndex + 4] = offset + 2;
        ELEMENTS[offsetArrayIndex + 5] = offset + 1;
    }

    protected void render()
    {
        // - - - for now, we will rebuffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // - - - Use the shader
        shader.use();
        shader.uploadMatrix4f("uProjection", justForgeWindow.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMatrix4f("uView", justForgeWindow.getCurrentScene().getCamera().getViewMatrix());

        // - - - bIND everything
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // - - - Draw call
        glDrawElements(GL_TRIANGLES, this.spriteCount * 6, GL_UNSIGNED_INT, 0);

        // - - - Disable everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    public void addSprite(justForgeSpriteRendererComponent SPRITE)
    {
        // - - - Get the index and add the render object
        int index = this.spriteCount;
        this.sprites[index] = SPRITE;
        this.spriteCount++;

        // - - - Add properties to load verticies array
        loadVertexProperties(index);

        if (spriteCount >= this.maxBatchSize)
        {
            justForgeLogger.FORGE_LOG_WARNING("No more space to insert another sprite. Render Batch full");
            this.hasRoom = false;
        }
    }

    private void loadVertexProperties(int INDEX)
    {
        justForgeSpriteRendererComponent sprite = this.sprites[INDEX];

        // - - - Find offset within array (4 verticies for a sprite) and color
        int offset = INDEX * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();

        // - - - Add vertiices with the appropirate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; ++i)
        {
            switch (i)
            {
                case 1:
                    yAdd = 0.0f;
                    break;

                case 2:
                    xAdd = 0.0f;
                    break;

                case 3:
                    yAdd = 1.0f;
                    break;
            }

            // - - - Load positions
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            // - - - Load the color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // - - - Update the offset
            offset += VERTEX_SIZE;
        }
    }
}
