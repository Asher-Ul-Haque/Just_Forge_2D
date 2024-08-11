package Just_Forge_2D.Core.ECS.Components.EditorComponents;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Editor.PropertiesWindow;
import Just_Forge_2D.Utils.Configurations;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends Component
{
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        PropertiesWindow propertiesWindow = ForgeDynamo.getEditor().getPropertiesWindow();;
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();
        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && Keyboard.isKeyBeginPress(GLFW_KEY_C) && activeGameObject != null)
        {
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
                GameObject copy = go.copy();
                ForgeDynamo.getCurrentScene().addGameObject(copy);
                propertiesWindow.addActiveGameObject(copy);
            }
        }
        else if (Keyboard.isKeyPressed(GLFW_KEY_DELETE))
        {
            for (GameObject go : activeGameObjects)
            {
                go.destroy();
            }
            propertiesWindow.clearSelection();
        }
    }

}
