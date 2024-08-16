package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.InputControls;

import Just_Forge_2D.AnimationSystem.StateMachine;
import Just_Forge_2D.EditorSystem.ObjectSelector;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.NonPickableComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.RenderingSystems.DebugPencil;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Forge;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

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
        this.holdingObject.getCompoent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        this.holdingObject.addComponent(new NonPickableComponent());
        Forge.getCurrentScene().addGameObject(GO);
        Logger.FORGE_LOG_DEBUG("Picked up object: "+ this.holdingObject);
    }

    public void place()
    {
        Logger.FORGE_LOG_DEBUG("Placed game object: " + this.holdingObject);
        GameObject newObj = this.holdingObject.copy();
        if (newObj.getCompoent(StateMachine.class) != null)
        {
            newObj.getCompoent(StateMachine.class).refreshTextures();
        }
        newObj.getCompoent(SpriteComponent.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickableComponent.class);
        Forge.getCurrentScene().addGameObject(newObj);
    }

    // - - - run
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        debounce -= DELTA_TIME;
        ObjectSelector selector = Forge.getEditor().getPropertiesWindow().getSelector();
        Scene currentScene = Forge.getCurrentScene();

        if (holdingObject != null && debounce < 0.0f)
        {
            holdingObject.transform.position.x = (int)(Mouse.getWorldX() / Configurations.GRID_WIDTH) * Configurations.GRID_WIDTH + Configurations.GRID_WIDTH / 2f;
            holdingObject.transform.position.y = (int)(Mouse.getWorldY() / Configurations.GRID_HEIGHT) * Configurations.GRID_WIDTH + Configurations.GRID_HEIGHT / 2f;
            if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0.0f)
            {
                float halfWidth = Configurations.GRID_WIDTH / 2.0f;
                float halfHeight = Configurations.GRID_HEIGHT / 2.0f;
                place();
                debounce = debounceTime;
            }
            if (Keyboard.isKeyPressed(GLFW_KEY_ESCAPE))
            {
                holdingObject.destroy();
                holdingObject = (null);
            }
        }
        else if (!Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0)
        {
            int x = (int)Mouse.getScreenX();
            int y = (int)Mouse.getScreenY();
            int gameObjectID = selector.readPixel(x, y);
            GameObject picked = currentScene.getGameObject(gameObjectID);
            if (picked != null && picked.getCompoent(NonPickableComponent.class) == null)
            {
                Forge.getEditor().getPropertiesWindow().setActiveGameObject(picked);
            }
            else if (picked == null && !Mouse.isDragging())
            {
                Forge.getEditor().getPropertiesWindow().clearSelection();
            }

            this.debounce = this.debounceTime;
        }
        else if (Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && Forge.getEditor().getPropertiesWindow().getActiveGameObject() == null && Forge.getEditor().getPropertiesWindow().getActiveGameObjects().isEmpty())
        {
            if (!boxSelect)
            {
                Forge.getEditor().getPropertiesWindow().clearSelection();
                boxSelectStart = new Vector2f(Mouse.getScreenX(), Mouse.getScreenY());
                boxSelectWorldStart = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());
                boxSelect = true;
            }
            boxSelectEnd = new Vector2f(Mouse.getScreenX(), Mouse.getScreenY());
            boxSelectWorldEnd = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());
            Vector2f halfSize = (new Vector2f(boxSelectWorldEnd).sub(boxSelectWorldStart)).mul(0.5f);
            DebugPencil.addBox(
                    (new Vector2f(boxSelectWorldStart)).add(halfSize),
                    new Vector2f(halfSize).mul(2.0f),
                    new Vector3f(1, 0, 1));

        }
        else if (boxSelect)
        {
            boxSelect = false;
            int screenStartX = (int) boxSelectStart.x;
            int screenStartY = (int) boxSelectStart.y;
            int screenEndX = (int) boxSelectEnd.x;
            int screenEndY = (int) boxSelectEnd.y;
            boxSelectStart.zero();
            boxSelectEnd.zero();

            if (screenEndX < screenStartX)
            {
                int tmp = screenStartX;
                screenStartX = screenEndX;
                screenEndX = tmp;
            }
            if (screenEndY < screenStartY)
            {
                int tmp = screenStartY;
                screenStartY = screenEndY;
                screenEndY = tmp;
            }

            float[] gameObjectIDs = selector.readPixels(
                    new Vector2i(screenStartX, screenStartY),
                    new Vector2i(screenEndX, screenEndY)
            );
            Set<Integer> uniqueGameObjectIDs = new HashSet<>();
            for (float objID : gameObjectIDs)
            {
                uniqueGameObjectIDs.add((int)objID);
            }

            for (Integer gameObjectID : uniqueGameObjectIDs)
            {
                GameObject picked = Forge.getCurrentScene().getGameObject(gameObjectID);
                if (picked != null && picked.getCompoent(NonPickableComponent.class) == null)
                {
                    Forge.getEditor().getPropertiesWindow().addActiveGameObject(picked);
                }
            }
        }
    }
}
