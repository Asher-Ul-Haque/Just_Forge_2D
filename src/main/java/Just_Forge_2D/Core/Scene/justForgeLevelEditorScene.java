package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.justForgeSprite;
import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.Transform;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;

import org.joml.Vector2f;
import org.joml.Vector4f;

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

       justForgeGameObject obj1 = new justForgeGameObject("OBject 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
       obj1.addComponent(new justForgeSprite(justForgeAssetPool.getTexture("Assets/Textures/MarioWalk0.png")));
       this.addGameObject(obj1);

       justForgeGameObject obj2 = new justForgeGameObject("OBject 2", new Transform(new Vector2f(400, 400), new Vector2f(256, 256)));
       obj2.addComponent(new justForgeSprite(justForgeAssetPool.getTexture("Assets/Textures/GoombaWalk0.png")));
       this.addGameObject(obj2);

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
