package Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
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
}