package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EditorSystem.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem.GizmoSystemComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.NonPickableComponent;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
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
import java.util.List;

public class SceneSystemManager
{
    public static void save(Scene SCENE)
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
            FileWriter writer = new FileWriter(SCENE.getSavePath());
            List<GameObject> toSerialize = new ArrayList<>();
            for (GameObject obj : SCENE.getGameObjects())
            {
                if (!obj.getSerializationStatus())
                {
                    Logger.FORGE_LOG_TRACE("Not saving: " + obj.name);
                    continue;
                }
                toSerialize.add(obj);
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

    public static void load(Scene SCENE)
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .create();
        String inFile = "";
        try
        {
            inFile = new String(Files.readAllBytes(Paths.get(SCENE.getSavePath())));
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_ERROR("Couldn't read from file: " + SCENE.getSavePath());
            Logger.FORGE_LOG_ERROR(e.getMessage());
        }

        if (!inFile.isEmpty())
        {
            int maxGoId = -1;
            int maxCompoId = -1;
            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for (GameObject object : objects)
            {
                if (!object.getSerializationStatus())
                {
                    Logger.FORGE_LOG_TRACE("Not loading: " + object.name);
                    continue;
                }
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

    public static void destroy(Scene SCENE)
    {
        for (GameObject go : SCENE.getGameObjects())
        {
            go.destroy();
        }
        Logger.FORGE_LOG_INFO("Scene Destroyed " + SCENE);
    }

    public static void setPause(Scene SCENE, boolean REALLY)
    {
        Logger.FORGE_LOG_DEBUG("Setting Pause status of Scene : " + SCENE + " to : " + REALLY);
        if (SCENE.isRunning == REALLY)
        {
            Logger.FORGE_LOG_WARNING(SCENE + " has its pause status already set to : " + SCENE.isRunning);
            return;
        }
        SCENE.isRunning = REALLY;
    }

    public static boolean isRunning(Scene SCENE)
    {
        return SCENE.isRunning;
    }

    public static void createMaster(Scene SCENE)
    {
        GameObject master = new GameObject("Master");
        master.noSerialize();
        master.addComponent(new GridlinesComponent());
        master.addComponent(new EditorCameraComponent(SCENE.getCamera()));
        master.addComponent(new GizmoSystemComponent());
        master.addComponent(new MouseControlComponent());
        master.addComponent(new NonPickableComponent());
        SCENE.addGameObject(master);
    }
}
