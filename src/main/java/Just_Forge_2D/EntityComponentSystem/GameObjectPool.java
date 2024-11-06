package Just_Forge_2D.EntityComponentSystem;

import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GameObjectPool
{
    public static Map<String, PoolInfo> gameObjectPools = new HashMap<>();

    public static GameObject spawnObject(String POOL_NAME, TransformComponent TRANSFORM)
    {
        PoolInfo pool = gameObjectPools.get(POOL_NAME);

        // - - - make a new pool if it doesnt exist
        if (pool == null)
        {
            pool = new PoolInfo();
            gameObjectPools.put(POOL_NAME, pool);
        }

        // - - - check if there are any inactive objects
        GameObject spawn = null;
        if (!pool.inactiveObjects.isEmpty())
        {
            // - - - Reuse an inactive object from the pool
            spawn = pool.inactiveObjects.poll();
            GameWindow.getCurrentScene().addGameObject(spawn);
        }
        else
        {
            // No inactive objects, so create a new one
            spawn = PrefabManager.generateObject(TRANSFORM.scale.x, TRANSFORM.scale.y);
        }
        spawn.transform = TRANSFORM.copy();

        return spawn;
    }

    public static void despawnObject(String POOL_NAME, GameObject SPAWN)
    {
        PoolInfo poolInfo = gameObjectPools.get(POOL_NAME);

        if (poolInfo == null)
        {
            Logger.FORGE_LOG_WARNING(POOL_NAME + " : no such pool exists");
            return;
        }

        poolInfo.inactiveObjects.add(SPAWN);
        GameWindow.getCurrentScene().removeGameObject(SPAWN);
    }


    private static class PoolInfo
    {
        protected Queue<GameObject> inactiveObjects = new LinkedList<>();
    }
}
