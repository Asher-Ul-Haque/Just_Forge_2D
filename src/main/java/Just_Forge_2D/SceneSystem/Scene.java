package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsManager;
import Just_Forge_2D.RenderingSystems.Renderer;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.JsonHandlers.ComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.GameObjectJsonHandler;
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

    // - - - Scene Rendering
    private Renderer renderer;
    private SceneInitializer initializer;

    // - - - saving and loading
    private PhysicsManager physics;
    private List<GameObject> pendingObjects;


    // - - - | Functions | - - -


    // - - - Now presenting: useful constructor
    public Scene(SceneInitializer INITIALIZER)
    {
        this.initializer = INITIALIZER;
        this.physics = new PhysicsManager();
        this.renderer = new Renderer();
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
        this.pendingObjects = new ArrayList<>();
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
        Logger.FORGE_LOG_INFO("Scene: " + this.initializer + " Started");
    }

    public void update(float DELTA_TIME)
    {
        this.camera.adjustProjection();
        this.physics.update(DELTA_TIME);
        this.initializer.update(DELTA_TIME);
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

        for (GameObject go : pendingObjects)
        {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }
        pendingObjects.clear();
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
        Logger.FORGE_LOG_INFO("Scene: " + this.initializer + " Initialized");
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
        if (isRunning)
        {
            pendingObjects.add(GAME_OBJECT);
        }
        else
        {
            gameObjects.add(GAME_OBJECT);
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

    public GameObject getGameObject(String NAME)
    {
        for (GameObject object : gameObjects)
        {
            if (object.name.equals(NAME))
            {
                return object;
            }
        }
        Logger.FORGE_LOG_WARNING("Found no game object with Name: " + NAME + " in scene: " + this);
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
        this.initializer.editorUpdate(DELTA_TIME);
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

        for (GameObject go : pendingObjects)
        {
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics.add(go);
        }
        pendingObjects.clear();
    }

    // - - - save
    public void save()
    {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .enableComplexMapKeySerialization()
                .create();
        Logger.FORGE_LOG_INFO("Saving scene...: " + this.initializer);

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
            Logger.FORGE_LOG_INFO("Saved scene: " + this.initializer);
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
        Logger.FORGE_LOG_INFO("Scene Destroyed " + this.initializer);
    }

    public PhysicsManager getPhysics()
    {
        return this.physics;
    }
}
