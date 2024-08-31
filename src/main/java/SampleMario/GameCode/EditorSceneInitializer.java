package SampleMario.GameCode;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.GizmoSystem.GizmoSystemComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.RenderingSystem.Renderer;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.AssetPool;
import SampleMario.BlockCoinPrefab;
import SampleMario.MarioPrefabs;
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
    }

    // - - - useful initialization
    @Override
    public void init(Scene SCENE)
    {
        sprites = AssetPool.getSpriteSheet("Decorations");
        MainWindow.get().setClearColor(new Vector4f(0.5f, 0.5f, 0.8f, 1.0f));
        PrefabManager.registerPrefab("Mario", new MarioPrefabs());
        PrefabManager.registerPrefab("Question Block", new QuestionBlockPrefab());
        PrefabManager.registerPrefab("Coin Prefab", new BlockCoinPrefab());
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
    }
}