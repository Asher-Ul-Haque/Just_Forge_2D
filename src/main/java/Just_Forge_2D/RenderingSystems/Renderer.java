package Just_Forge_2D.RenderingSystems;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// - - - the mighty renderer
public class Renderer
{
    // - - - private variables
    private final String window;
    private final int MAX_BATCH_SIZE = Configurations.MAX_BATCH_SIZE;
    private final List<RenderBatch> batches;
    private static Shader currentShader;
    protected Scene currentScene;


    // - - - | Functions | - - -


    // - - - constructor
    public Renderer(String WINDOW_TITLE)
    {
        Logger.FORGE_LOG_INFO("Creating Rendering System for Window of title : " + WINDOW_TITLE);
        this.window = WINDOW_TITLE;
        this.batches = new ArrayList<>();
    }


    // - - - add Game Objects - - -

    public void addGameObject(GameObject GAME_OBJECT)
    {
        Logger.FORGE_LOG_DEBUG("Adding " + GAME_OBJECT + " to renderer for window: " + this.window);
        SpriteComponent sprite = GAME_OBJECT.getCompoent(SpriteComponent.class);

        if (sprite != null)
        {
            addSprite(sprite);
        }
    }

    private void addSprite(SpriteComponent SPRITE)
    {
        boolean added = false;
        for (RenderBatch batch : batches)
        {
            if (batch.hasRoom && batch.getLayer() == SPRITE.gameObject.transform.layer)
            {
                Texture texture = SPRITE.getTexture();
                if (texture == null || (batch.hasTexture(texture) || batch.hasTextureRoom()))
                {
                    batch.addSprite(SPRITE);
                    added = true;
                    break;
                }
            }
        }

        if (!added)
        {
            Logger.FORGE_LOG_DEBUG("Batch ran out of room, creating new batch for layer: " + SPRITE.gameObject.transform.layer);
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, SPRITE.gameObject.transform.layer, this);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(SPRITE);
            Collections.sort(batches);
        }
    }

    // - - - Destroy Game Object
    public void destroyGameObject(GameObject GO)
    {
        Logger.FORGE_LOG_DEBUG("Destroying Game Object from the scene: " + GO);
        if (GO.getCompoent(SpriteComponent.class) == null) return;
        for (RenderBatch batch: batches)
        {
            if (batch.destroyIfExists(GO))
            {
                return;
            }
        }
    }

    // - - - use
    public void render()
    {
        for (int i = 0; i < batches.size(); ++i)
        {
            RenderBatch batch = batches.get(i);
            batch.render();
        }
    }


    // - - - Shader - - -

    public void bindShader(Shader SHADER)
    {
        currentShader = SHADER;
        currentShader.use();
    }

    public Shader getCurrentShader()
    {
        return currentShader;
    }

    @Override
    public String toString()
    {
        return "Rendering System slave to " + this.window;
    }

    public void setCurrentScene(Scene CURRENT_SCENE)
    {
        Logger.FORGE_LOG_DEBUG(this + " Setting Current Scene to : " + CURRENT_SCENE);
        if (CURRENT_SCENE == this.currentScene)
        {
            Logger.FORGE_LOG_WARNING(this + " already has current scene set to: " + CURRENT_SCENE);
            return;
        }
        this.currentScene = CURRENT_SCENE;
    }

    public Scene getCurrentScene()
    {
        if (this.currentScene == null)
        {
            Logger.FORGE_LOG_WARNING("Current Scene is null for : " + this);
        }
        return this.currentScene;
    }
}