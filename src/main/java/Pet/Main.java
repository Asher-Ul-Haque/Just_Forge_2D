package Pet;

import Just_Forge_2D.EditorSystem.EditorWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Forge;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import imgui.app.Application;
import imgui.app.Configuration;
import org.joml.Vector4f;

public class Main
{
    protected static WindowConfig config = new WindowConfig(320, 320, "Stuti Pet", true, 0, 0, 16f/9f, true, false, true, true, true, true, "/home/nadeem/Documents/Just_Forge_2D/src/main/java/Pet/Assets/Textures/icon.png");
    protected static Window petWindow;
    protected static Scene petScene;
//    static WindowConfig config = new WindowConfig(32, 32, "Stuti");
    public static void main(String[] args)
    {
        petWindow = new Window(config);
        petWindow.setClearColor(new Vector4f(0.5f, 0.5f, 0.5f, 0.0f));
        AssetPool.addShader("Default", "Assets/Shaders/default.glsl");
        petScene = SceneSystemManager.addScene(petScene, new PetSceneInitializer(), "Fox");
        GameObject fox = new GameObject("Fox");
        Sprite foxSprite = AssetPool.getSpriteSheet("Spritesheet").getSprite(1);
        SpriteComponent foxSpr = new SpriteComponent();
        foxSpr.setSprite(foxSprite);
        fox.addComponent(new SpriteComponent());
        fox.addComponent(foxSpr);
        TransformComponent a = new TransformComponent();
        fox.addComponent(a);
        petScene.addGameObject(fox);
        petScene.getRenderer().bindShader(AssetPool.getShader("Default"));
        fox.transform.position.x = 2.5f;
        fox.transform.position.y = 1.5f;
        fox.transform.scale.x = 1f;
        fox.transform.scale.y = 1f;

        while (!petWindow.shouldClose())
        {
            petScene.getCamera().position.x += 0.01f;
            petScene.getCamera().adjustProjection();
            petWindow.setPosition((int) petScene.getCamera().position.x, petWindow.getYPosition());
            petScene.update(petWindow.getDeltaTime());
            petWindow.loop();
            petScene.render(petWindow.getDeltaTime());
        }
    }
}