package Just_Forge_2D.EditorSystem.EditorComponents.InputControls;

import Just_Forge_2D.AnimationSystem.StateMachine;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EditorSystem.EditorComponents.NonPickableComponent;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Forge;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.InputSystem.MouseButtons;
import Just_Forge_2D.RenderingSystems.DebugPencil;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.Set;

// - - - Component to make sure things can be picked up
public class MouseControlComponent extends Component
{
    // - - - private variable for the thing being held
    GameObject holdingObject = null;
    private final float debounceTime = 0.5f;
    private float debounce = debounceTime;

    // - - - private variables for box selection
    private boolean boxSelect = false;
    private Vector2f boxSelectStart = new Vector2f();
    private Vector2f boxSelectEnd = new Vector2f();
    private Vector2f boxSelectWorldStart = new Vector2f();
    private Vector2f boxSelectWorldEnd = new Vector2f();


    // - - - | Functions | - - -


    // - - - Pick and Drop - - -

    public void pickupObject(GameObject GO)
    {
        if (this.holdingObject != null)
        {
            this.holdingObject.destroy();
        }
        this.holdingObject = GO;
        this.holdingObject.getComponent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingObject.addComponent(new NonPickableComponent());
        EditorSystemManager.editorScene.addGameObject(GO);
        Logger.FORGE_LOG_DEBUG("Picked up object: "+ this.holdingObject);
    }

    public void place()
    {
        Logger.FORGE_LOG_DEBUG("Placed game object: " + this.holdingObject);
        GameObject newObj = this.holdingObject.copy();
        if (newObj.getComponent(StateMachine.class) != null)
        {
            newObj.getComponent(StateMachine.class).refreshTextures();
        }
        newObj.getComponent(SpriteComponent.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickableComponent.class);
        EditorSystemManager.editorScene.addGameObject(newObj);
    }

    // - - - run
}
