package Just_Forge_2D.Core.ECS.Components.Attachable;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.justForgeLogger;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class MouseControlComponent extends Component
{
    GameObject holdingObject = null;

    public void pickupObject(GameObject GO)
    {
        this.holdingObject = GO;
        Window.getCurrentScene().addGameObject(GO);
        justForgeLogger.FORGE_LOG_DEBUG("Picked up object: "+ this.holdingObject);
    }

    public void place()
    {
        justForgeLogger.FORGE_LOG_DEBUG("Placed game object: " + this.holdingObject);
        this.holdingObject = null;
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (holdingObject != null)
        {
            //justForgeLogger.FORGE_LOG_TRACE("Mouse orhto: " + Mouse.getOrthoX() + " " + Mouse.getOrthoY());
            holdingObject.transform.position.x = (int)(Mouse.getOrthoX() / Configurations.GRID_WIDTH) * Configurations.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(Mouse.getOrthoY() / Configurations.GRID_HEIGHT) * Configurations.GRID_WIDTH;
            if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
            {
                place();
            }
        }
    }
}
