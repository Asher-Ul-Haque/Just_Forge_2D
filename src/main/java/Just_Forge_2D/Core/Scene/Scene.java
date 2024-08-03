package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Renderer.justForgeRenderer;
import Just_Forge_2D.Utils.JsonHandlers.justForgeComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.justForgeGameObjectJsonHandler;
import Just_Forge_2D.Utils.justForgeLogger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// - - - Abstract class for all the scenes
public abstract class Scene
{
    // - - - Private Variables - - -

    // - - - Basic
    protected Camera camera;
    private boolean isRunning = false;

    // - - - ALl the objects
    public List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

    // - - - Scene Rendering
    protected justForgeRenderer renderer = new justForgeRenderer();

    // - - - saving and loading
    protected boolean levelLoaded = false;


    // - - - | Functions | - - -


    // - - - Useless constructor
    public Scene() {}


    // - - - Use the Scene - - -

    public void start()
    {
        for (GameObject go : gameObjects)
        {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public abstract void update(float DELTA_TIME);
    public abstract void render(float DELTA_TIME);
    public void init(){}


    // - - -  Game Objects - - -

    public void addGameObject(GameObject GAME_OBJECT)
    {
        gameObjects.add(GAME_OBJECT);
        if (!isRunning)
        {
            return;
        }
        GAME_OBJECT.start();
        this.renderer.add(GAME_OBJECT);
    }

    public GameObject getGameObject(int GAME_OBJECT_ID)
    {
        Optional<GameObject> result = this.gameObjects.stream().filter(gameObject -> gameObject.getUniqueID() == GAME_OBJECT_ID).findFirst();
        return result.orElse(null);

        /*
        for (GameObject object : gameObjects)
        {
            if (object.getUniqueID() == GAME_OBJECT_ID)
            {
                return object;
            }
        }
        justForgeLogger.FORGE_LOG_ERROR("Found no game object with ID: " + GAME_OBJECT_ID + " in scene: " + this.toString());
        return null;
         */
    }

    // - - - Getters and Setters - - -

    public Camera getCamera()
    {
        return this.camera;
    }


    // - - - Editor GUI - - -


    public void editorGUI() {}


    // - - - Loading and Saving - - -

    public void load()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new justForgeComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new justForgeGameObjectJsonHandler())
                .create();
        String inFile = "";
        try
        {
            inFile = new String(Files.readAllBytes(Paths.get("Configurations/Levels/level.justForgeFile")));
        }
        catch (IOException e)
        {
            justForgeLogger.FORGE_LOG_ERROR("Couldnt read from file: Configurations/Levels/level.justForgeFile");
            justForgeLogger.FORGE_LOG_ERROR(e.getMessage());
        }

        if (!inFile.isEmpty())
        {
            int maxGoId = -1;
            int maxCompoId = -1;
            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for (GameObject object : objects)
            {
                addGameObject(object);

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
            this.levelLoaded = true;
        }
    }

    // - - - save
    public void save()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new justForgeComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new justForgeGameObjectJsonHandler())
                .create();
        justForgeLogger.FORGE_LOG_INFO("Saving scene...: " + this.toString());

        try
        {
            FileWriter writer = new FileWriter("Configurations/Levels/level.justForgeFile");
            List<GameObject> toSerialize = new ArrayList<>();
            for (GameObject obj : this.gameObjects)
            {
                if (obj.getSerializationStatus())
                {
                    toSerialize.add(obj);
                }
            }
            writer.write(gson.toJson(toSerialize));
            writer.close();
            justForgeLogger.FORGE_LOG_INFO("Saved scene: " + this.toString());
        }
        catch (IOException e)
        {
            justForgeLogger.FORGE_LOG_ERROR("Couldnt write to file");
            justForgeLogger.FORGE_LOG_ERROR(e.getMessage());
        }
    }
}
