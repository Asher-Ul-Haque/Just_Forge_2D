package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;

public abstract class SceneInitializer
{
    public GameObject master;
    public abstract void init(SceneManager SCENE);
    public abstract void loadResources(SceneManager SCENE);
    public abstract void editorGUI();
}
