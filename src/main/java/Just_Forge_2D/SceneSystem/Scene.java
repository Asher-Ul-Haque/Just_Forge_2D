package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsManager;
import Just_Forge_2D.RenderingSystems.Renderer;
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
    private boolean isRunning;

    // - - - All the objects
    private final List<GameObject> gameObjects;

    // - - - Scene Rendering
    private Renderer renderer;
    private final SceneInitializer initializer;

    // - - - saving and loading
    private final PhysicsManager physics;
    private final List<GameObject> pendingObjects;


    // - - - | Functions | - - -


    // - - - Now presenting: useful constructor
    public Scene(SceneInitializer INITIALIZER)
    {
        this.initializer = INITIALIZER;
        this.physics = new PhysicsManager(this, initializer.physicsWorld);
        this.renderer = INITIALIZER.renderer;
        this.gameObjects = new ArrayList<>();
        this.isRunning = false;
        this.pendingObjects = new ArrayList<>();
    }


    // - - - Use the Scene - - -

    public void init()
    {
        Logger.FORGE_LOG_INFO("Initializing new scene: " + this.initializer);
        this.renderer.setCurrentScene(this);
        this.camera = new Camera(new Vector2f(0, 0));
        this.initializer.loadResources(this);
        this.initializer.init(this);
    }

    public void start()
    {
        Logger.FORGE_LOG_INFO("Starting Scene : " + this.initializer);
        // NOTE: do not change to enhanced for loop
        for (int i = 0; i < this.gameObjects.size(); ++i)
        {
            GameObject go = this.gameObjects.get(i);
            go.start();
            this.renderer.addGameObject(go);
            this.physics.add(go);
        }
        this.isRunning = true;
    }

    public void update(float DELTA_TIME)
    {
        if (!this.isRunning) return;
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
            this.renderer.addGameObject(go);
            this.physics.add(go);
        }
        pendingObjects.clear();
    }

    public void render(float DELTA_TIME)
    {
        this.renderer.render();
    }

    public void editorUpdate(float DELTA_TIME)
    {
        if (!this.isRunning) return;
        this.camera.adjustProjection();
        this.initializer.editorUpdate(DELTA_TIME);
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
//            go.editorUpdate(DELTA_TIME);

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
            this.renderer.addGameObject(go);
            this.physics.add(go);
        }
        pendingObjects.clear();
    }

    // - - -  Game Objects - - -

    public GameObject createGameObject(String NAME)
    {
        GameObject go = new GameObject(NAME);
        go.addComponent(new TransformComponent());
        go.transform = go.getComponent(TransformComponent.class);
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

    public List<GameObject> getGameObjects()
    {
        return this.gameObjects;
    }

    // - - - Loading and Saving - - -

    // - - - load
    public void load()
    {
        SceneSystemManager.load(this);
    }

    // - - - save
    public void save()
    {
        SceneSystemManager.save(this);
    }


    // - - - Pause and Delete the Scene - - -

    // - - - pause
    public void pauseScene(boolean REALLY)
    {
        Logger.FORGE_LOG_DEBUG("Setting Pause status of Scene : " + this + " to : " + REALLY);
        if (this.isRunning == REALLY)
        {
            Logger.FORGE_LOG_WARNING(this + " has its pause status already set to : " + this.isRunning);
            return;
        }
        this.isRunning = REALLY;
    }

    public boolean getPause()
    {
        return this.isRunning;
    }

    // - - - destroy
    public void destroy()
    {
        for (GameObject go : this.gameObjects)
        {
            go.destroy();
        }
        Logger.FORGE_LOG_INFO("Scene Destroyed " + this.initializer);
    }

    // - - - Physics System
    public PhysicsManager getPhysics()
    {
        return this.physics;
    }


    // - - - Renderer - - -

    public void switchRenderer(Renderer RENDERER)
    {
        this.renderer = RENDERER;
    }

    public Renderer getRenderer()
    {
        return this.renderer;
    }
}