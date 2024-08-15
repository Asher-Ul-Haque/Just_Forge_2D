package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.CoreSystems.InputSystem.Mouse;
import Just_Forge_2D.Utils.Logger;

// - - - Translation Gizmo
public class TranslationGizmoComponent extends GizmoComponent
{
    // - - - translate gizmo
    public TranslationGizmoComponent(Sprite ARROW)
    {
        super(ARROW);
        Logger.FORGE_LOG_INFO("Created translation Gizmo");
    }

    // - - - update
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