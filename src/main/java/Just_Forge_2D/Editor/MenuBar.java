package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.EventSystem.EventSystem;
import Just_Forge_2D.Core.EventSystem.Events.Event;
import Just_Forge_2D.Core.EventSystem.Events.EventTypes;
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
                EventSystem.notify(null, new Event(EventTypes.SaveLevel));
            }
            if (ImGui.menuItem("Load", "Ctrl+O"))
            {
                EventSystem.notify(null, new Event(EventTypes.LoadLevel));
            }
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
    }
}
