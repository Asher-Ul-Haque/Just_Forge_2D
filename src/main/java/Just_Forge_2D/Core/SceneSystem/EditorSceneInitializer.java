package Just_Forge_2D.Core.SceneSystem;

import Just_Forge_2D.Core.AnimationSystem.StateMachine;
import Just_Forge_2D.Core.EntityComponentSystem.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.EditorComponents.InputControls.KeyboardControls;
import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.EntityComponentSystem.Components.EditorComponents.InputControls.MouseControlComponent;
import Just_Forge_2D.Core.EntityComponentSystem.GameObject;
import Just_Forge_2D.Core.EntityComponentSystem.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.Core.EntityComponentSystem.Components.EditorComponents.GizmoSystem.GizmoSystemComponent;
import Just_Forge_2D.EditorSystem.Prefabs;
import Just_Forge_2D.Sound.Sound;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.AssetPool;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

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
    }

    // - - - useful initialization
    @Override
    public void init(SceneManager SCENE)
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
    public void loadResources(SceneManager SCENE)
    {
        AssetPool.getShader("Default");
        AssetPool.addSpriteSheet("Big Spritesheet", "Assets/Textures/bigSpritesheet.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/bigSpritesheet.png"), 16, 16, 16, 0));
        AssetPool.addSpriteSheet("Decorations","Assets/Textures/decorationsAndBlocks.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/decorationsAndBlocks.png"), 16, 16, 81, 0));
        AssetPool.addSpriteSheet("Spritesheet","Assets/Textures/spritesheet.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
        AssetPool.addSpriteSheet("Items","Assets/Textures/items.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/items.png"), 16, 16, 43, 0));
        AssetPool.addSpriteSheet("Gizmos","Assets/Textures/gizmos.png", new SpriteSheet(AssetPool.getTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0));

        AssetPool.addSound("Main Theme","Assets/Sounds/main-theme-overworld.ogg", true);
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
                SpriteSheet playerSprites = AssetPool.getSpriteSheet("Spritesheet");
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
}