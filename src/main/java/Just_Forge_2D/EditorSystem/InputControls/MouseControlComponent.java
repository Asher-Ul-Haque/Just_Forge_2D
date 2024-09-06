package Just_Forge_2D.EditorSystem.InputControls;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EditorSystem.Windows.ComponentsWindow;
import Just_Forge_2D.EditorSystem.Windows.GridControls;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.NonPickableComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.EditorSystem.Windows.ObjectSelector;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import org.joml.*;

import java.util.HashSet;
import java.util.Set;

import static org.lwjgl.glfw.GLFW.*;

public class MouseControlComponent extends Component
{
    // - - - private variable for the thing being held
    static GameObject holdingObject = null;
    static private final float debounceTime = 0.4f;
    static private float debounce = debounceTime;

    // - - - private variables for box selection
    private static boolean boxSelect = false;
    private static Vector2f boxSelectStart = new Vector2f();
    private static Vector2f boxSelectEnd = new Vector2f();
    private static Vector2f boxSelectWorldStart = new Vector2f();


    // - - - | Functions | - - -


    // - - - Pick and Drop - - -

    public static void pickupObject(GameObject GO)
    {
        if (holdingObject != null)
        {
            holdingObject.destroy();
        }
        holdingObject = GO;
        holdingObject.getCompoent(SpriteComponent.class).setColor(new Vector4f(0.8f, 0.8f, 0.8f, 0.5f));
        holdingObject.addComponent(new NonPickableComponent());
        MainWindow.getCurrentScene().addGameObject(GO);
        Logger.FORGE_LOG_DEBUG("Picked up object: "+ holdingObject);
    }

    public static void place()
    {
        Logger.FORGE_LOG_DEBUG("Placed game object: " + holdingObject);
        GameObject newObj = holdingObject.copy();
        if (newObj.getCompoent(AnimationComponent.class) != null)
        {
            newObj.getCompoent(AnimationComponent.class).refreshTextures();
        }
        newObj.getCompoent(SpriteComponent.class).setColor(new Vector4f(1, 1, 1, 1));
        newObj.removeComponent(NonPickableComponent.class);
        MainWindow.getCurrentScene().addGameObject(newObj);
    }

    // - - - run
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        debounce -= DELTA_TIME;
        Scene currentScene = MainWindow.getCurrentScene();

        if (holdingObject != null && debounce < 0.0f)
        {
            if (GridControls.snapToGrid)
            {
                holdingObject.transform.position.x = (int) (Mouse.getWorldX() / GridlinesComponent.gridSize.x) * GridlinesComponent.gridSize.x + GridlinesComponent.gridSize.x / 2f;
                holdingObject.transform.position.y = (int) (Mouse.getWorldY() / GridlinesComponent.gridSize.y) * GridlinesComponent.gridSize.x + GridlinesComponent.gridSize.y / 2f;
            }
            else
            {
                holdingObject.transform.position.x = Mouse.getWorldX();
                holdingObject.transform.position.y = Mouse.getWorldY();
            }

            if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0.0f)
            {
                place();
                debounce = debounceTime;
            }
            if (Keyboard.isKeyPressed(Keys.ESCAPE))
            {
                holdingObject.destroy();
                holdingObject = (null);
            }
        }
        else if (!Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0)
        {
            int x = (int)Mouse.getScreenX(DefaultValues.DEFAULT_WINDOW_WIDTH);
            int y = (int)Mouse.getScreenY(DefaultValues.DEFAULT_WINDOW_HEIGHT);
            int gameObjectID = ObjectSelector.readPixel(x, y);
            GameObject picked = currentScene.getGameObject(gameObjectID);
            if (picked != null && picked.getCompoent(NonPickableComponent.class) == null)
            {
                ComponentsWindow.setActiveGameObject(picked);
            }
            else if (picked == null && !Mouse.isDragging())
            {
                ComponentsWindow.clearSelection();
            }

            debounce = debounceTime;
        }
        else if (Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && holdingObject == null && ComponentsWindow.getActiveGameObjects().isEmpty())
        {
            if (!boxSelect)
            {
                ComponentsWindow.clearSelection();
                boxSelectStart = new Vector2f(Mouse.getScreenX(DefaultValues.DEFAULT_WINDOW_WIDTH), Mouse.getScreenY(DefaultValues.DEFAULT_WINDOW_HEIGHT));
                boxSelectWorldStart = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());
                boxSelect = true;
            }
            boxSelectEnd = new Vector2f(Mouse.getScreenX(DefaultValues.DEFAULT_WINDOW_WIDTH), Mouse.getScreenY(DefaultValues.DEFAULT_WINDOW_HEIGHT));
            Vector2f boxSelectWorldEnd = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());
            Vector2f halfSize = (new Vector2f(boxSelectWorldEnd).sub(boxSelectWorldStart)).mul(0.5f);
            DebugPencil.addBox(
                    (new Vector2f(boxSelectWorldStart)).add(halfSize),
                    new Vector2f(halfSize).mul(2.0f),
                    new Vector3f(1, 0, 0));

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

            float[] gameObjectIDs = ObjectSelector.readPixels(
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
                GameObject picked = MainWindow.getCurrentScene().getGameObject(gameObjectID);
                if (picked != null && picked.getCompoent(NonPickableComponent.class) == null)
                {
                    ComponentsWindow.addActiveGameObject(picked);
                }
            }
        }
    }
}
