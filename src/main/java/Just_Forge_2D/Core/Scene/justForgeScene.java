package Just_Forge_2D.Core.Scene;

import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Core.justForgeCamera;
import Just_Forge_2D.Renderer.justForgeRenderer;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

// - - - Abstract class for all the scenes
public abstract class justForgeScene
{
    // - - - Private Variables - - -

    // - - - Basic
    protected justForgeCamera camera;
    private boolean isRunning = false;

    // - - - ALl the objects
    protected List<justForgeGameObject> gameObjects = new ArrayList<>();
    protected justForgeGameObject activeGameObject = null;

    // - - - Scene Rendering
    protected justForgeRenderer renderer = new justForgeRenderer();


    // - - - | Functions | - - -


    // - - - Useless constructor
    public justForgeScene() {}


    // - - - Use the Scene - - -

    public void start()
    {
        for (justForgeGameObject go : gameObjects)
        {
            go.start();
            this.renderer.add(go);
        }
        isRunning = true;
    }

    public abstract void update(double DELTA_TIME);
    public void init(){}


    // - - - Getters and Setters - - -

    public void addGameObject(justForgeGameObject GAME_OBJECT)
    {
        gameObjects.add(GAME_OBJECT);
        if (!isRunning)
        {
            return;
        }
        GAME_OBJECT.start();
        this.renderer.add(GAME_OBJECT);
    }

    public justForgeCamera getCamera()
    {
        return this.camera;
    }

    public void sceneGUI()
    {
        if (activeGameObject != null)
        {
            ImGui.begin("Inspector");
            activeGameObject.editorGUI();
            ImGui.end();
        }
        editorGUI();
    }

    public void editorGUI()
    {}
}
