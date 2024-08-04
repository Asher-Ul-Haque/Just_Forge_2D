package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.Input.Mouse;

public class ScaleGizmo extends Gizmo
{
    public ScaleGizmo(Sprite SCALE)
    {
        super(SCALE);
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
