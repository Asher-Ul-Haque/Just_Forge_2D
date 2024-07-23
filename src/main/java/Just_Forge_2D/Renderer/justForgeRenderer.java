package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.ECS.Components.justForgeSpriteRendererComponent;
import Just_Forge_2D.Core.ECS.justForgeGameObject;
import Just_Forge_2D.Utils.justForgeLogger;

import java.util.ArrayList;
import java.util.List;

public class justForgeRenderer
{
    private final int MAX_BATCH_SIZE = 1024;
    private List<justForgeRenderBatch> batches;

    public justForgeRenderer()
    {
        this.batches = new ArrayList<>();
    }

    public void add(justForgeGameObject gameObject)
    {
        justForgeSpriteRendererComponent sprite = gameObject.getCompoent(justForgeSpriteRendererComponent.class);

        if (sprite != null)
        {
            add(sprite);
        }
    }

    private void add(justForgeSpriteRendererComponent SPRITE)
    {
        boolean added = false;

        for (justForgeRenderBatch batch : batches)
        {
            if (batch.hasRoom)
            {
                batch.addSprite(SPRITE);
                added = true;
                break;
            }
        }

        if (!added)
        {
            justForgeLogger.FORGE_LOG_DEBUG("Batch ran out of room, creating new batch");
            justForgeRenderBatch newBatch = new justForgeRenderBatch(MAX_BATCH_SIZE);
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(SPRITE);
            added = true;
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
