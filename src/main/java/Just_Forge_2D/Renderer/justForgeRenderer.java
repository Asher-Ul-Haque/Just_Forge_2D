package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Utils.justForgeLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class justForgeRenderer
{
    private final int MAX_BATCH_SIZE = 1024;
    private List<justForgeRenderBatch> batches;

    public justForgeRenderer()
    {
        this.batches = new ArrayList<>();
    }

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

        for (justForgeRenderBatch batch : batches)
        {
            if (batch.hasRoom && batch.getLayer() == SPRITE.gameObject.getLayer())
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
            justForgeLogger.FORGE_LOG_DEBUG("Batch ran out of room, creating new batch for layer: " + SPRITE.gameObject.getLayer());
            justForgeRenderBatch newBatch = new justForgeRenderBatch(MAX_BATCH_SIZE, SPRITE.gameObject.getLayer());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(SPRITE);
            Collections.sort(batches);
        }
    }

    public void render()
    {
        for (justForgeRenderBatch batch : batches)
        {
            batch.render();
        }
    }
}
