package Just_Forge_2D.Core.ECS.Components.EditorComponents;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.*;

// - - - Component to make sure things can be picked up
public class MouseControlComponent extends Component
{
    // - - - private variable for the thing being held
    GameObject holdingObject = null;
    private final float debounceTime = 0.005f;
    private float debounce = debounceTime;


    // - - - | Functions | - - -


    // - - - Pick and Drop - - -

    public void pickupObject(GameObject GO)
    {
        if (this.holdingObject != null)
        {
            this.holdingObject.destroy();
        }
        this.holdingObject = GO;
        this.holdingObject.getCompoent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        //this.holdingObject.addComponent(new NonPickableComponent());
        ForgeDynamo.getCurrentScene().addGameObject(GO);
        Logger.FORGE_LOG_DEBUG("Picked up object: "+ this.holdingObject);
    }

    public void place()
    {
        Logger.FORGE_LOG_DEBUG("Placed game object: " + this.holdingObject);
        GameObject newObj = this.holdingObject.copy();
        newObj.getCompoent(SpriteComponent.class).setColor(new Vector4f(1, 1, 1, 1));
        //newObj.removeComponent(SpriteComponent.class);
        ForgeDynamo.getCurrentScene().addGameObject(newObj);
    }

    // - - - run
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        debounce -= DELTA_TIME;
        if (holdingObject != null && debounce <= 0)
        {
            holdingObject.transform.position.x = (int)(Mouse.getWorldX() / Configurations.GRID_WIDTH) * Configurations.GRID_WIDTH + Configurations.GRID_WIDTH / 2f;
            holdingObject.transform.position.y = (int)(Mouse.getWorldY() / Configurations.GRID_HEIGHT) * Configurations.GRID_WIDTH + Configurations.GRID_HEIGHT / 2f;
            if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT))
            {
                place();
                debounce = debounceTime;
            }
            if (Keyboard.isKeyPressed(GLFW_KEY_ESCAPE))
            {
                holdingObject.destroy();
                holdingObject = (null);
            }
        }
    }
}
