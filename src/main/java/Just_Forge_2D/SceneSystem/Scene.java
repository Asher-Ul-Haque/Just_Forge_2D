package SceneSystem;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Mouse;
import PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import PhysicsSystem.PhysicsSystemManager;
import RenderingSystem.Renderer;
import Utils.AssetPool;
import Utils.Logger;

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
    private final PhysicsSystemManager physics;
    private final List<GameObject> pendingObjects;

    private final String name;


    // - - - | Functions | - - -


    // - - - Now presenting: useful constructor
    public Scene(SceneInitializer INITIALIZER, String NAME)
    {
        this.initializer = INITIALIZER;
        this.physics = new PhysicsSystemManager(this, INITIALIZER.physicsWorld);
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
            if (this.renderer != null && go.hasComponent(SpriteComponent.class)) this.renderer.add(go);
            if (this.physics != null && go.hasComponent(RigidBodyComponent.class)) this.physics.add(go);
            go.start();
        }
        isRunning = true;
        Logger.FORGE_LOG_INFO("Scene: " + this + " Started");
    }

    public void update(float DELTA_TIME)
    {
        this.camera.adjustProjection();
        if (!this.isRunning) return;
        if (this.physics != null) this.physics.update();
        for (int i = 0; i < gameObjects.size(); ++i)
        {
            GameObject go = gameObjects.get(i);
            go.update(DELTA_TIME);

            if (go.isDead())
            {
                gameObjects.remove(i);
                if (this.physics != null) this.physics.destroyGameObject(go);
                if (this.renderer != null) this.renderer.destroyGameObject(go);
                i--;
            }
        }

        for (GameObject go : pendingObjects)
        {
            gameObjects.add(go);
            go.start();
            if (this.renderer != null) this.renderer.destroyGameObject(go);
            if (this.physics != null) this.physics.destroyGameObject(go);
        }
        pendingObjects.clear();
    }

    public void render(float DELTA_TIME)
    {
        if (this.renderer != null) this.renderer.render();
    }

    public void init()
    {
        this.camera = Mouse.getWorldCamera();
        this.initializer.loadResources(this);
        for (GameObject g : this.getGameObjects())
        {
            if (g.getComponent(SpriteComponent.class) != null)
            {
                SpriteComponent spr = g.getComponent(SpriteComponent.class);
                if (spr.getTexture() != null)
                {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if (g.getComponent(AnimationComponent.class) != null)
            {
                AnimationComponent stateMachine = g.getComponent(AnimationComponent.class);
                stateMachine.refreshTextures();
            }
        }
        this.initializer.init(this);
        if (!EditorSystemManager.isRelease) SceneSystemManager.createMaster(this);
        Logger.FORGE_LOG_INFO("Scene: " + this.initializer + " Initialized");
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

            if (this.renderer != null) this.renderer.add(go);
            if (this.physics != null) this.physics.add(go);
        }
        pendingObjects.clear();
    }


    public List<GameObject> getGameObjects()
    {
        return this.gameObjects;
    }

    public List<GameObject> getGameObjects(List<Class<? extends Component>> TYPES)
    {
        if (TYPES.isEmpty()) return getGameObjects();
        List<GameObject> returnList = new ArrayList<>();
        for (GameObject g : this.gameObjects)
        {
            boolean added = false;
            for (Class<? extends Component> type : TYPES)
            {
                if (g.hasComponent(type) && !added)
                {
                    returnList.add(g);
                    added = true;
                }
            }
        }
        return returnList;
    }

    public List<GameObject> getGameObjects(String NAME)
    {
        if (NAME.isEmpty() || NAME.isBlank()) return getGameObjects();
        List<GameObject> returnList = new ArrayList<>();
        for (GameObject g : this.gameObjects)
        {
            if (g.toString().trim().toLowerCase().startsWith(NAME.toLowerCase().trim())) returnList.add(g);
        }
        return returnList;
    }

    public List<GameObject> getGameObjects(List<Class<? extends Component>> TYPES, String NAME)
    {
        if (TYPES.isEmpty()) return getGameObjects(NAME);
        if (NAME.isEmpty() || NAME.isBlank()) return getGameObjects(TYPES);

        List<GameObject> returnList = new ArrayList<>();
        for (GameObject g : this.gameObjects)
        {
            boolean added = false;
            for (Class<? extends Component> type : TYPES)
            {
                if (g.hasComponent(type) && g.toString().trim().toLowerCase().startsWith(NAME.toLowerCase().trim()) && !added)
                {
                    returnList.add(g);
                    added = true;
                }
            }
        }
        return returnList;
    }


    public PhysicsSystemManager getPhysics()
    {
        return this.physics;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public String getSavePath()
    {
        return this.initializer.savePath;
    }

    public void setSavePath(String PATH)
    {
        this.initializer.savePath = PATH;
    }

    public Renderer getRenderer()
    {
        return this.renderer;
    }
}