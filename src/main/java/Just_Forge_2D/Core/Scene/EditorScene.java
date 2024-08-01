package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.GridLines;
import Just_Forge_2D.Core.ECS.Components.Attachable.MouseControlComponent;
import Just_Forge_2D.Core.ECS.Components.justForgeRigidBodyComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Editor.Prefabs;
import Just_Forge_2D.Physics.PhysicsSystem;
import Just_Forge_2D.Physics.RigidBody.RigidBody;
import Just_Forge_2D.Renderer.DebugPencil;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.justForgeAssetPool;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;


public class EditorScene extends justForgeScene
{
    private GameObject master = new GameObject("Master", new TransformComponent(new Vector2f(100, 100)), 0);
    private SpriteSheet sprites;
    PhysicsSystem physics = new PhysicsSystem(1.0f / 60.0f, new Vector2f(0, -10));
    TransformComponent obj1, obj2;
    RigidBody rb1, rb2;


//    MouseControlComponent mouseControls = new MouseControlComponent();

    public EditorScene()
    {
    }

    @Override
    public void init()
    {

        //master.addComponent(new GridLines());
        master.addComponent(new MouseControlComponent());
        this.addGameObject(master);

        obj1 = new TransformComponent(new Vector2f(100, 500));
        obj2 = new TransformComponent(new Vector2f(200, 500));
        rb1 = new RigidBody();
        rb2 = new RigidBody();
        rb1.setRawTransform(obj1);
        rb2.setRawTransform(obj2);
        rb1.setMass(100);
        rb2.setMass(200);
        physics.addRigidBody(rb1);
        physics.addRigidBody(rb2);

        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        sprites = justForgeAssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");
        if (levelLoaded)
        {
            if (!gameObjects.isEmpty())
            {
                this.activeGameObject = gameObjects.get(0);
            }
        }
    }

    private void loadResources()
    {
        justForgeAssetPool.getShader("Assets/Shaders/default.glsl");
        justForgeAssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new SpriteSheet(justForgeAssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 81, 0));
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
    public void update(double DELTA_TIME)
    {
        master.getCompoent(MouseControlComponent.class).update((float) DELTA_TIME);

        DebugPencil.addBox2D(obj1.position, new Vector2f(32, 32), new Vector3f(1, 0, 0));
        DebugPencil.addBox2D(obj2.position, new Vector2f(32, 32), new Vector3f(0, 1, 0));

        physics.update((float) DELTA_TIME);

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
