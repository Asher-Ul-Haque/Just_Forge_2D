package Just_Forge_2D.EditorSystem.EditorComponents.GizmoSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.RenderingSystem.TextureMaximizeFilter;
import Just_Forge_2D.RenderingSystem.TextureMinimizeFilter;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;

// - - - Gizmo System
public class GizmoSystemComponent extends Component
{
    // - - - private variables
    protected static Texture gizmoTexture;
    private SpriteSheet gizmos;
    private int currentGizmo = 0;


    // - - - | Functions | - - -


    // - - - Constructors and Starters - - -

    @Override
    public void start()
    {
        if (gizmos == null)
        {
            if (gizmoTexture == null) gizmoTexture = new Texture();
            gizmoTexture.init("Assets/Textures/gizmos.png");
            gizmoTexture.setFilters(TextureMaximizeFilter.LINEAR, TextureMinimizeFilter.LINEAR, Settings.DEFAULT_TEXTURE_WRAP_S(), Settings.DEFAULT_TEXTURE_WRAP_T());
            gizmos = new SpriteSheet(gizmoTexture, 24, 48, 3, 0);
        }
        Logger.FORGE_LOG_INFO("Adding Gizmo System to " + gameObject);
        gameObject.addComponent(new TranslationGizmoComponent(gizmos.getSprite(1)));
        gameObject.addComponent(new ScaleGizmoComponent(gizmos.getSprite(2)));
    }


    // - - - Usage - - -

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        try
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

            if (Keyboard.isKeyBeginPress(Keys.T))
            {
                currentGizmo = (currentGizmo + 1) % 2;
            }
        }
        catch (NullPointerException e)
        {
            gameObject.destroy();
        }
    }
}