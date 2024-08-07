package Just_Forge_2D.Core.ECS.Components.EditorComponents;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.justForgeLogger;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

// - - - Component to make sure things can be picked up
public class MouseControlComponent extends Component
{
    // - - - private variable for the thing being held
    GameObject holdingObject = null;


    // - - - | Functions | - - -


    // - - - Pick and Drop - - -

    public void pickupObject(GameObject GO)
    {
        this.holdingObject = GO;
        ForgeDynamo.getCurrentScene().addGameObject(GO);
        justForgeLogger.FORGE_LOG_DEBUG("Picked up object: "+ this.holdingObject);
    }

    public void place()
    {
        justForgeLogger.FORGE_LOG_DEBUG("Placed game object: " + this.holdingObject);
        this.holdingObject = null;
    }

    // - - - run
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (holdingObject != null)
        {
            holdingObject.transform.position.x = (int)(Mouse.getWorldX() / Configurations.GRID_WIDTH) * Configurations.GRID_WIDTH;
            holdingObject.transform.position.y = (int)(Mouse.getWorldY() / Configurations.GRID_HEIGHT) * Configurations.GRID_WIDTH;
            if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
            {
                place();
            }
        }
    }
}
