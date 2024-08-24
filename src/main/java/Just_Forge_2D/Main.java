package Just_Forge_2D;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.AnimationSystem.AnimationState;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.InputSystem.MouseButtons;
import Just_Forge_2D.RenderingSystems.SpriteSheet;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import org.joml.Vector2f;
import org.joml.Vector4f;


public class Main {
    private static final WindowConfig CONFIG = new WindowConfig(
            150, 150, "Stuti Pet", true, 0, 0, 16f / 9f, true, false, true,
            false, true, true, "Assets/Textures/icon.png"
    );
    public static Window petWindow;
    public static Scene petScene;
    private static SpriteSheet foxSprites;
    private static AnimationComponent foxAnimation;

    // Global variables for random behavior
    private static float inactivityTimer = 0;
    private static final float inactivityThreshold = 60f;  // Time in seconds before rest
    private static final float restToSleepThreshold = 15f;  // Time in seconds before sleep after rest
    private static boolean isResting = false;
    private static boolean isSleeping = false;
    private static Vector2f targetPosition = null;

    public static void main(String[] args)
    {
        initialize();
        createFox();
        petScene.start();
        //EditorManager.run();

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

        fox.transform.position.set(2.3f, 2.5f);
        fox.transform.scale.set(5f, 5f);

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
        animate.addStateTrigger(play.title, frighten.title, "frighten");
        animate.addStateTrigger(lookAround.title, frighten.title, "frighten");
        animate.addStateTrigger(walk.title, frighten.title, "frighten");
        animate.addStateTrigger(idle.title, sleep.title, "sleep");
        animate.addStateTrigger(idle.title, rest.title, "rest");
        animate.addStateTrigger(lookAround.title, rest.title, "rest");
        animate.addStateTrigger(walk.title, rest.title, "rest");
        animate.addStateTrigger(sleep.title, rest.title, "sleep-rest");
        animate.addStateTrigger(rest.title, lookAround.title, "rest-look");
        return animate;
    }

    public static void update() {
        handleInput();
        handleRandomBehavior();  // Handle random behavior in each frame
        petWindow.loop();
    }

    private static void handleInput() {
        if (Keyboard.isKeyPressed(Keys.ESCAPE)) {
            petWindow.close();
        }

        // Reset inactivity timer on interaction
        if (Keyboard.isAnyKeyPressed() || Mouse.isAnyButtonDown()) {
            inactivityTimer = 0.0f;
        }
    }

    private static void updateInactivityTimer() {
        inactivityTimer += petWindow.getDeltaTime();
    }

    private static void handleRandomBehavior() {
        // Timer for inactivity
        inactivityTimer += petWindow.getDeltaTime();
        if (inactivityTimer > inactivityThreshold)
        {
            if (!isResting && !isSleeping)
            {
                // Rest and then sleep if no interaction for a long time
                foxAnimation.trigger("rest");
                isResting = true;
                inactivityTimer = 0; // Reset timer for rest-to-sleep transition
            }
            else if (isResting)
            {
                foxAnimation.trigger("sleep");
                isSleeping = true;
            }
        }

        // Randomly look around if idle
        if (foxAnimation.getCurrentStateTitle().equals("idle") && Math.random() < 0.01)
        {
            foxAnimation.trigger("look");
        }

        // Random movement to a new target position
        if (targetPosition == null && foxAnimation.getCurrentStateTitle().equals("idle") && Math.random() < 0.01) {
            targetPosition = new Vector2f(
                    petWindow.getXPosition() + (float) (Math.random() * 600 - 300),
                    petWindow.getYPosition() + (float) (Math.random() * 600 - 300)
            );

            petScene.getGameObject("Fox").transform.scale.x = targetPosition.x / Math.abs(targetPosition.x) * petScene.getGameObject("Fox").transform.scale.x;
            foxAnimation.trigger("walk");
        }

        // Handle double-click for frighten
        if (Mouse.isMouseButtonDown(MouseButtons.LEFT) && Mouse.isMouseButtonDown(MouseButtons.RIGHT) || Keyboard.isKeyPressed(Keys.SPACE))
        {
            foxAnimation.trigger("frighten");
            targetPosition = new Vector2f(
                    petWindow.getXPosition() + (float) (Math.random() * 600 - 300),
                    petWindow.getYPosition() + (float) (Math.random() * 600 - 300)
            );
        }

        // Move towards target position
        if (targetPosition != null) {
            Vector2f currentPosition = new Vector2f(petWindow.getXPosition(), petWindow.getYPosition());
            Vector2f direction = new Vector2f(targetPosition).sub(currentPosition);
            if (direction.length() < 1) {
                // Stop moving if close to the target
                targetPosition = null;
                foxAnimation.trigger("walk-idle");
            } else {
                direction.normalize().mul(2); // Move at a constant speed
                petWindow.setPosition(
                        (int) (currentPosition.x + direction.x),
                        (int) (currentPosition.y + direction.y)
                );
            }
        }

        // Handle click for play (if not sleeping)
        if (Mouse.isAnyButtonDown() && !isSleeping) {
            foxAnimation.trigger("play");
        }

        // Wake up when clicked while sleeping
        if (isSleeping && Mouse.isMouseButtonBeginPress(MouseButtons.LEFT)) {
            foxAnimation.trigger("sleep-rest");
            isSleeping = false;
            foxAnimation.trigger("idle");
            isResting = false;
        }
    }
}