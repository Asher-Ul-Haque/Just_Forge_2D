package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystems.Renderer;

import java.util.ArrayList;

public abstract class SceneInitializer
{
    public GameObject master;
    public Renderer renderer;
    public abstract void init(Scene SCENE);
    public abstract void loadResources(Scene SCENE);
    public abstract void editorGUI();
    public void update(float DELTA_TIME){}
    public void editorUpdate(float DELTA_TIME){}
}
