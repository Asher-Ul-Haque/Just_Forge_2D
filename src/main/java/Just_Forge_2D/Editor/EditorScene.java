package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.Attachable.MouseControlComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Core.Scene.Scene;
import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Physics.Physics;
import Just_Forge_2D.Physics.RigidBody.RigidBody;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class EditorScene extends Scene
{
//    private GameObject master = new GameObject("Master", new TransformComponent(new Vector2f(100, 200), new Vector2f(26, 26)), 0);
    private GameObject master = this.createGameObject("Master");
    private SpriteSheet sprites;

    public EditorScene()
    {
    }

    @Override
    public void init()
    {
        loadResources();
        sprites = justForgeAssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");
        SpriteSheet gizmos = justForgeAssetPool.getSpriteSheet("Assets/Textures/gizmos.png");

        this.camera = new Camera(new Vector2f(-250, 0));
        //master.addComponent(new GridLines());
        master.addComponent(new MouseControlComponent());
        master.addComponent(new EditorCamera(this.camera));
        master.addComponent(new GizmoSystem(gizmos));
        //this.addGameObject(master);

        master.start();
    }

    private void loadResources()
    {
        justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
        justForgeAssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new SpriteSheet(justForgeAssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 16, 0));
        justForgeAssetPool.addSpriteSheet("Assets/Textures/gizmos.png", new SpriteSheet(justForgeAssetPool.getTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0));
        justForgeAssetPool.getTexture("Assets/Textures/blendImage2.png");

        for (GameObject g : gameObjects)
        {
            if (g.getCompoent(SpriteComponent.class) != null)
            {
               SpriteComponent spr = g.getCompoent(SpriteComponent.class);
               if (spr.getTexture() != null)
               {
                   spr.setTexture(justForgeAssetPool.getTexture(spr.getTexture().getFilepath()));
               }
            }
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        master.update(DELTA_TIME);
        this.camera.adjustProjection();
        for (GameObject gameObject : this.gameObjects)
        {
            gameObject.update((float) DELTA_TIME);
        }
        render(DELTA_TIME);
    }

    @Override
    public void render(float DELTA_TIME)
    {
        this.renderer.render();
    }

    @Override
    public void editorGUI()
    {
        ImGui.begin("Master Panel");
        master.editorGUI();
        ImGui.end();

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
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTextureID();
            Vector2f[] texCoords = sprite.getTextureCoordinates();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
            {
                GameObject object = Prefabs.generateSpriteObject(sprite, Configurations.GRID_WIDTH, Configurations.GRID_HEIGHT);
                this.activeGameObject = object;
                master.getCompoent(MouseControlComponent.class).pickupObject(object);
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
