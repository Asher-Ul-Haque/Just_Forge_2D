package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.justForgeRigidBodyComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Core.Input.justForgeMouse;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import Just_Forge_2D.Utils.justForgeLogger;
import imgui.ImGui;
import imgui.ImVec2;
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
        sprites = justForgeAssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");
        if (levelLoaded)
        {
            this.activeGameObject = gameObjects.get(0);
            return;
        }


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
        obj2Sprite.setTexture(justForgeAssetPool.getTexture("Assets/images/blendImage2.png"));
        obj2SpriteRender.setSprite(obj2Sprite);
        obj2.addComponent(obj2SpriteRender);
        this.addGameObject(obj2);
    }

    private void loadResources()
    {
        justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
        justForgeAssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new SpriteSheet(justForgeAssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
        justForgeAssetPool.getTexture("Assets/Textures/blendImage2.png");
    }

    @Override
    public void update(double DELTA_TIME)
    {
        justForgeMouse.getOrthoX();
        justForgeMouse.getOrthoY();
        for (GameObject gameObject : this.gameObjects)
        {
            gameObject.update((float) DELTA_TIME);
        }
        this.renderer.render();
    }

    @Override
    public void editorGUI()
    {
        ImGui.begin("Block Picker");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);
        float windowX2 = windowPos.x + windowSize.x;
        for (int i = 0; i < sprites.size(); ++i)
        {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 4;
            float spriteHeight = sprite.getHeight() * 4;
            int id = sprite.getTextureID();
            Vector2f[] texCoords = sprite.getTextureCoordinates();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y))
            {
                justForgeLogger.FORGE_LOG_WARNING("Button: " + i + "clicked");
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2)
            {
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
