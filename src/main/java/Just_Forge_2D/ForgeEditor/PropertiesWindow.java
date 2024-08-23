package Just_Forge_2D.ForgeEditor;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import imgui.ImGui;

public class PropertiesWindow
{
    public static void render()
    {
        ImGui.begin("Inspector");

        if (EditorManager.currentGameObjects.size() == 1)
        {
            GameObject current = EditorManager.currentGameObjects.get(0);
            ImGui.text("Selected Game Object : " + current.name);

            for (Component component : current.getComponents())
            {
                if (ImGui.treeNode(component.getClass().getSimpleName()))
                {
              //      component.editorGUI();
                    ImGui.treePop();
                }
            }
        }
        else if (EditorManager.currentGameObjects.size() > 1)
        {
            ImGui.text("Multiple game Objects selected");
            for (GameObject gameObject : EditorManager.currentGameObjects)
            {
                ImGui.text(gameObject.name);
            }
        }
        else
        {
            ImGui.text("No game object selected");
        }

        ImGui.end();
    }
}
