package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.Themes.Theme;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import imgui.extension.implot.ImPlot;
import imgui.extension.implot.ImPlotContext;
import imgui.type.ImBoolean;

public class FPSGraph
{
    private static final int MAX_SAMPLES = 128;
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
            vsync.set(GameWindow.get().isVsync());
        }
    }

    public static void render()
    {
        if (GameWindow.get() != null)
        {
            initializeImPlot();
            float currentFPS = GameWindow.get().getFPS();
            float maxFPS = currentFPS;

            for (int i = 1; i < MAX_SAMPLES; i++)
            {
                fpsSamples[i - 1] = fpsSamples[i];
                maxFPS = Math.max(fpsSamples[i], maxFPS);
            }

            // - - - Insert the latest FPS value at the rightmost position
            fpsSamples[MAX_SAMPLES - 1] = currentFPS;

            for (int i = 0; i < MAX_SAMPLES; i++)
            {
                fpsSamplesWrapper[i] = (float) i;
                xValuesWrapper[i] = fpsSamples[i];
            }

            ImGui.begin(Icons.ChartLine + "  FPS Graph");
            ImGui.setCursorPos(ImGui.getCursorPosX() + EditorSystemManager.getCurrentTheme().framePadding.x,
                    ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
            Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().tertiaryColor);
            ImGui.checkbox("Vsync", vsync);
            ImGui.sameLine();
            ImGui.text("Current FPS: " + currentFPS);
            Theme.resetDefaultTextColor();

            if (vsync.get() != GameWindow.get().isVsync())
            {
                GameWindow.get().setVsync(vsync.get());
            }

            ImPlot.setNextPlotLimitsY(currentFPS > 1000 ? 200 : 0, maxFPS * 1.5, 1);
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
