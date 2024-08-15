package Just_Forge_2D.CoreSystems.SceneSystem;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.GameObject;

public abstract class SceneInitializer
{
    public GameObject master;
    public abstract void init(SceneManager SCENE);
    public abstract void loadResources(SceneManager SCENE);
    public abstract void editorGUI();
}
