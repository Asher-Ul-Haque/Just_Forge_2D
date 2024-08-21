package Just_Forge_2D.ForgeEditor.Windows;

import Just_Forge_2D.Forge;
import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.DefaultValues;

public class SplashScreenSceneConfig extends SceneInitializer
{
    @Override
    public void init(Scene SCENE)
    {

    }

    @Override
    public void loadResources(Scene SCENE)
    {
    }

    @Override
    public void editorGUI()
    {

    }

    public SplashScreenSceneConfig()
    {
        this.renderer = Forge.getRenderer(EditorManager.splashScreen);
        this.physicsWorld = new PhysicsWorld();
    }
}
