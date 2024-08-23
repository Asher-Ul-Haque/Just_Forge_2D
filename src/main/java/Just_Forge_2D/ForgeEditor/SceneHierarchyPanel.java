package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;

public class SceneHierarchyPanel
{
    public static void render()
    {
        // Begin the Scene Hierarchy Panel
        ImGui.begin("Scene Hierarchy Panel");

        // Iterate through all scenes
        for (Scene scene : SceneSystemManager.getAllScenes())
        {
            // Open a tree node for each scene
            if (ImGui.treeNode(scene.getID()))
            {
                // If the item is clicked, set it as the current scene
                if (ImGui.isItemClicked())
                {
                    Logger.FORGE_LOG_TRACE("Assigned Scene : " + scene.getID());
                    EditorManager.currentScene = scene;
                }

                // If the current scene is selected, iterate through its game objects
                if (EditorManager.currentScene != null && EditorManager.currentScene.equals(scene))
                {
                    for (GameObject gameObject : EditorManager.currentScene.getGameObjects())
                    {
                        // Open a tree node for each game object
                        if (ImGui.treeNode(gameObject.name))
                        {
                            // If the game object is clicked, set it as the current game object
                            if (ImGui.isItemClicked())
                            {
                                EditorManager.currentGameObject = gameObject;
                            }

                            ImGui.treePop(); // Close the game object node
                        }
                    }
                }

                ImGui.treePop(); // Close the scene node
            }
        }

        // End the Scene Hierarchy Panel
        ImGui.end();
    }
}
