package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Physics.PhysicsSystem;
import Just_Forge_2D.Renderer.Renderer;

public abstract class SceneInitializer
{
    public GameObject master;
    public Renderer renderer;
    public PhysicsSystem physicsSystem;
    public abstract void init(Scene SCENE);
    public abstract void loadResources(Scene SCENE);
    public abstract void editorGUI();
    public abstract void assignRenderingSystem();
    public abstract void assignPhysicsSystem();
    public SceneInitializer()
    {
        assignRenderingSystem();
        assignPhysicsSystem();
    }
}
