package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.Animations.StateMachine;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.MouseControlComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem.GizmoSystemComponent;
import Just_Forge_2D.Editor.Prefabs;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;


// - - - Class to run the editor
public class EditorSceneInitializer extends SceneInitializer
{
    // - - - private variables
    private SpriteSheet sprites;


    // - - - | Functions | - - -


    // - - - Constructors and initialization - - -

    // - - - useless constructor
    public EditorSceneInitializer()
    {
    }

    // - - - useful initialization
    @Override
    public void init(Scene SCENE)
    {
        sprites = AssetPool.getSpriteSheet("Assets/Textures/decorationsAndBlocks.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("Assets/Textures/gizmos.png");

        this.master = SCENE.createGameObject("Master");
        this.master.noSerialize();
        master.addComponent(new GridlinesComponent());
        master.addComponent(new MouseControlComponent());
        master.addComponent(new EditorCameraComponent(SCENE.getCamera()));
        master.addComponent(new GizmoSystemComponent(gizmos));
        SCENE.addGameObject(this.master);
    }

    @Override
    public void loadResources(Scene SCENE)
    {
        AssetPool.getShader("Assets/Shaders/default.glsl");
        AssetPool.addSpriteSheet("Assets/Textures/decorationsAndBlocks.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/decorationsAndBlocks.png"), 16, 16, 81, 0));
        AssetPool.addSpriteSheet("Assets/Textures/spritesheet.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
        AssetPool.addSpriteSheet("Assets/Textures/items.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/items.png"), 16, 16, 43, 0));
        AssetPool.addSpriteSheet("Assets/Textures/gizmos.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0));

        for (GameObject g : SCENE.getGameObjects())
        {
            if (g.getCompoent(SpriteComponent.class) != null)
            {
               SpriteComponent spr = g.getCompoent(SpriteComponent.class);
               if (spr.getTexture() != null)
               {
                   spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
               }
            }

            if (g.getCompoent(StateMachine.class) != null)
            {
                StateMachine stateMachine = g.getCompoent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }


    // - - - Scene usage - - -


    // - - - editor stuff
    @Override
    public void editorGUI()
    {
        ImGui.begin("Block Picker");
        if (ImGui.beginTabBar("Window Tabar"))
        {
            if (ImGui.beginTabItem("Blocks"))
            {
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);
                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < sprites.size(); ++i) {
                    Sprite sprite = sprites.getSprite(i);
                    float spriteWidth = sprite.getWidth() * 2;
                    float spriteHeight = sprite.getHeight() * 2;
                    int id = sprite.getTextureID();
                    Vector2f[] texCoords = sprite.getTextureCoordinates();

                    ImGui.pushID(i);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, Configurations.GRID_WIDTH, Configurations.GRID_HEIGHT);
//                object.getCompoent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
                        master.getCompoent(MouseControlComponent.class).pickupObject(object);
                    }
                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            if (ImGui.beginTabItem("Prefabs"))
            {
                SpriteSheet playerSprites = AssetPool.getSpriteSheet("Assets/Textures/spritesheet.png");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 4;
                float spriteHeight = sprite.getHeight() * 4;
                int id = sprite.getTextureID();
                Vector2f[] texCoords = sprite.getTextureCoordinates();
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
                {
                    GameObject object = Prefabs.generateMario();
//                object.getCompoent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
                    master.getCompoent(MouseControlComponent.class).pickupObject(object);
                }
                ImGui.sameLine();
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }


        ImGui.end();
    }
}