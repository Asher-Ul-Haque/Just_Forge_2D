package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.justForgeFontRendererComponent;
import Just_Forge_2D.Core.ECS.Components.justForgeSpriteRendererComponent;
import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Core.justForgeLogger;
import Just_Forge_2D.Core.justForgeWindow;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class justForgeLevelEditorScene extends justForgeScene 
{
    private justForgeShader defaultShader;
    private justForgeTexture testTexture;
    private float time = 0;
    private justForgeGameObject testObject;

    private float[] vertexArray = {
            // position                    // color                         // texture UV
             100f,    0.5f,    0.0f,       1.0f, 0.0f, 0.0f, 1.0f,          1, 1, // Bottom right 0
             0.5f,    100.5f,  0.0f,       0.0f, 1.0f, 0.0f, 1.0f,          0, 0, // Top left 1
             100.5f,  100.5f,  0.0f ,      1.0f, 0.0f, 1.0f, 1.0f,          1, 0, // Top right    2
             0.5f,    0.5f,    0.0f,       1.0f, 1.0f, 0.0f, 1.0f,          0, 1, // Bottom left  3
    };

    // IMPORTANT: Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoID, vboID, eboID;

    public justForgeLevelEditorScene() 
    {
        init();
        start();
    }

    @Override
    public void init() 
    {
        justForgeLogger.FORGE_LOG_INFO("Current Scene: Level Editor");
        defaultShader = new justForgeShader("Assets/Shaders/default.glsl");
        defaultShader.compile();

        justForgeLogger.FORGE_LOG_DEBUG("Creating test object");
        this.testObject = new justForgeGameObject("test Object");
        this.testObject.addComponent(new justForgeSpriteRendererComponent());
        this.testObject.addComponent(new justForgeFontRendererComponent());
        this.addGameObject(this.testObject);

        // - - - test image
        this.testTexture = new justForgeTexture("Assets/Textures/MarioWalk0.png");

        // - - - Creating camera
        justForgeLogger.FORGE_LOG_INFO("Creating Scene Camera");
        this.camera = new justForgeCamera(new Vector2f());

        // ============================================================
        // Generate VAO, VBO, and EBO buffer objects, and send to GPU
        // ============================================================
        justForgeLogger.FORGE_LOG_DEBUG("Binding Buffer Objects before Sending to GPU");

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = Float.BYTES;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);

        justForgeLogger.FORGE_LOG_INFO("Shader System Online");
    }

    @Override
    public void update(double DELTA_TIME)
    {
        // - - - TODO: test code remove this
        camera.position.x -= (float) (DELTA_TIME * 100);
        camera.position.y -= (float) (DELTA_TIME * 100);
        if (camera.position.x < -1000)
        {
            camera.position.x = 80;
        }
        if (camera.position.y < -1000)
        {
            camera.position.y = 80;
        }

        // - - - Use shader
        defaultShader.use();


        // - - - Upload DATA to GPU - - -

        // - - - Upload Texture Data
        defaultShader.uploadTexture("TEXTURE_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        // - - - Upload Camera Data
        defaultShader.uploadMatrix4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMatrix4f("uView", camera.getViewMatrix());

        // Bind the VAO that we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        time += (float) DELTA_TIME;
        justForgeWindow.get().r = (float) (0.5f + (0.5f * Math.sin(time)));
        justForgeWindow.get().g = (float) (0.5f + (0.5f * Math.sin(time + (1.0f / 3.0f * Math.PI))));
        justForgeWindow.get().b = (float) (0.5f + (0.5f * Math.sin(time) + (2.0f / 3.0f * Math.PI)));

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();

        for (justForgeGameObject gameObject : this.gameObjects)
        {
            gameObject.update((float) DELTA_TIME);
        }
    }
}
