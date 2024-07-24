package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.justForgeFontRendererComponent;
import Just_Forge_2D.Core.ECS.Components.justForgeSpriteRendererComponent;
import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.Transform;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Core.justForgeWindow;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;

import org.joml.Vector2f;
import org.joml.Vector4f;
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
    }

    @Override
    public void init() 
    {
        // - - - TODO: test code, remov ethe 10k cubes
        this.camera = new justForgeCamera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float)(600 - xOffset * 2);
        float totalHeight = (float)(300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; ++x)
        {
            for (int y = 0; y < 100; ++y)
            {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                justForgeGameObject gameObject = new justForgeGameObject("Test Obect- " + x + " : " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                gameObject.addComponent(new justForgeSpriteRendererComponent(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                this.addGameObject(gameObject);
            }
        }

        loadResources();

        start();
    }

    private void loadResources()
    {
        justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
    }

    @Override
    public void update(double DELTA_TIME)
    {
        for (justForgeGameObject gameObject : this.gameObjects)
        {
            gameObject.update((float) DELTA_TIME);
        }

        this.renderer.render();
    }
}
