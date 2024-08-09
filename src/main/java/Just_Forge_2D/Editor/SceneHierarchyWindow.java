package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ForgeDynamo;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;

public class SceneHierarchyWindow
{
    public void editorGUI()
    {
        ImGui.begin("Scene Hierarchy");
        List<GameObject> gameObjectList = ForgeDynamo.getCurrentScene().getGameObjects();
        int index = 0;
        for (GameObject obj : gameObjectList)
        {
            if (!obj.getSerializationStatus())
            {
                continue;
            }
            ImGui.pushID(index);
            boolean treeNodeOpen = ImGui.treeNodeEx(
                 obj.toString(),
                    ImGuiTreeNodeFlags.DefaultOpen |
                            ImGuiTreeNodeFlags.FramePadding |
                            ImGuiTreeNodeFlags.OpenOnArrow |
                            ImGuiTreeNodeFlags.SpanAvailWidth,
                    obj.toString()
            );
            ImGui.popID();

            if (treeNodeOpen)
            {
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }
}
