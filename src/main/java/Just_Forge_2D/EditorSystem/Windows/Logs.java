package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.flag.ImGuiWindowFlags;

public class Logs
{
    // Variables to hold the checkbox states for each log level
    private static boolean showFatal = true;
    private static boolean showError = true;
    private static boolean showWarning = true;
    private static boolean showInfo = true;
    private static boolean showDebug = true;
    private static boolean showTrace = true;

    public static void render()
    {
        ImGui.begin(Icons.Blog + "  Logs", ImGuiWindowFlags.AlwaysVerticalScrollbar | ImGuiWindowFlags.AlwaysHorizontalScrollbar);

        // - - - Checkbox UI to toggle log levels
        if (ImGui.collapsingHeader(Icons.Filter + "  Filters"))
        {
            showFatal = Widgets.drawBoolControl(Icons.SkullCrossbones + "  FATAL", showFatal);
            showError = Widgets.drawBoolControl(Icons.ExclamationTriangle + "  ERROR", showError);
            showWarning = Widgets.drawBoolControl(Icons.Exclamation + "  WARNING", showWarning);
            showInfo = Widgets.drawBoolControl(Icons.InfoCircle + "  INFO", showInfo);
            showDebug = Widgets.drawBoolControl(Icons.Bug + "  DEBUG", showDebug);
            showTrace = Widgets.drawBoolControl(Icons.Comment + "  TRACE", showTrace);
        }
        Widgets.text("");

        for (int i = Logger.getReadBuffer().length - 1; i > 0; --i)
        {
            String e = Logger.getReadBuffer()[i];
            if (e == null) continue;
            float r = 1f, g = 1f, b = 1f;

            // - - - Apply color coding based on the log type
            if (e.startsWith("[FATAL]") && showFatal)
            {
                r = 1;
                g = 0;
                b = 0;
            }
            else if (e.startsWith("[ERROR]") && showError)
            {
                r = 214f / 256f;
                g = 36f / 256f;
                b = 17f / 256f;
            }
            else if (e.startsWith("[WARNING]") && showWarning)
            {
                r = 235f / 256f;
                g = 132f / 256f;
                b = 38f / 256f;
            }
            else if (e.startsWith("[INFO]") && showInfo)
            {
                r = 16f / 256f;
                g = 210f / 256f;
                b = 117f / 256f;
            }
            else if (e.startsWith("[DEBUG]") && showDebug)
            {
                r = 104f / 256f;
                g = 174f / 256f;
                b = 212f / 256f;
            }
            else if (e.startsWith("[TRACE]") && showTrace)
            {
                r = 255f / 256f;
                g = 128f / 256f;
                b = 164f / 256f;
            }
            else
            {
                continue;
            }

            ImGui.textColored(r, g, b, 1f, e);
        }

        ImGui.end();
    }
}
