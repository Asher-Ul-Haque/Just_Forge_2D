package Just_Forge_2D.Core.SceneSystem;

import Just_Forge_2D.Core.EntityComponentSystem.GameObject;

public abstract class SceneInitializer
{
    public GameObject master;
    public abstract void init(SceneManager SCENE);
    public abstract void loadResources(SceneManager SCENE);
    public abstract void editorGUI();
}
