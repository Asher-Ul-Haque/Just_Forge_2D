package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.Attachable.Sprite.SpriteSheet;
import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Utils.justForgeLogger;

import static org.lwjgl.glfw.GLFW.*;


public class GizmoSystem extends Component
{
    private SpriteSheet gizmos;
    private int currentGizmo = 0;

    public GizmoSystem(SpriteSheet GIZMO_SPRITES)
    {
        gizmos = GIZMO_SPRITES;
    }

    @Override
    public void start()
    {
        justForgeLogger.FORGE_LOG_DEBUG("Adding Gizmo System to " + gameObject);
        gameObject.addComponent(new TranslationGizmo(gizmos.getSprite(1)));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2)));
    }

    @Override
    public void update(float DELTA_TIME)
    {
        switch (currentGizmo)
        {
            case 0:
                gameObject.getCompoent(TranslationGizmo.class).setUsing(true);
                gameObject.getCompoent(ScaleGizmo.class).setUsing(false);
                break;

            case 1:
                gameObject.getCompoent(TranslationGizmo.class).setUsing(false);
                gameObject.getCompoent(ScaleGizmo.class).setUsing(true);
                break;
        }

        if (Keyboard.isKeyPressed(GLFW_KEY_E))
        {
            currentGizmo = 0;
        }
        else if (Keyboard.isKeyPressed(GLFW_KEY_R))
        {
            currentGizmo = 1;
        }
    }
}
