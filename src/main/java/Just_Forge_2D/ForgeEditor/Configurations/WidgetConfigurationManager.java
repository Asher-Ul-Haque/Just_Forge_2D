package Just_Forge_2D.ForgeEditor.Configurations;

import imgui.ImGui;
import imgui.flag.ImGuiCol;

public class WidgetConfigurationManager
{
    public static void configureSlider()
    {
        ImGui.getStyle().setColor(ImGuiCol.SliderGrab, ColorScheme.sliderGrab.x, ColorScheme.sliderGrab.y, ColorScheme.sliderGrab.z, ColorScheme.sliderGrab.w);
        ImGui.getStyle().setColor(ImGuiCol.SliderGrabActive, ColorScheme.sliderGrabActive.x, ColorScheme.sliderGrabActive.y, ColorScheme.sliderGrabActive.z, ColorScheme.sliderGrabActive.w);
        ImGui.getStyle().setGrabRounding(WidgetConfig.grabRounding);
        ImGui.getStyle().setGrabMinSize(WidgetConfig.minGrabSize);
    }

    public static void configureTextBox()
    {
        ImGui.getStyle().setColor(ImGuiCol.FrameBg, ColorScheme.frameBg.x, ColorScheme.frameBg.y, ColorScheme.frameBg.z, ColorScheme.frameBg.w);
        ImGui.getStyle().setColor(ImGuiCol.FrameBgHovered, ColorScheme.frameBgHover.x, ColorScheme.frameBgHover.y, ColorScheme.frameBgHover.z, ColorScheme.frameBgHover.w);

        ImGui.getStyle().setFrameRounding(WidgetConfig.frameRounding);
        ImGui.getStyle().setFramePadding(WidgetConfig.framePadding.x, WidgetConfig.framePadding.y);
        ImGui.getStyle().setFrameBorderSize(WidgetConfig.frameBorderSize);
    }
}
