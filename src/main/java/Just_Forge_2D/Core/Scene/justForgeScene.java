package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Core.justForgeLogger;

import java.util.ArrayList;
import java.util.List;

// - - - Abstract class for all the scenes
public abstract class justForgeScene
{
    // - - - Private Variables - - -
    protected justForgeCamera camera;
    private boolean isRunning = false;
    protected List<justForgeGameObject> gameObjects = new ArrayList<>();

    // - - - Useless constructor
    public justForgeScene() {}
    public abstract void update(double DELTA_TIME);
    public void init(){}

    public void addGameObject(justForgeGameObject GAME_OBJECT)
    {
        gameObjects.add(GAME_OBJECT);
        if (!isRunning)
        {
            justForgeLogger.FORGE_LOG_WARNING("Trying to add more game objects when the scene is not running");
            return;
        }
        GAME_OBJECT.start();
    }

    public void start()
    {
        for (justForgeGameObject go : gameObjects)
        {
            go.start();
        }
        isRunning = true;
    }
}
