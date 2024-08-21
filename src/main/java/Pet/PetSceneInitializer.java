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
        AssetPool.addSpriteSheet("Fox Sprites", new SpriteSheet(AssetPool.getTexture("Assets/Textures/Fox Sprite Sheet.png"), 32, 32, 98, 0));
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
