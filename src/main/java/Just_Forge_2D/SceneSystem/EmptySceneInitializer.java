package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsWorld;
import Just_Forge_2D.RenderingSystem.Renderer;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.AssetPool;
import org.joml.Vector4f;

public class EmptySceneInitializer extends SceneInitializer
{
    @Override
    public void init(Scene SCENE)
    {
        MainWindow.get().setClearColor(new Vector4f(1.0f));
    }

    @Override
    public void loadResources(Scene SCENE)
    {
    }
}
