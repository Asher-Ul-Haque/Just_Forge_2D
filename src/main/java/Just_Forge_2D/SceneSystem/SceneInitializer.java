package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.RenderingSystem.Renderer;

public abstract class SceneInitializer
{
    public GameObject master;
    public Renderer renderer;
    public PhysicsWorld physicsWorld;
    public String savePath = EditorSystemManager.projectDir + "/Saves/" + this.getClass().getSimpleName() + ".justForgeFile";
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
