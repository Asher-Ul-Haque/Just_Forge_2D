package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import imgui.ImGui;

import java.util.List;

public class SceneHierarchyWindow
{
    public static void editorGUI()
    {
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjectList = MainWindow.getCurrentScene().getGameObjects();
        for (GameObject obj : gameObjectList)
        {
            if (!obj.getSerializationStatus())
            {
                continue;
            }
            ImGui.setCursorPosX(EditorSystemManager.getCurrentTheme().framePadding.x);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
            if (ImGui.button(obj.name))
            {
                PropertiesWindow.clearSelection();
                PropertiesWindow.setActiveGameObject(obj);
            }
        }
        ImGui.end();
    }
}
