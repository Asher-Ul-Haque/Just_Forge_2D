package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import imgui.ImGui;

public class MenuBar
{
    public void editorGui()
    {
        ImGui.beginMainMenuBar();
        if (ImGui.beginMenu("File"))
        {
            if (ImGui.menuItem("Save", "Ctrl+S"))
            {
                EventManager.notify(null, new Event(EventTypes.SaveLevel));
            }
            if (ImGui.menuItem("Load", "Ctrl+O"))
            {
                EventManager.notify(null, new Event(EventTypes.LoadLevel));
            }
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
    }
}
