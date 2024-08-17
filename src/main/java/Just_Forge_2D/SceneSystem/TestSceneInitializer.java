package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.Forge;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.EditorWindow;

public class TestSceneInitializer extends SceneInitializer{
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
}
