package Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.Logger;

// - - - Gizmo System
public class GizmoSystemComponent extends Component
{
    // - - - private variables
    private SpriteSheet gizmos = new SpriteSheet(AssetPool.makeTexture("Assets/Textures/gizmos.png"), 24, 48, 3, 0);
    private int currentGizmo = 0;


    // - - - | Functions | - - -


    // - - - Constructors and Starters - - -

    @Override
    public void start()
    {
        Logger.FORGE_LOG_INFO("Adding Gizmo System to " + gameObject);
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
                gameObject.getComponent(TranslationGizmoComponent.class).setUsing(true);
                gameObject.getComponent(ScaleGizmoComponent.class).setUsing(false);
                break;

            case 1:
                gameObject.getComponent(TranslationGizmoComponent.class).setUsing(false);
                gameObject.getComponent(ScaleGizmoComponent.class).setUsing(true);
                break;
        }

        if (Keyboard.isKeyPressed(Keys.T))
        {
            Logger.FORGE_LOG_INFO("Switched to Translate Gizmo");
            currentGizmo = 0;
        }
        else if (Keyboard.isKeyPressed(Keys.S))
        {
            Logger.FORGE_LOG_INFO("Switched to scale gizmo");
            currentGizmo = 1;
        }
    }
}
