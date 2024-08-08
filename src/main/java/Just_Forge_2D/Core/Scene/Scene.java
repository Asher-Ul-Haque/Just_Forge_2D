package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.TransformComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Physics.PhysicsSystem;
import Just_Forge_2D.Renderer.Renderer;
import Just_Forge_2D.Utils.JsonHandlers.justForgeComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.justForgeGameObjectJsonHandler;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.joml.Vector2f;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// - - - Abstract class for all the scenes
public class Scene
{
    // - - - Private Variables - - -

    // - - - Basic
    private Camera camera;
    private boolean isRunning = false;

    // - - - ALl the objects
    private List<GameObject> gameObjects;
    private GameObject activeGameObject = null;

    // - - - Scene Rendering
    private Renderer renderer;
    private SceneInitializer initializer;

    // - - - saving and loading
    private PhysicsSystem physics;


    // - - - | Functions | - - -


    // - - - Now presenting: useful constructor
    public Scene(SceneInitializer INITIALIZER)
    {
        this.initializer = INITIALIZER;
        this.physics = new PhysicsSystem();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
    }


    // - - - Use the Scene - - -

    public void start()
    {
        // NOTE: do not change to enhanced for loop
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }
        isRunning = true;
    }

    public void update(float DELTA_TIME)
    {
        this.camera.adjustProjection();
        this.physics.update(DELTA_TIME);
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.update(DELTA_TIME);

            if (go.isDead())
            {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics.destroyGameObject(go);
                i--;
            }
        }
    }

    public void render(float DELTA_TIME)
    {
        this.renderer.render();
    }

    public void init()
    {
        this.camera = new Camera(new Vector2f(0, 0));
        this.initializer.loadResources(this);
        this.initializer.init(this);
    }


    // - - -  Game Objects - - -

    public GameObject createGameObject(String NAME)
    {
        GameObject go = new GameObject(NAME);
        go.addComponent(new TransformComponent());
        go.transform = go.getCompoent(TransformComponent.class);
        return go;
    }

    public void addGameObject(GameObject GAME_OBJECT)
    {
        gameObjects.add(GAME_OBJECT);
        if (isRunning)
        {
            GAME_OBJECT.start();
            this.renderer.add(GAME_OBJECT);
            this.physics.add(GAME_OBJECT);
        }
    }

    public GameObject getGameObject(int GAME_OBJECT_ID)
    {
        for (GameObject object : gameObjects)
        {
            if (object.getUniqueID() == GAME_OBJECT_ID)
            {
                return object;
            }
        }
        Logger.FORGE_LOG_WARNING("Found no game object with ID: " + GAME_OBJECT_ID + " in scene: " + this);
        return null;
    }

    // - - - Getters and Setters - - -

    public Camera getCamera()
    {
        return this.camera;
    }

    // - - - Editor GUI
    public void editorGUI()
    {
        this.initializer.editorGUI();
    }


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
            Logger.FORGE_LOG_ERROR("Couldn't read from file: Configurations/Levels/level.justForgeFile");
            Logger.FORGE_LOG_ERROR(e.getMessage());
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
        }
    }

    public void editorUpdate(float DELTA_TIME)
    {
        this.camera.adjustProjection();
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(DELTA_TIME);

            if (go.isDead())
            {
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics.destroyGameObject(go);
                i--;
            }
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
        Logger.FORGE_LOG_INFO("Saving scene...: " + this);

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
            Logger.FORGE_LOG_INFO("Saved scene: " + this);
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_ERROR("Couldn't write to file");
            Logger.FORGE_LOG_ERROR(e.getMessage());
        }
    }

    public List<GameObject> getGameObjects()
    {
        return this.gameObjects;
    }

    public void destroy()
    {
        for (GameObject go : this.gameObjects)
        {
            go.destroy();
        }
    }
}
