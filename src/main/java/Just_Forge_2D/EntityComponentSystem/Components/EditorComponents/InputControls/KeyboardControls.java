package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.InputControls;

import Just_Forge_2D.AnimationSystem.StateMachine;
import Just_Forge_2D.EditorSystem.PropertiesWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.EditorWindow;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

// - - - Key control class
public class KeyboardControls extends Component
{
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        PropertiesWindow propertiesWindow = EditorWindow.getEditor().getPropertiesWindow();;
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();

        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && Keyboard.isKeyBeginPress(GLFW_KEY_C) && activeGameObject != null)
        {
            Logger.FORGE_LOG_DEBUG("Copying: " + activeGameObject);
            GameObject newObj = activeGameObject.copy();
            EditorWindow.getCurrentScene().addGameObject(newObj);
            newObj.transform.position.add(0.1f, 0.1f);
            EditorWindow.getEditor().getPropertiesWindow().setActiveGameObject(newObj);
            if (newObj.getCompoent(StateMachine.class) != null)
            {
                newObj.getCompoent(StateMachine.class).refreshTextures();
            }
        }
        else if (Keyboard.isKeyPressed(GLFW_KEY_LEFT_CONTROL) && Keyboard.isKeyBeginPress(GLFW_KEY_C) && activeGameObjects.size() > 1)
        {
            List<GameObject> gameObjects = new ArrayList<>(activeGameObjects);
            propertiesWindow.clearSelection();
            for (GameObject go : gameObjects)
            {
                Logger.FORGE_LOG_DEBUG("Copying: " + go);
                GameObject copy = go.copy();
                EditorWindow.getCurrentScene().addGameObject(copy);
                copy.transform.position.add(0.1f, 0.1f);
                propertiesWindow.addActiveGameObject(copy);
                if (go.getCompoent(StateMachine.class) != null)
                {
                    go.getCompoent(StateMachine.class).refreshTextures();
                }
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
        else if (!activeGameObjects.isEmpty() && Keyboard.isKeyBeginPress(GLFW_KEY_PAGE_DOWN))
        {
            for (GameObject go: activeGameObjects)
            {
                go.transform.layer--;
            }
        }
        else if (!activeGameObjects.isEmpty() && Keyboard.isKeyBeginPress(GLFW_KEY_PAGE_UP))
        {
            for (GameObject go: activeGameObjects)
            {
                go.transform.layer++;
            }
        }
    }
}