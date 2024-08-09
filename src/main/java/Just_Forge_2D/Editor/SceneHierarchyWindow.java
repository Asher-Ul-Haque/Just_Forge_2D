package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.flag.ImGuiTreeNodeFlags;

import java.util.List;
import java.util.Locale;

public class SceneHierarchyWindow
{
    private static String payloadType = "Scene Hierarchy";
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
                 obj.name,
                    ImGuiTreeNodeFlags.DefaultOpen |
                            ImGuiTreeNodeFlags.FramePadding |
                            ImGuiTreeNodeFlags.OpenOnArrow |
                            ImGuiTreeNodeFlags.SpanAvailWidth,
                    obj.name
            );
            ImGui.popID();

            if (ImGui.beginDragDropSource())
            {
                ImGui.setDragDropPayload(payloadType, obj);
                ImGui.text(obj.name);
                ImGui.endDragDropSource();
            }

            if (ImGui.beginDragDropTarget())
            {
                Object payload = ImGui.acceptDragDropPayload(payloadType);
                if (payload != null)
                {
                    if (payload.getClass().isAssignableFrom(GameObject.class))
                    {
                        GameObject payerGameObj = (GameObject) payload;
                        Logger.FORGE_LOG_TRACE("payload accepted" + payerGameObj.name);
                    }
                    index++;
                }
                ImGui.endDragDropTarget();
            }

            if (treeNodeOpen)
            {
                ImGui.treePop();
            }
            index++;
        }
        ImGui.end();
    }
}
