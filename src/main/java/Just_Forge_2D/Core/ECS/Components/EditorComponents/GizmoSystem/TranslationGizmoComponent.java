package Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Utils.justForgeLogger;

// - - - Translation Gizmo
public class TranslationGizmoComponent extends GizmoComponent
{
    public TranslationGizmoComponent(Sprite ARROW)
    {
        super(ARROW);
        justForgeLogger.FORGE_LOG_INFO("Created translation Gizmo");
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (activeGameObject != null)
        {
            if (xAxisActive && !yAxisActive)
            {
                activeGameObject.transform.position.x -= Mouse.getWorldDeltaX();
            }
            else if (yAxisActive && !xAxisActive)
            {
                activeGameObject.transform.position.y -= Mouse.getWorldDeltaY();
            }
        }
        super.editorUpdate(DELTA_TIME);
    }
}