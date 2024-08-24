package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.AudioSystems.Sound;
import Just_Forge_2D.EditorSystem.Prefabs;
import Just_Forge_2D.EditorSystem.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem.GizmoSystemComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.InputControls.KeyboardControls;
import Just_Forge_2D.EditorSystem.EditorComponents.InputControls.MouseControlComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.RenderingSystems.SpriteSheet;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Forge;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.WindowSystem.WindowSystemManager;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;

import java.io.File;
import java.util.Collection;


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
        this.renderer = WindowSystemManager.getRenderer(Forge.window);
        this.physicsWorld = new PhysicsWorld();
    }

    // - - - useful initialization
    @Override
    public void init(Scene SCENE)
    {
        sprites = AssetPool.getSpriteSheet("Decorations");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("Gizmos");

        this.master = SCENE.createGameObject("Master");
        this.master.noSerialize();
        master.addComponent(new KeyboardControls());
        master.addComponent(new GridlinesComponent());
        master.addComponent(new MouseControlComponent());
        master.addComponent(new EditorCameraComponent(SCENE.getCamera()));
        master.addComponent(new GizmoSystemComponent(gizmos));
        SCENE.addGameObject(this.master);
    }

    @Override
    public void loadResources(Scene SCENE)
    {
        AssetPool.getShader("Default");

        AssetPool.addTexture("Big Spritesheet", "Assets/Textures/bigSpritesheet.png");

        AssetPool.addSpriteSheet("Turtle", new SpriteSheet(AssetPool.getTexture("Assets/Textures/turtle.png"), 16, 24, 4, 0));
        AssetPool.addSpriteSheet("Pipes", new SpriteSheet(AssetPool.getTexture("Assets/Textures/pipes.png"), 32, 32, 4, 0));
        AssetPool.addSpriteSheet("Big Spritesheet", new SpriteSheet(AssetPool.getTexture("Assets/Textures/bigSpritesheet.png"), 16, 32, 42, 0));
        AssetPool.addSpriteSheet("Decorations",new SpriteSheet(AssetPool.getTexture("Assets/Textures/decorationsAndBlocks.png"), 16, 16, 81, 0));
        AssetPool.addSpriteSheet("Spritesheet",new SpriteSheet(AssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
        AssetPool.addSpriteSheet("Items", new SpriteSheet(AssetPool.getTexture("Assets/Textures/items.png"), 16, 16, 43, 0));
        AssetPool.addSpriteSheet("Gizmos", new SpriteSheet(AssetPool.getTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0));

        AssetPool.addSound("Just_Forge_2D.Main Theme","Assets/Sounds/main-theme-overworld.ogg", true);
        AssetPool.addSound("Flagpole","Assets/Sounds/flagpole.ogg", false);
        AssetPool.addSound("Break Block", "Assets/Sounds/break_block.ogg", false);
        AssetPool.addSound("Bump","Assets/Sounds/bump.ogg", false);
        AssetPool.addSound("Coin", "Assets/Sounds/coin.ogg", false);
        AssetPool.addSound("GameOver","Assets/Sounds/gameover.ogg", false);
        AssetPool.addSound("Jump Small", "Assets/Sounds/jump-small.ogg", false);
        AssetPool.addSound("Mario Die", "Assets/Sounds/mario_die.ogg", false);
        AssetPool.addSound("Pipe", "Assets/Sounds/pipe.ogg", false);
        AssetPool.addSound("Powerup", "Assets/Sounds/powerup.ogg", false);
        AssetPool.addSound("Powerup Appears", "Assets/Sounds/powerup_appears.ogg", false);
        AssetPool.addSound("Stage Clear", "Assets/Sounds/stage_clear.ogg", false);
        AssetPool.addSound("Stomp", "Assets/Sounds/stomp.ogg", false);
        AssetPool.addSound("Kick", "Assets/Sounds/kick.ogg", false);
        AssetPool.addSound("Invincible", "Assets/Sounds/invincible.ogg", false);

        for (GameObject g : SCENE.getGameObjects())
        {
            if (g.getComponent(SpriteComponent.class) != null)
            {
               SpriteComponent spr = g.getComponent(SpriteComponent.class);
               if (spr.getTexture() != null)
               {
                   spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
               }
            }

            if (g.getComponent(AnimationComponent.class) != null)
            {
                AnimationComponent animationComponent = g.getComponent(AnimationComponent.class);
                animationComponent.refreshTextures();
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
                        GameObject object = Prefabs.generateSpriteObject(sprite, DefaultValues.GRID_WIDTH, DefaultValues.GRID_HEIGHT);
//                object.getCompoent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
                        master.getComponent(MouseControlComponent.class).pickupObject(object);
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
                SpriteSheet playerSprites = AssetPool.getSpriteSheet("Spritesheet");
                Sprite sprite = playerSprites.getSprite(0);
                float spriteWidth = sprite.getWidth() * 4;
                float spriteHeight = sprite.getHeight() * 4;
                int id = sprite.getTextureID();
                Vector2f[] texCoords = sprite.getTextureCoordinates();
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y))
                {
                    GameObject object = Prefabs.generateMario();
                    object.getComponent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
                    master.getComponent(MouseControlComponent.class).pickupObject(object);
                }

                SpriteSheet items = AssetPool.getSpriteSheet("Items");
                Sprite sprite2 = items.getSprite(0);
                float sprite2Width = sprite2.getWidth() * 4;
                float sprite2Height = sprite2.getHeight() * 4;
                int id2 = sprite2.getTextureID();
                Vector2f[] texCoords2 = sprite2.getTextureCoordinates();
                if (ImGui.imageButton(id2, sprite2Width, sprite2Height, texCoords2[2].x, texCoords2[0].y, texCoords2[0].x, texCoords2[2].y))
                {
                    GameObject object = Prefabs.generateQuestionBlock();
                    object.getComponent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
                    master.getComponent(MouseControlComponent.class).pickupObject(object);
                }
                ImGui.sameLine();
                ImGui.endTabItem();
            }

            if (ImGui.beginTabItem("Sounds"))
            {
                Collection<Sound> sounds = AssetPool.getAllSounds();
                for (Sound sound : sounds)
                {
                    File tmp = new File(sound.getFIlePath());
                    if (ImGui.button(tmp.getName()))
                    {
                        if (!sound.isPlaying())
                        {
                            sound.play();
                        }
                        else
                        {
                            sound.stop();
                        }
                    }
                }
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }

    @Override
    public void assignRenderingSystem() {

    }

    @Override
    public void assignPhysicsSystem() {

    }

}