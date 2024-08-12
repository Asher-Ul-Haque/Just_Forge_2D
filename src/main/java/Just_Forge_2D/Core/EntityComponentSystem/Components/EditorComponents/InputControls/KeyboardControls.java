package Just_Forge_2D.Core.EntityComponentSystem.Components.EditorComponents.InputControls;

import Just_Forge_2D.Core.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Core.EntityComponentSystem.GameObject;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Core.InputSystem.Keyboard;
import Just_Forge_2D.EditorSystem.PropertiesWindow;
import Just_Forge_2D.Utils.Logger;

import java.util.ArrayList;
import java.util.List;
import static org.lwjgl.glfw.GLFW.*;

// - - - Key control class
public class KeyboardControls extends Component
{
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        PropertiesWindow propertiesWindow = ForgeDynamo.getEditor().getPropertiesWindow();;
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();

        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && Keyboard.isKeyBeginPress(GLFW_KEY_C) && activeGameObject != null)
        {
            Logger.FORGE_LOG_DEBUG("Copying: " + activeGameObject);
            GameObject newObj = activeGameObject.copy();
            ForgeDynamo.getCurrentScene().addGameObject(newObj);
            newObj.transform.position.add(0.1f, 0.1f);
            ForgeDynamo.getEditor().getPropertiesWindow().setActiveGameObject(newObj);
        }
        else if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && Keyboard.isKeyPressed(GLFW_KEY_C) && activeGameObjects.size() > 1)
        {
            List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
            propertiesWindow.clearSelection();
            for (GameObject go : gameObjects)
            {
                Logger.FORGE_LOG_DEBUG("Copying: " + go);
                GameObject copy = go.copy();
                ForgeDynamo.getCurrentScene().addGameObject(copy);
                propertiesWindow.addActiveGameObject(copy);
            }
        }
        else if (Keyboard.isKeyPressed(GLFW_KEY_DELETE))
        {
            for (GameObject go : activeGameObjects)
            {
                Logger.FORGE_LOG_DEBUG("Destroying game object: "+ go);
                go.destroy();
            }
            propertiesWindow.clearSelection();
        }
    }
}