package Just_Forge_2D.ForgeEditor.Themes;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;

public abstract class Theme
{


    // - - - Window Styling - - -

    // - - - background
    public  ImVec4 windowBgColor; // - - - background color
    public  ImVec4 windowChildBgColor; // - - - child background color
    public  ImVec4 popupBgColor; // - - - popup Bg Color

    // - - - border
    public  float windowBorderSize;
    public  ImVec4 windowBorderColor;
    public  ImVec4 windowBorderShadowColor;

    // - - - title bar
    public  ImVec4 windowTitleBgColor;
    public  ImVec4 windowTitleBgActiveColor;
    public  ImVec4 windowTitleBgCollapsedColor;
    public  float windowRounding;

    // - - - padding and miscellaneous
    public  ImVec2 windowPadding;

    public void applyWindowTheme()
    {
        // - - - background
        ImGui.getStyle().setColor(ImGuiCol.WindowBg, windowBgColor.x, windowBgColor.y, windowBgColor.z, windowBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.ChildBg, windowChildBgColor.x, windowChildBgColor.y, windowChildBgColor.z, windowChildBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.PopupBg, popupBgColor.x, popupBgColor.y, popupBgColor.z, popupBgColor.w);

        // - - - border
        ImGui.getStyle().setWindowBorderSize(windowBorderSize);
        ImGui.getStyle().setColor(ImGuiCol.Border, windowBorderColor.x, windowBorderColor.y, windowBorderColor.z, windowBorderColor.w);
        ImGui.getStyle().setColor(ImGuiCol.BorderShadow, windowBorderShadowColor.x, windowBorderShadowColor.y, windowBorderShadowColor.z, windowBorderShadowColor.w);

        // - - - title bar
        ImGui.getStyle().setColor(ImGuiCol.TitleBg, windowTitleBgColor.x, windowTitleBgColor.y, windowTitleBgColor.z, windowTitleBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.TitleBgActive, windowTitleBgActiveColor.x, windowTitleBgActiveColor.y, windowTitleBgColor.z, windowTitleBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.TitleBgCollapsed, windowTitleBgCollapsedColor.x, windowTitleBgCollapsedColor.y, windowTitleBgCollapsedColor.z, windowTitleBgCollapsedColor.w);
        ImGui.getStyle().setWindowRounding(windowRounding);

        // - - - padding and miscellaneous
        ImGui.getStyle().setWindowPadding(windowPadding.x, windowPadding.y);
    }


    // - - - Button - - -

    // - - - background
    public ImVec4 buttonBgColor;
    public ImVec4 buttonBgHoverColor;
    public ImVec4 buttonBgActiveColor;

    // - - - text
    public ImVec4 buttonTextColor;

    // - - - padding and rounding
    public ImVec2 framePadding;
    public float frameRounding;

    public void applyButtonTheme()
    {
        // - - - background
        ImGui.getStyle().setColor(ImGuiCol.Button, buttonBgColor.x, buttonBgColor.y, buttonBgColor.z, buttonBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.ButtonActive, buttonBgActiveColor.x, buttonBgActiveColor.y, buttonBgActiveColor.z, buttonBgActiveColor.w);
        ImGui.getStyle().setColor(ImGuiCol.ButtonHovered, buttonBgHoverColor.x, buttonBgHoverColor.y, buttonBgHoverColor.z, buttonBgHoverColor.w);

        // - - - text
        ImGui.getStyle().setColor(ImGuiCol.Text, buttonTextColor.x, buttonTextColor.y, buttonTextColor.z, buttonTextColor.w);

        // - - - padding
        ImGui.getStyle().setFramePadding(framePadding.x, framePadding.y);
        ImGui.getStyle().setFrameRounding(frameRounding);
    }


    // - - - apply all
    public void applyTheme()
    {
        applyWindowTheme();
        applyButtonTheme();
    }
}