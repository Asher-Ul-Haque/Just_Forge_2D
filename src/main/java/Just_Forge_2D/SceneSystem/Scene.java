package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsSystem;
import Just_Forge_2D.RenderingSystem.Renderer;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

// - - - Abstract class for all the scenes
public class Scene
{
    // - - - Private Variables - - -

    // - - - Basic
    private Camera camera;
    protected boolean isRunning = false;

    // - - - ALl the objects
    private final List<GameObject> gameObjects;

    // - - - Scene Rendering
    private final Renderer renderer;
    private final SceneInitializer initializer;

    // - - - saving and loading
    private final PhysicsSystem physics;
    private final List<GameObject> pendingObjects;

    private final String name;


    // - - - | Functions | - - -


    // - - - Now presenting: useful constructor
    public Scene(SceneInitializer INITIALIZER, String NAME)
    {
        this.initializer = INITIALIZER;
        this.physics = INITIALIZER.physicsSystem;
        this.renderer = INITIALIZER.renderer;
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
        this.pendingObjects = new ArrayList<>();
        this.name = NAME;
    }


    // - - - Use the Scene - - -

    public void start()
    {
        Logger.FORGE_LOG_INFO("Starting Scene : " + this.initializer);
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.start();
            if (this.renderer != null) this.renderer.add(go);
            if (this.physics != null) this.physics.add(go);
        }
        isRunning = true;
        Logger.FORGE_LOG_INFO("Scene: " + this + " Started");
    }

    public void update(float DELTA_TIME)
    {
        this.camera.adjustProjection();
        if (!this.isRunning) return;
        if (this.physics != null) this.physics.update(DELTA_TIME);
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.update(DELTA_TIME);

            if (go.isDead())
            {
                gameObjects.remove(i);
                if (this.renderer != null) this.renderer.destroyGameObject(go);
                if (this.physics != null) this.physics.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects)
        {
            gameObjects.add(go);
            go.start();
            if (this.renderer != null) this.renderer.add(go);
            if (this.physics != null) this.physics.add(go);
        }
        pendingObjects.clear();
    }

    public void render(float DELTA_TIME)
    {
        if (this.renderer != null) this.renderer.render();
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


    public void editorUpdate(float DELTA_TIME)
    {
        this.camera.adjustProjection();
        if (!this.isRunning) return;
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.editorUpdate(DELTA_TIME);

            if (go.isDead())
            {
                gameObjects.remove(i);
                if (this.renderer != null) this.renderer.destroyGameObject(go);
                if (this.physics != null) this.physics.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects)
        {
            gameObjects.add(go);
            go.start();
            if (this.renderer != null) this.renderer.add(go);
            if (this.physics != null) this.physics.add(go);
        }
        pendingObjects.clear();
    }


    public List<GameObject> getGameObjects()
    {
        return this.gameObjects;
    }

    public PhysicsSystem getPhysics()
    {
        return this.physics;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}
