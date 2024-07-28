package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.justForgeRigidBodyComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class EditorScene extends justForgeScene
{
    private GameObject obj1;
    private SpriteSheet sprites;
    private SpriteComponent obj1Sprite;

    public EditorScene()
    {
    }

    @Override
    public void init() {
        // - - - TODO: test code, remov ethe 10k cubes
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        if (levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        sprites = justForgeAssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");

        obj1 = new GameObject("OBject 1", new TransformComponent(new Vector2f(200, 100), new Vector2f(256, 256)), 2);
        obj1Sprite = new SpriteComponent();
        obj1Sprite.setColor(new Vector4f(1, 0, 0, 1));
        obj1.addComponent(obj1Sprite);
        obj1.addComponent(new justForgeRigidBodyComponent());
        this.addGameObject(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new TransformComponent(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
        SpriteComponent obj2SpriteRender = new SpriteComponent();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(justForgeAssetPool.getTexture("Assets/images/MarioWalk0.png"));
        obj2SpriteRender.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRender);
        this.addGameObject(obj2);
    }

    private void loadResources()
    {
        justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
        justForgeAssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new SpriteSheet(justForgeAssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
        justForgeAssetPool.getTexture("Assets/Textures/MarioWalk0.png");
    }

    @Override
    public void update(double DELTA_TIME)
    {
        for (GameObject gameObject : this.gameObjects)
        {
            gameObject.update((float) DELTA_TIME);
        }
        this.renderer.render();
    }
}
