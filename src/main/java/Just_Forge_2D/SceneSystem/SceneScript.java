package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.RenderingSystem.Renderer;

public abstract class SceneScript
{
    public GameObject master;
    public Renderer renderer;
    public PhysicsWorld physicsWorld;
    public String savePath = EditorSystemManager.projectDir + "/Scenes/" + this.getClass().getSimpleName() + ".justForgeFile";
    public abstract void init(Scene SCENE);
    public abstract void loadResources(Scene SCENE);
    public void editorGUI(){};
    public void update(float DELTA_TIME){};
    public void editorUpdate(float DELTA_TIME){};
    public void render(float DELTA_TIME){};
    public void assignRenderingSystem()
    {
        this.renderer = new Renderer();
    }
    public void assignPhysicsSystem()
    {
        this.physicsWorld = new PhysicsWorld();
    }
    public SceneScript()
    {
        assignRenderingSystem();
        assignPhysicsSystem();
    }
}
