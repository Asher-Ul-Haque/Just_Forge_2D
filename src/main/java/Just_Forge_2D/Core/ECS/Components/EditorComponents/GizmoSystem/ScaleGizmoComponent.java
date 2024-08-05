package Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Utils.justForgeLogger;

// - - - Scale Gizmo
public class ScaleGizmoComponent extends GizmoComponent
{
    public ScaleGizmoComponent(Sprite SCALE)
    {
        super(SCALE);
        justForgeLogger.FORGE_LOG_INFO("Created Scale Gizmo");
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (activeGameObject != null)
        {
            if (xAxisActive && !yAxisActive)
            {
                activeGameObject.transform.scale.x -= Mouse.getWorldDeltaX();
            }
            else if (yAxisActive && !xAxisActive)
            {
                activeGameObject.transform.scale.y -= Mouse.getWorldDeltaY();
            }
        }
        super.update(DELTA_TIME);
    }
}
