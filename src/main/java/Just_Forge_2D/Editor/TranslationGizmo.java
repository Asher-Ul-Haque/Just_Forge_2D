package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.Sprite;
import Just_Forge_2D.Core.Input.Mouse;

public class TranslationGizmo extends Gizmo
{
    public TranslationGizmo(Sprite ARROW)
    {
        super(ARROW);
    }

    @Override
    public void update(float DELTA_TIME)
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
        super.update(DELTA_TIME);
    }

}