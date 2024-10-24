package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// - - - the mighty renderer
public class Renderer
{
    // - - - private variables
    private final int MAX_BATCH_SIZE = DefaultValues.MAX_BATCH_SIZE;
    private final List<RenderBatch> batches;
    private static Shader currentShader;


    // - - - | Functions | - - -


    // - - - constructor
    public Renderer()
    {
        this.batches = new ArrayList<>();
    }


    // - - - add Game Objects - - -

    public void add(GameObject gameObject)
    {
        SpriteComponent sprite = gameObject.getComponent(SpriteComponent.class);
        if (sprite != null)
        {
            add(sprite);
        }
    }

    private void add(SpriteComponent SPRITE)
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
        if (GO.getComponent(SpriteComponent.class) == null) return;
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

    public static void bindShader(Shader SHADER)
    {
        currentShader = SHADER;
        currentShader.use();
    }

    public static Shader getCurrentShader()
    {
        return currentShader;
    }
}


