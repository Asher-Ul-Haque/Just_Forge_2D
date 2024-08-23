package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneInitializer;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.WindowSystemManager;

public class TestSceneInitializer extends SceneInitializer {
    @Override
    public void init(Scene SCENE)
    {
        Logger.FORGE_LOG_TRACE("Hi");
    }

    @Override
    public void loadResources(Scene SCENE)
    {
        Logger.FORGE_LOG_TRACE("Loading Resources");
    }

    @Override
    public void editorGUI()
    {
    }

    public TestSceneInitializer()
    {
        this.renderer = WindowSystemManager.getRenderer(EditorManager.editorScreen);
        this.physicsWorld = new PhysicsWorld();
    }
}
