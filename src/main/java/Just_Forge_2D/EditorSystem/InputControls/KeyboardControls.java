package Just_Forge_2D.EditorSystem.InputControls;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.Windows.ComponentsWindow;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.Utils.Logger;

import java.util.List;

// - - - Key control class
public class KeyboardControls
{
    public static void editorUpdate()
    {
        GameObject activeGameObject = ComponentsWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = ComponentsWindow.getActiveGameObjects();

        if (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) && Keyboard.isKeyBeginPress(Keys.C) && activeGameObject != null)
        {
            ComponentsWindow.setActiveGameObject(null);
            Logger.FORGE_LOG_DEBUG("Copying: " + activeGameObject);
            GameObject newObj = activeGameObject.copy();
            MouseControlComponent.pickupObject(newObj);
//            MouseControlComponent.holdingObject = newObj;
//            MainWindow.getCurrentScene().addGameObject(newObj);
            if (newObj.getCompoent(AnimationComponent.class) != null)
            {
                newObj.getCompoent(AnimationComponent.class).refreshTextures();
            }
        }

        else if (Keyboard.isKeyPressed(Keys.DELETE))
        {
            for (GameObject go : activeGameObjects)
            {
                Logger.FORGE_LOG_DEBUG("Destroying game object: "+ go);
                go.destroy();
            }
            ComponentsWindow.clearSelection();
        }
        else if (!activeGameObjects.isEmpty())
        {
            for (GameObject go : activeGameObjects)
            {
                if (Keyboard.isKeyBeginPress(Keys.PAGE_UP)) go.transform.layer--;
                else if (Keyboard.isKeyBeginPress(Keys.PAGE_DOWN)) go.transform.layer++;

                else if (Keyboard.isKeyPressed(Keys.ARROW_RIGHT))
                {
                    if (Keyboard.isKeyPressed(Keys.LEFT_SHIFT) || Keyboard.isKeyPressed(Keys.RIGHT_SHIFT))
                    {
                        if (Keyboard.isKeyPressed(Keys.LEFT_ALT) || Keyboard.isKeyPressed(Keys.RIGHT_ALT)) go.transform.rotation -= 0.01f;
                        else go.transform.scale.x += (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                    }
                    else go.transform.position.x += (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                }

                else if (Keyboard.isKeyPressed(Keys.ARROW_LEFT))
                {
                    if (Keyboard.isKeyPressed(Keys.LEFT_SHIFT) || Keyboard.isKeyPressed(Keys.RIGHT_SHIFT))
                    {
                        if (Keyboard.isKeyPressed(Keys.LEFT_ALT) || Keyboard.isKeyPressed(Keys.RIGHT_ALT)) go.transform.rotation += 0.01f;
                        else go.transform.scale.x -= (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                    }
                    else go.transform.position.x -= (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                }

                else if (Keyboard.isKeyPressed(Keys.ARROW_UP))
                {
                    if (Keyboard.isKeyPressed(Keys.LEFT_SHIFT) || Keyboard.isKeyPressed(Keys.RIGHT_SHIFT))
                    {
                        if (Keyboard.isKeyPressed(Keys.LEFT_ALT) || Keyboard.isKeyPressed(Keys.RIGHT_ALT)) go.transform.rotation -= 0.01f;
                        else go.transform.scale.y += (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                    }
                    else go.transform.position.y += (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                }

                else if (Keyboard.isKeyPressed(Keys.ARROW_DOWN))
                {
                    if (Keyboard.isKeyPressed(Keys.LEFT_SHIFT) || Keyboard.isKeyPressed(Keys.RIGHT_SHIFT))
                    {
                        if (Keyboard.isKeyPressed(Keys.LEFT_ALT) || Keyboard.isKeyPressed(Keys.RIGHT_ALT)) go.transform.rotation += 0.01f;
                        else go.transform.scale.y -= (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                    }
                    else go.transform.position.y -= (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) || Keyboard.isKeyPressed(Keys.RIGHT_CONTROL)) ? GridlinesComponent.gridSize.x : 0.01f;
                }
            }
        }
    }
}