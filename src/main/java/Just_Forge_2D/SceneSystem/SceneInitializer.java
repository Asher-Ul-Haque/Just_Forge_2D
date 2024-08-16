package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;

public abstract class SceneInitializer
{
    public GameObject master;
    public abstract void init(Scene SCENE);
    public abstract void loadResources(Scene SCENE);
    public abstract void editorGUI();
    public void update(float DELTA_TIME){}
    public void editorUpdate(float DELTA_TIME){}
}
