package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.EditorWindow;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow
{
    public static void editorGUI()
    {
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjectList = EditorWindow.getCurrentScene().getGameObjects();
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
