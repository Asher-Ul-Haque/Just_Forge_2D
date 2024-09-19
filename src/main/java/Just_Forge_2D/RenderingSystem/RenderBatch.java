package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.Utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL20C.*;
import static org.lwjgl.opengl.GL30C.glBindVertexArray;
import static org.lwjgl.opengl.GL30C.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>
{
    /* C struct
        Vertex
        Position
        Color
        Texture coords
        texture id
     */


    // - - - | Private variables | - - -


    // - - - What would have been a #define in C

    // - - - sizes
    private final int TEXTURE_COORDS_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;
    private final int POSITION_SIZE = 2;
    private final int VERTEX_SIZE = 10;
    private final int COLOR_SIZE = 4;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    // - - - offset
    private final int POSITION_OFFSET = 0;
    private final int COLOR_OFFSET = POSITION_OFFSET + POSITION_SIZE * Float.BYTES;
    private final int TEXTURE_COORDS_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_COORDS_OFFSET + TEXTURE_COORDS_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEXTURE_ID_OFFSET + TEXTURE_ID_SIZE * Float.BYTES;

    // - - - batch related
    private final SpriteComponent[] sprites;
    private int spriteCount;
    protected boolean hasRoom;
    private final List<Texture> textures;
    private final int maxBatchSize;
    private final int layer;

    // - - - for renderer
    private int vaoID, vboID;
    private final float[] vertices;
    private final int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};
    private Renderer renderer;


    // - - - Functions - - - -

    // - - - constructor
    public RenderBatch(int MAX_BATCH_SIZE, int LAYER, Renderer RENDERER)
    {
        this.renderer = RENDERER;
        this.maxBatchSize = MAX_BATCH_SIZE;
        this.sprites = new SpriteComponent[maxBatchSize];
        this.layer = LAYER;

        //  4 vertices quadrants
        this.vertices = new float[maxBatchSize * VERTEX_SIZE * 4];

        this.spriteCount = 0;
        this.hasRoom = true;

        // - - - Initialise textures
        this.textures = new ArrayList<>();

        Logger.FORGE_LOG_INFO("Render Batch created for layer: " + this.layer);
    }

    // - - - starter to make the batch accept input, create its buffers and enable them
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

        glVertexAttribPointer(2, TEXTURE_COORDS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDS_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
        Logger.FORGE_LOG_DEBUG("RenderBatch for layer: " + this.layer + " started");
    }

    // - - - there is no comment here
    protected void render()
    {
        boolean rebufferData = false;
        for (int i = 0; i < spriteCount; ++i)
        {
            SpriteComponent sprite = sprites[i];
            if (sprite.isChanged())
            {
                if (!hasTexture(sprite.getTexture()))
                {
                    this.renderer.destroyGameObject(sprite.gameObject);
                    this.renderer.add(sprite.gameObject);
                }
                else
                {
                    loadVertexProperties(i);
                    sprite.clean();
                    rebufferData = true;
                }
            }
            if (sprite.gameObject.transform.layer != this.layer)
            {
                destroyIfExists(sprite.gameObject);
                renderer.add(sprite.gameObject);
                i--;
            }
        }
        if (rebufferData)
        {
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        // - - - Use the shader
        Shader shader = Renderer.getCurrentShader();
        shader.use();
        shader.uploadMatrix4f("uProjection", MainWindow.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMatrix4f("uView", MainWindow.getCurrentScene().getCamera().getViewMatrix());
        for (int i = 0; i < textures.size(); ++i)
        {
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", textureSlots);

        // - - - bIND everything
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        // - - - Draw call
        glDrawElements(GL_TRIANGLES, this.spriteCount * 6, GL_UNSIGNED_INT, 0);

        // - - - Unbind textures
        for (Texture texture : textures)
        {
            texture.detach();
        }

        // - - - Disable everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        shader.detach();
    }

    // - - - add sprite to a renderer batch, taking into account its layering
    public void addSprite(SpriteComponent SPRITE)
    {
        // - - - Get the index and add the render object
        int index = this.spriteCount;
        this.sprites[index] = SPRITE;
        this.spriteCount++;

        // - - - Check for textures
        if (SPRITE.getTexture() != null)
        {
            if (!textures.contains(SPRITE.getTexture()))
            {
                Logger.FORGE_LOG_TRACE("Detected texture for sprite: " + SPRITE + " Ready to be loaded");
                textures.add(SPRITE.getTexture());
            }
        }

        // - - - Add properties to load vertices array
        loadVertexProperties(index);

        if (spriteCount >= this.maxBatchSize)
        {
            Logger.FORGE_LOG_WARNING("No more space to insert another sprite. Render Batch full");
            this.hasRoom = false;
        }
    }

    // - - - loading vertex properties
    private void loadVertexProperties(int INDEX)
    {
        SpriteComponent sprite = this.sprites[INDEX];

        // - - - Find offset within array (4 vertices for a sprite) and color
        int offset = INDEX * 4 * VERTEX_SIZE;
        Vector4f color = sprite.getColor();
        int textID = 0;
        Vector2f[] textureCoords = sprite.getTextureCoords();

        // - - - Load texture
        if (sprite.getTexture() != null)
        {
            for (int i = 0; i < textures.size(); ++i)
            {
                if (textures.get(i).equals(sprite.getTexture()))
                {
                    textID = i + 1;
                    break;
                }
            }
        }

        // - - - manage rotation
        boolean isRotated = sprite.gameObject.transform.rotation != 0.0f;
        Matrix4f transformMatrix = new Matrix4f().identity();
        if (isRotated)
        {
            transformMatrix.translate(sprite.gameObject.transform.position.x,
                    sprite.gameObject.transform.position.y, 0f);
            transformMatrix.rotate(sprite.gameObject.transform.rotation,
                    0, 0, 1);
            transformMatrix.scale(sprite.gameObject.transform.scale.x,
                    sprite.gameObject.transform.scale.y, 1);
        }

        // - - - Add vertices with the appropriate properties
        float xAdd = 0.5f; // place towards center
        float yAdd = 0.5f;
        for (int i = 0; i < 4; ++i)
        {
            switch (i)
            {
                case 1:
                    yAdd = -0.5f;
                    break;

                case 2:
                    xAdd = -0.5f;
                    break;

                case 3:
                    yAdd = 0.5f;
                    break;
            }
            Vector4f currentPos = new Vector4f(sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x),
                    sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y),
                    0, 1);
            if (isRotated)
            {
                currentPos = new Vector4f(xAdd, yAdd, 0, 1).mul(transformMatrix);
            }

            // - - - Load positions
            vertices[offset] = currentPos.x;
            vertices[offset + 1] = currentPos.y;

            // - - - Load the color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            // - - - Load the texture coordinates and id
            vertices[offset + 6] = textureCoords[i].x;
            vertices[offset + 7] = textureCoords[i].y;
            vertices[offset + 8] = textID;

            // - - - Load Entity ID
            vertices[offset + 9] = sprite.gameObject.getUniqueID() + 1;

            // - - - Update the offset
            offset += VERTEX_SIZE;
        }
    }


    // - - - Helper functions, getters and setters - - -

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
        ELEMENTS[offsetArrayIndex + 2] = offset;

        // - - - Create the second triangle
        ELEMENTS[offsetArrayIndex + 3] = offset;
        ELEMENTS[offsetArrayIndex + 4] = offset + 2;
        ELEMENTS[offsetArrayIndex + 5] = offset + 1;
    }


    // - - - Texture - - -
    public boolean hasTextureRoom()
    {
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture TEXTURE)
    {
        return this.textures.contains(TEXTURE) || TEXTURE == null;
    }

    // - - - layer
    public int getLayer()
    {
        return this.layer;
    }

    // - - - compare
    @Override
    public int compareTo(@NotNull RenderBatch OTHER)
    {
        return Integer.compare(this.getLayer(), OTHER.getLayer());
    }

    // - -  destroy
    public boolean destroyIfExists(GameObject GO)
    {
        Logger.FORGE_LOG_DEBUG("Removing game object: " + GO + " from render batch with layer " + this.layer);
        SpriteComponent sprite = GO.getCompoent(SpriteComponent.class);
        for (int i = 0; i < spriteCount; ++i)
        {
            if (sprites[i] == sprite)
            {
                for (int j = i ; j < spriteCount - 1; j++)
                {
                    sprites[j] = sprites[j + 1];
                    sprites[j].setChanged();
                }
                spriteCount--;
                return true;
            }
        }
        return false;
    }
}