package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.GameObject;

public abstract class SceneInitializer
{
    public GameObject master;
    public abstract void init(Scene SCENE);
    public abstract void loadResources(Scene SCENE);
    public abstract void editorGUI();
}
