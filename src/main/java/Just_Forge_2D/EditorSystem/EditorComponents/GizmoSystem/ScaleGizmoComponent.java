package Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.Utils.Logger;

// - - - Scale Gizmo
public class ScaleGizmoComponent extends GizmoComponent
{
    // - - - Constructor

    public ScaleGizmoComponent(Sprite SCALE)
    {
        super(SCALE);
        Logger.FORGE_LOG_INFO("Created Scale Gizmo");
    }


    // - - - updated
    @Override
    public void editorUpdate(float DELTA_TIME)
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
        super.editorUpdate(DELTA_TIME);
    }
}
