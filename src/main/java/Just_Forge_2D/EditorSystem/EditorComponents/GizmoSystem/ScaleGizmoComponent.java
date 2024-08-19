package Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
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
}
