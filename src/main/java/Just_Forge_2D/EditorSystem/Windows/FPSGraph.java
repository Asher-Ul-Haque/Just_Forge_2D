package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.extension.implot.ImPlot;
import imgui.extension.implot.ImPlotContext;
import imgui.extension.implot.flag.ImPlotCol;
import imgui.type.ImBoolean;

public class FPSGraph
{
    private static final int MAX_SAMPLES = 1024;
    private static final float[] fpsSamples = new float[MAX_SAMPLES];
    private static final Float[] fpsSamplesWrapper = new Float[MAX_SAMPLES];
    private static final Float[] xValuesWrapper = new Float[MAX_SAMPLES];
    private static int currentSampleIndex = 0;
    private static final ImBoolean vsync = new ImBoolean();

    private static ImPlotContext imPlotContext;

    private static void initializeImPlot()
    {
        if (imPlotContext == null)
        {
            imPlotContext = ImPlot.createContext();
            vsync.set(MainWindow.get().isVsync());
        }
    }

    public static void render()
    {
        if (MainWindow.get() != null)
        {
            initializeImPlot();
            fpsSamples[currentSampleIndex] = MainWindow.get().getFPS();

            for (int i = 0; i < MAX_SAMPLES; i++)
            {
                fpsSamplesWrapper[i] = (float) i;
                xValuesWrapper[i] = fpsSamples[i];
            }

            currentSampleIndex = (currentSampleIndex + 1) % MAX_SAMPLES;

            ImGui.begin("FPS");
            ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x , ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
            Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.checkbox("Vsync", vsync);
            Theme.resetDefaultTextColor();
            if (vsync.get() != MainWindow.get().isVsync())
            {
                MainWindow.get().setVsync(vsync.get());
            }


            ImPlot.setNextPlotLimitsY(0, vsync.get() ? 120.0f : 600.0f, 1);
            ImPlot.setNextPlotFormatX("");
            if (ImPlot.beginPlot("FPS Plot"))
            {
                ImPlot.plotLine("", fpsSamplesWrapper, xValuesWrapper);

                ImPlot.endPlot();
            }

            ImGui.end();
        }
    }
}
