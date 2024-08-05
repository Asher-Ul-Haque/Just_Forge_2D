package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.MouseControlComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem.GizmoSystemComponent;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.Editor.Prefabs;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;


// - - - Class to run the editor
public class EditorScene extends Scene
{
    // - - - private variables
    private final GameObject master = this.createGameObject("Master");
    private SpriteSheet sprites;


    // - - - | Functions | - - -


    // - - - Constructors and initialization - - -

    // - - - useless constructor
    public EditorScene()
    {
    }

    // - - - useful initialization
    @Override
    public void init()
    {
        loadResources();
        sprites = AssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("Assets/Textures/gizmos.png");

        this.camera = new Camera(new Vector2f(-250, -100));
        master.addComponent(new GridlinesComponent());
        master.addComponent(new MouseControlComponent());
        master.addComponent(new EditorCameraComponent(this.camera));
        master.addComponent(new GizmoSystemComponent(gizmos));

        master.start();
    }

    private void loadResources()
    {
        AssetPool.getShader("Assets/Shaders/default.glsl");
        AssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 16, 0));
        AssetPool.addSpriteSheet("Assets/Textures/gizmos.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0));
        AssetPool.getTexture("Assets/Textures/blendImage2.png");

        for (GameObject g : gameObjects)
        {
            if (g.getCompoent(SpriteComponent.class) != null)
            {
               SpriteComponent spr = g.getCompoent(SpriteComponent.class);
               if (spr.getTexture() != null)
               {
                   spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
               }
            }
        }
    }


    // - - - Scene usage - - -

    @Override
    public void update(float DELTA_TIME)
    {
        master.update(DELTA_TIME);
        this.camera.adjustProjection();
        for (GameObject gameObject : this.gameObjects)
        {
            gameObject.update(DELTA_TIME);
        }
        render(DELTA_TIME);
    }

    @Override
    public void render(float DELTA_TIME)
    {
        this.renderer.render();
    }

    // - - - editor stuff
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
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2)
            {
                ImGui.sameLine();
            }
        }
        ImGui.end();
    }
}
