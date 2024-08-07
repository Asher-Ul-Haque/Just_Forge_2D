package Just_Forge_2D.Core.ECS.Components.EditorComponents.GizmoSystem;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Utils.justForgeLogger;
import static org.lwjgl.glfw.GLFW.*;

// - - - Gizmo System
public class GizmoSystemComponent extends Component
{
    // - - - private variables
    private final SpriteSheet gizmos;
    private int currentGizmo = 0;


    // - - - | Functions | - - -


    // - - - Constructors and Starters - - -

    public GizmoSystemComponent(SpriteSheet GIZMO_SPRITES)
    {
        gizmos = GIZMO_SPRITES;
    }

    @Override
    public void start()
    {
        justForgeLogger.FORGE_LOG_INFO("Adding Gizmo System to " + gameObject);
        gameObject.addComponent(new TranslationGizmoComponent(gizmos.getSprite(1)));
        gameObject.addComponent(new ScaleGizmoComponent(gizmos.getSprite(2)));
    }


    // - - - Usage - - -

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        switch (currentGizmo)
        {
            case 0:
                gameObject.getCompoent(TranslationGizmoComponent.class).setUsing(true);
                gameObject.getCompoent(ScaleGizmoComponent.class).setUsing(false);
                break;

            case 1:
                gameObject.getCompoent(TranslationGizmoComponent.class).setUsing(false);
                gameObject.getCompoent(ScaleGizmoComponent.class).setUsing(true);
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
