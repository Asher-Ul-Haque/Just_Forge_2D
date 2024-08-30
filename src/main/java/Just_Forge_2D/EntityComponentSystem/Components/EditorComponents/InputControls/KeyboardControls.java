package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.InputControls;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.EditorSystem.Windows.PropertiesWindow;
import Just_Forge_2D.Utils.Logger;

import java.util.ArrayList;
import java.util.List;

// - - - Key control class
public class KeyboardControls
{
    public static void editorUpdate()
    {
        GameObject activeGameObject = PropertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = PropertiesWindow.getActiveGameObjects();

        if (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) && Keyboard.isKeyBeginPress(Keys.C) && activeGameObject != null)
        {
            Logger.FORGE_LOG_DEBUG("Copying: " + activeGameObject);
            GameObject newObj = activeGameObject.copy();
            MainWindow.getCurrentScene().addGameObject(newObj);
            newObj.transform.position.add(0.1f, 0.1f);
            PropertiesWindow.setActiveGameObject(newObj);
            if (newObj.getCompoent(AnimationComponent.class) != null)
            {
                newObj.getCompoent(AnimationComponent.class).refreshTextures();
            }
        }
        else if (Keyboard.isKeyPressed(Keys.LEFT_CONTROL) && Keyboard.isKeyBeginPress(Keys.C) && activeGameObjects.size() > 1)
        {
            List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
            PropertiesWindow.clearSelection();
            for (GameObject go : gameObjects)
            {
                Logger.FORGE_LOG_DEBUG("Copying: " + go);
                GameObject copy = go.copy();
                MainWindow.getCurrentScene().addGameObject(copy);
                copy.transform.position.add(0.1f, 0.1f);
                PropertiesWindow.addActiveGameObject(copy);
                if (go.getCompoent(AnimationComponent.class) != null)
                {
                    go.getCompoent(AnimationComponent.class).refreshTextures();
                }
            }
        }
        else if (Keyboard.isKeyPressed(Keys.DELETE))
        {
            for (GameObject go : activeGameObjects)
            {
                Logger.FORGE_LOG_DEBUG("Destroying game object: "+ go);
                go.destroy();
            }
            PropertiesWindow.clearSelection();
        }
        else if (!activeGameObjects.isEmpty() && Keyboard.isKeyBeginPress(Keys.PAGE_DOWN))
        {
            for (GameObject go: activeGameObjects)
            {
                go.transform.layer--;
            }
        }
        else if (!activeGameObjects.isEmpty() && Keyboard.isKeyBeginPress(Keys.PAGE_UP))
        {
            for (GameObject go: activeGameObjects)
            {
                go.transform.layer++;
            }
        }
    }
}