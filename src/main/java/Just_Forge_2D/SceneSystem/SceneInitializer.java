package SceneSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import PhysicsSystem.PhysicsWorld;
import RenderingSystem.Renderer;

public abstract class SceneInitializer
{
    public GameObject master;
    public Renderer renderer;
    public PhysicsWorld physicsWorld;
    public String savePath = EditorSystemManager.projectDir + "/Scenes/" + this.getClass().getSimpleName() + ".justForgeFile";
    public abstract void init(Scene SCENE);
    public abstract void loadResources(Scene SCENE);
    public void editorGUI(){};
    public void assignRenderingSystem()
    {
        this.renderer = new Renderer();
    }
    public void assignPhysicsSystem()
    {
        this.physicsWorld = new PhysicsWorld();
    }
    public SceneInitializer()
    {
        assignRenderingSystem();
        assignPhysicsSystem();
    }
}
