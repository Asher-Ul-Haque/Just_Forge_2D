package Pet;

import Just_Forge_2D.Forge;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.RenderingSystems.SpriteSheet;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.Utils.AssetPool;

public class PetSceneInitializer extends SceneInitializer
{
    @Override
    public void init(Scene SCENE)
    {

    }

    @Override
    public void loadResources(Scene SCENE)
    {
        AssetPool.getShader("Default");
        AssetPool.addTexture("Fox", "/home/nadeem/Documents/Just_Forge_2D/src/main/java/Pet/Assets/Textures/Fox.png");
        AssetPool.addSpriteSheet("Fox Sprites", new SpriteSheet(AssetPool.getTexture("/home/nadeem/Documents/Just_Forge_2D/src/main/java/Pet/Assets/Textures/Fox.png"), 32, 16, 98, 16));
        AssetPool.addSpriteSheet("Spritesheet",new SpriteSheet(AssetPool.getTexture("Assets/Textures/spritesheet.png"), 16, 16, 26, 0));
    }

    @Override
    public void editorGUI()
    {

    }

    public PetSceneInitializer()
    {
        this.renderer = Forge.getRenderer(Main.petWindow);
        this.physicsWorld = new PhysicsWorld();
    }
}
