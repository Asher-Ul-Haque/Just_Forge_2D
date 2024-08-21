package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.JsonHandlers.ComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.GameObjectJsonHandler;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SceneSystemManager
{
    private static HashMap<String, Scene> allScenes = new HashMap<>();

    protected static void save(Scene SCENE)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .enableComplexMapKeySerialization()
                .create();
        Logger.FORGE_LOG_INFO("Saving scene...: " + SCENE);

        try
        {
            FileWriter writer = new FileWriter("Configurations/Levels/level.justForgeFile");
            List<GameObject> toSerialize = new ArrayList<>();
            for (GameObject obj : SCENE.getGameObjects())
            {
                if (obj.getSerializationStatus())
                {
                    toSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(toSerialize));
            writer.close();
            Logger.FORGE_LOG_INFO("Saved scene: " + SCENE);
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_ERROR("Couldn't write to file");
            Logger.FORGE_LOG_ERROR(e.getMessage());
        }
    }

    protected static void load(Scene SCENE)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .create();
        String inFile = "";
        try
        {
            inFile = new String(Files.readAllBytes(Paths.get(Configurations.DEFAULT_SAVE_FILE)));
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_ERROR("Couldn't read from file: " + Configurations.DEFAULT_SAVE_FILE);
            Logger.FORGE_LOG_ERROR(e.getMessage());
        }

        if (!inFile.isEmpty())
        {
            int maxGoId = -1;
            int maxCompoId = -1;
            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for (GameObject object : objects)
            {
                SCENE.addGameObject(object);

                for (Component component : object.getComponents())
                {
                    maxCompoId = Math.max(maxCompoId, component.getUniqueID());
                }
                maxGoId = Math.max(maxGoId, object.getUniqueID());
            }

            maxGoId++;
            maxCompoId++;
            GameObject.init(maxGoId);
            Component.init(maxCompoId);
        }
    }

    public static Scene addScene(Scene SCENE, SceneInitializer INITIALIZER, String ID)
    {
        if (SCENE != null)
        {
            Logger.FORGE_LOG_INFO("Clearing Scene Catch from previous run");
            SCENE.destroy();
        }

        Logger.FORGE_LOG_INFO("Adding " + SCENE + " to Scene Manager");

        SCENE = new Scene(INITIALIZER);
        allScenes.put(ID, SCENE);
        SCENE.load();
        SCENE.init();

        return SCENE;
    }

    public static Scene getScene(String ID)
    {
        Scene returnScene = allScenes.get(ID);
        if (returnScene == null)
        {
            Logger.FORGE_LOG_ERROR("No such Scene in the Scene Manager");
        }
        return returnScene;
    }

    public static List<Scene> getAllScenes()
    {
        return new ArrayList<>(allScenes.values());
    }
}
