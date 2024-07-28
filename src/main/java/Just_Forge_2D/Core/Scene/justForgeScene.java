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

// - - - Abstract class for all the scenes
public abstract class justForgeScene
{
    // - - - Private Variables - - -

    // - - - Basic
    protected Camera camera;
    private boolean isRunning = false;

    // - - - ALl the objects
    protected List<GameObject> gameObjects = new ArrayList<>();
    protected GameObject activeGameObject = null;

    // - - - Scene Rendering
    protected justForgeRenderer renderer = new justForgeRenderer();

    // - - - saving and loading
    protected boolean levelLoaded = false;


    // - - - | Functions | - - -


    // - - - Useless constructor
    public justForgeScene() {}


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

    public abstract void update(double DELTA_TIME);
    public void init(){}


    // - - - Getters and Setters - - -

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

    public Camera getCamera()
    {
        return this.camera;
    }


    // - - - Editor GUI - - -

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

        if (!inFile.equals(""))
        {
            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for (int i = 0; i < objects.length; ++i)
            {
                addGameObject(objects[i]);
            }
            this.levelLoaded = true;
        }
    }

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
            writer.write(gson.toJson(this.gameObjects));
            writer.close();
            justForgeLogger.FORGE_LOG_INFO("Saved scene: " + this.toString());
        }
        catch (IOException e)
        {
            justForgeLogger.FORGE_LOG_ERROR("Couldnt write to file");
            justForgeLogger.FORGE_LOG_ERROR(e.getMessage());
            e.printStackTrace();
        }
    }
}
