package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.InputSystem.Mouse;
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
                activeGameObject.transform.position.x -= EditorSystemManager.getWorldDeltaX();
            }
            else if (yAxisActive && !xAxisActive)
            {
                activeGameObject.transform.position.y -= EditorSystemManager.getWorldDeltaY();
            }
        }
        super.editorUpdate(DELTA_TIME);
    }
}