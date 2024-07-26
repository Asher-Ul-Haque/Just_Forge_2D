package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Sprite.justForgeSprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.justForgeSpriteRenderer;
import Just_Forge_2D.Core.ECS.Components.Sprite.justForgeSpriteSheet;
import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Utils.justForgeTransform;
import imgui.ImGui;
import org.joml.Vector2f;

public class justForgeLevelEditorScene extends justForgeScene 
{
    private justForgeShader defaultShader;
    private justForgeTexture testTexture;
    private float time = 0;
    private justForgeGameObject testObject;
    private justForgeGameObject obj1;
    private justForgeSpriteSheet sprites;

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipeTimeLeft = 0;
    int direction = 1;

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

        loadResources();
        sprites = justForgeAssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");

        obj1 = new justForgeGameObject("OBject 1", new justForgeTransform(new Vector2f(100, 100), new Vector2f(256, 256)), 1);
        obj1.addComponent(new justForgeSpriteRenderer(new justForgeSprite(justForgeAssetPool.getTexture("Assets/Textures/blendImage1.png"))));
        this.addGameObject(obj1);
        this.activeGameObject = obj1;

        justForgeGameObject obj2 = new justForgeGameObject("OBject 2", new justForgeTransform(new Vector2f(400, 100), new Vector2f(256, 256)), 1);
        obj2.addComponent(new justForgeSpriteRenderer(new justForgeSprite(justForgeAssetPool.getTexture("Assets/Textures/blendImage2.png"))));
        this.addGameObject(obj2);

        start();
    }

    private void loadResources()
    {
        justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
        justForgeAssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new justForgeSpriteSheet(justForgeAssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void update(double DELTA_TIME)
    {
        spriteFlipeTimeLeft -= DELTA_TIME;


        obj1.transform.position.x += (float) (150 * DELTA_TIME) * direction;

        for (justForgeGameObject gameObject : this.gameObjects)
        {
            gameObject.update((float) DELTA_TIME);
        }

        if ((obj1.transform.position.x >= 500) || (obj1.transform.position.x <= 50))
        {
            direction = -direction;
        }

        this.renderer.render();
    }

    @Override
    public void editorGUI()
    {
        ImGui.begin("Test Window");
        ImGui.text("Some random text");
        ImGui.end();
    }

}
