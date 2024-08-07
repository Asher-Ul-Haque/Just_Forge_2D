package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Utils.justForgeLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// - - - the mighty renderer
public class Renderer
{
    // - - - private variables
    private final int MAX_BATCH_SIZE = 1024;
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
        SpriteComponent sprite = gameObject.getCompoent(SpriteComponent.class);

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
            justForgeLogger.FORGE_LOG_DEBUG("Batch ran out of room, creating new batch for layer: " + SPRITE.gameObject.transform.layer);
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, SPRITE.gameObject.transform.layer);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(SPRITE);
            Collections.sort(batches);
        }
    }

    // - - - use
    public void render()
    {
        for (RenderBatch batch : batches)
        {
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

    public void destroyGameObject(GameObject GO)
    {
        if (GO.getCompoent(SpriteComponent.class) == null) return;
        for (RenderBatch batch: batches)
        {
            if (batch.destroyIfExists(GO))
            {
                return;
            }
        }
    }
}


