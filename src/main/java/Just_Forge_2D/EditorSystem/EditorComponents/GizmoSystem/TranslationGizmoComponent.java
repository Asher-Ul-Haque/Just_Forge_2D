package Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
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
                if (Keyboard.isKeyPressed(Keys.RIGHT_SHIFT) || Keyboard.isKeyPressed(Keys.LEFT_SHIFT))
                {
                    activeGameObject.transform.position.x = Mouse.getWorldX();
                }
                else
                {
                    activeGameObject.transform.position.x -= Mouse.getWorldDeltaX();
                }
            }
            else if (yAxisActive && !xAxisActive)
            {
                if (Keyboard.isKeyPressed(Keys.RIGHT_SHIFT) || Keyboard.isKeyPressed(Keys.LEFT_SHIFT))
                {
                    activeGameObject.transform.position.y = Mouse.getWorldY();
                }
                else
                {
                    activeGameObject.transform.position.y -= Mouse.getWorldDeltaY();
                }
            }
        }
        super.editorUpdate(DELTA_TIME);
    }
}