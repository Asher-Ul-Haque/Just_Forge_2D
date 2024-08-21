package Pet;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.AnimationSystem.AnimationState;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.RenderingSystems.SpriteSheet;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import org.joml.Vector4f;

public class Main {
    private static final WindowConfig CONFIG = new WindowConfig(
            200, 200, "Stuti Pet", true, 0, 0, 16f / 9f, true, false, true,
            false, true, true, "/home/nadeem/Documents/Just_Forge_2D/src/main/java/Pet/Assets/Textures/icon.png"
    );
    public static Window petWindow;
    public static Scene petScene;
    private static SpriteSheet foxSprites;
    private static AnimationComponent foxAnimation;

    public static void main(String[] args) {
        initialize();
        createFox();
        petScene.start();

        while (!petWindow.shouldClose()) {
            update();
        }
    }

    private static void initialize() {
        petWindow = new Window(CONFIG);
        petWindow.setClearColor(new Vector4f(0.0f, 0.0f, 0.0f, 0.0f));
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        petScene = SceneSystemManager.addScene(petScene, new PetSceneInitializer(), "Fox");
        petWindow.setPosition((int) Mouse.getX(), (int) Mouse.getY());
    }

    private static void createFox() {
        GameObject fox = new GameObject("Fox");
        foxSprites = AssetPool.getSpriteSheet("Fox Sprites");

        SpriteComponent foxSpriteComponent = new SpriteComponent();

        fox.addComponent(foxSpriteComponent);
        fox.addComponent(new TransformComponent());

        fox.transform.position.set(1.5f, 2.5f);
        fox.transform.scale.set(4f, 4f);

        foxAnimation = addFoxAnimations();
        fox.addComponent(foxAnimation);
        petScene.addGameObject(fox);
        petScene.getRenderer().bindShader(AssetPool.getShader("Default"));
    }

    private static AnimationComponent addFoxAnimations() {
        SpriteSheet foxSprites = AssetPool.getSpriteSheet("Fox Sprites");

        AnimationState idle = new AnimationState("idle", true);
        for (int i = 0; i < 5; ++i) {
            idle.addFrame(foxSprites.getSprite(i));
        }

        AnimationState lookAround = new AnimationState("look", false);
        for (int i = 0; i < 14; ++i) {
            lookAround.addFrame(foxSprites.getSprite((14 * 1) + i));
        }

        AnimationState walk = new AnimationState("walk", true);
        for (int i = 0; i < 8; ++i) {
            walk.addFrame(foxSprites.getSprite((14 * 2) + i));
        }

        AnimationState play = new AnimationState("play", false);
        for (int i = 0; i < 11; ++i) {
            play.addFrame(foxSprites.getSprite((14 * 3) + i));
        }

        AnimationState frighten = new AnimationState("frighten", false);
        for (int i = 0; i < 5; ++i) {
            frighten.addFrame(foxSprites.getSprite((14 * 4) + i));
        }

        AnimationState sleep = new AnimationState("sleep", true);
        for (int i = 0; i < 6; ++i) {
            sleep.addFrame(foxSprites.getSprite((14 * 5) + i), 0.2f);
        }

        AnimationState rest = new AnimationState("rest", false);
        for (int i = 0; i < 7; ++i) {
            rest.addFrame(foxSprites.getSprite((14 * 6) + i));
        }
        AnimationComponent animate = new AnimationComponent();
        animate.addState(idle);
        animate.addState(lookAround);
        animate.addState(walk);
        animate.addState(play);
        animate.addState(frighten);
        animate.addState(sleep);
        animate.addState(rest);

        // Setting default state
        animate.setDefaultState(idle);
        animate.addStateTrigger(idle.title, lookAround.title, "look");
        animate.addStateTrigger(idle.title, walk.title, "walk");
        animate.addStateTrigger(walk.title, idle.title, "walk-idle");
        animate.addStateTrigger(idle.title, play.title, "play");
        animate.addStateTrigger(idle.title, frighten.title, "frighten");
        animate.addStateTrigger(idle.title, sleep.title, "sleep");
        animate.addStateTrigger(idle.title, rest.title, "rest");
        animate.addStateTrigger(sleep.title, rest.title, "sleep-rest");
        animate.addStateTrigger(rest.title, lookAround.title, "rest-look");
        return animate;
    }

    private static void update()
    {
        if (Keyboard.isKeyBeginPress(Keys.K)) Logger.FORGE_LOG_TRACE("");
        handleInput();
        petWindow.loop();
        petScene.render(petWindow.getDeltaTime());
    }

    private static void handleInput()
    {
        if (Keyboard.isKeyPressed(Keys.ESCAPE))
        {
            petWindow.close();
        }

        int x = petWindow.getXPosition();
        int y = petWindow.getYPosition();

        if (Keyboard.isKeyPressed(Keys.ARROW_RIGHT)) x += 5;
        if (Keyboard.isKeyPressed(Keys.ARROW_LEFT)) x -= 5;
        if (Keyboard.isKeyPressed(Keys.ARROW_UP)) y -= 5;
        if (Keyboard.isKeyPressed(Keys.ARROW_DOWN)) y += 5;

        if (x != petWindow.getXPosition() || y != petWindow.getYPosition())
        {
            petWindow.setPosition(x, y);
        }

        // Handling state changes based on number keys
        if (Keyboard.isKeyPressed(Keys.NUM_1)) {foxAnimation.trigger("look");}
        if (Keyboard.isKeyPressed(Keys.NUM_2)) {foxAnimation.trigger("walk");}
        if (Keyboard.isKeyPressed(Keys.NUM_3)) {foxAnimation.trigger("play");}
        if (Keyboard.isKeyPressed(Keys.NUM_4)) {foxAnimation.trigger("frighten");}
        if (Keyboard.isKeyPressed(Keys.NUM_5)) {foxAnimation.trigger("sleep");}
        if (Keyboard.isKeyPressed(Keys.NUM_6)) {foxAnimation.trigger("rest");}
        if (Keyboard.isKeyPressed(Keys.NUM_7)) {foxAnimation.trigger("sleep-rest");}
        if (Keyboard.isKeyPressed(Keys.NUM_8)) {foxAnimation.trigger("rest-look");}
        if (Keyboard.isKeyPressed(Keys.NUM_9)) {foxAnimation.trigger("walk-idle");}

    }
}
