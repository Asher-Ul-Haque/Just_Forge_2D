package Just_Forge_2D.ForgeEditor.Windows;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.ForgeEditor.EditorManager;
import Just_Forge_2D.SceneSystem.Scene;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;

public class SceneHierarchyPanel
{
    public static void render()
    {
        ImGui.begin("Scene Hierarchy Panel");

        for (Scene scene : SceneSystemManager.getAllScenes())
        {
            if (ImGui.treeNode(scene.getID()))
            {
                if (ImGui.isItemClicked())
                {
                    Logger.FORGE_LOG_TRACE("Assigned Scene : " + scene.getID());
                    EditorManager.currentScene = scene;
                }

                if (EditorManager.currentScene != null && EditorManager.currentScene.equals(scene))
                {
                    for (GameObject gameObject : EditorManager.currentScene.getGameObjects())
                    {
                        if (ImGui.treeNode(gameObject.name))
                        {
                            if (ImGui.isItemClicked())
                            {
                                Logger.FORGE_LOG_TRACE(gameObject.name);
                                EditorManager.currentGameObjects.clear();
                                EditorManager.currentGameObjects.add(gameObject);
                            }

                            ImGui.treePop();
                        }
                    }
                }
                ImGui.treePop();
            }
        }
        ImGui.end();
        ImGui.end();
    }
}
