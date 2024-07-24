package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Renderer.justForgeRenderer;
import Just_Forge_2D.Utils.justForgeLogger;

import java.util.ArrayList;
import java.util.List;

// - - - Abstract class for all the scenes
public abstract class justForgeScene
{
    // - - - Private Variables - - -

    // - - - Basic
    protected justForgeCamera camera;
    private boolean isRunning = false;

    // - - - ALl the objects
    protected List<justForgeGameObject> gameObjects = new ArrayList<>();

    // - - - Scene Rendering
    protected justForgeRenderer renderer = new justForgeRenderer();

    // - - - Useless constructor
    public justForgeScene() {}
    public abstract void update(double DELTA_TIME);
    public void init(){}

    public void addGameObject(justForgeGameObject GAME_OBJECT)
    {
        gameObjects.add(GAME_OBJECT);
        if (!isRunning)
        {
            return;
        }
        this.renderer.add(GAME_OBJECT);
        GAME_OBJECT.start();
    }

    public void start()
    {
        for (justForgeGameObject go : gameObjects)
        {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public justForgeCamera getCamera()
    {
        return this.camera;
    }
}
