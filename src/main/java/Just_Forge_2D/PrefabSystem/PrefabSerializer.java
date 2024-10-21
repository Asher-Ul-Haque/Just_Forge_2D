package Just_Forge_2D.PrefabSystem;


import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.JsonHandlers.ComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.GameObjectJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.PrefabJsonHandler;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class PrefabSerializer
{
    public static void savePrefabs(String FILE_PATH)
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(Prefab.class, new PrefabJsonHandler())
                .enableComplexMapKeySerialization()
                .create();

        String data = gson.toJson(PrefabManager.prefabRegistry);
        try
        {
            FileWriter writer = new FileWriter(FILE_PATH);
            writer.write(data);
            writer.close();
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_WARNING(e.getMessage());
        }
    }

    public static void loadPrefabs(String FILE_PATH)
    {
        try
        {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                    .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                    .registerTypeAdapter(Prefab.class, new PrefabJsonHandler())
                    .create();
            Type type = new TypeToken<Map<String, Prefab>>() {}.getType();
            Map<String, Prefab> deserializedRegistry = gson.fromJson(json, type);
            PrefabManager.prefabRegistry.putAll(deserializedRegistry);
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_FATAL(e.getMessage());
        }
    }
}
