package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class Logs
{
    public static void render()
    {
        ImGui.begin("Logger", ImGuiWindowFlags.AlwaysVerticalScrollbar);
        for (int i = Logger.getReadBuffer().length - 1; i > 0; --i)
        {
            String e = Logger.getReadBuffer()[i];
            float r, g, b;
            r = g = b = 1;
            if (e.startsWith("[FATAL]"))
            {
                r = 1; g = 0; b = 0;
            }
            else if (e.startsWith("[ERROR]"))
            {
                r = 214f / 256f; g = 36f / 256f; b = 17f / 256f;
            }
            else if (e.startsWith("[WARNING]"))
            {
                r = 235f / 256f; g = 132f / 256f; b = 38f / 256f;
            }
            else if (e.startsWith("[INFO]"))
            {
                r = 16f / 256f; g = 210f / 256f; b = 117f / 256f;
            }
            else if (e.startsWith("[DEBUG]"))
            {
                r = 104f / 256f; g = 174f / 256f; b = 212f / 256f;
            }
            else if (e.startsWith("[TRACE]"))
            {
                r = 255f / 256f; g = 1284f / 256f; b = 164f / 256f;
            }
            ImGui.textColored(r, g, b, 1f, e);
        }
        ImGui.end();
    }
}
