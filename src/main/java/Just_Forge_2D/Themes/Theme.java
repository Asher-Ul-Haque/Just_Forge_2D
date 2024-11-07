package Just_Forge_2D.Themes;

import imgui.ImGui;
import imgui.ImVec2;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;

public abstract class Theme
{
    public ImVec4 primaryColor;
    public ImVec4 secondaryColor;
    public ImVec4 tertiaryColor;
    public ImVec4 quaternaryColor;

    // - - - Window Styling - - -

    // - - - background
    public ImVec4 windowBgColor; // - - - background color
    public ImVec4 windowChildBgColor; // - - - child background color
    public ImVec4 popupBgColor; // - - - popup Bg Color

    // - - - border
    public float windowBorderSize;
    public ImVec4 windowBorderColor;
    public ImVec4 windowBorderShadowColor;

    // - - - title bar
    public ImVec4 windowTitleBgColor;
    public ImVec4 windowTitleBgActiveColor;
    public ImVec4 windowTitleBgCollapsedColor;
    public float windowRounding;

    // - - - text color
    public ImVec4 textColor;
    public ImVec4 textAltColor;

    // - - - padding and miscellaneous
    public ImVec2 windowPadding;

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


    // - - - Docking - - -

    public ImVec4 dockingBgColor;
    public ImVec4 dockingBorderColor;
    public ImVec4 dockGripperColor;

    public void applyDockingTheme()
    {
        ImGui.getStyle().setColor(ImGuiCol.DockingEmptyBg, dockingBgColor.x, dockingBgColor.y, dockingBgColor.z, dockingBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.DockingPreview, dockingBorderColor.x, dockingBorderColor.y, dockingBorderColor.z, dockingBorderColor.w);
        ImGui.getStyle().setColor(ImGuiCol.ResizeGrip, dockGripperColor.x, dockGripperColor.y, dockGripperColor.z, dockGripperColor.w);
    }


    // - - - Tree Node - - -

    public ImVec4 treeNodeBgColor;
    public ImVec4 treeNodeTextColor;
    public float treeNodeIndent;

    public void applyTreeNodeTheme()
    {
        ImGui.getStyle().setColor(ImGuiCol.Header, treeNodeBgColor.x, treeNodeBgColor.y, treeNodeBgColor.z, treeNodeBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.Text, treeNodeTextColor.x, treeNodeTextColor.y, treeNodeTextColor.z, treeNodeTextColor.w);
        ImGui.getStyle().setIndentSpacing(treeNodeIndent);
    }



    // - - - Button - - -

    // - - - background
    public ImVec4 buttonBgColor;
    public ImVec4 buttonBgHoverColor;
    public ImVec4 buttonBgActiveColor;

    public void applyButtonTheme()
    {
        // - - - background
        ImGui.getStyle().setColor(ImGuiCol.Button, buttonBgColor.x, buttonBgColor.y, buttonBgColor.z, buttonBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.ButtonActive, buttonBgActiveColor.x, buttonBgActiveColor.y, buttonBgActiveColor.z, buttonBgActiveColor.w);
        ImGui.getStyle().setColor(ImGuiCol.ButtonHovered, buttonBgHoverColor.x, buttonBgHoverColor.y, buttonBgHoverColor.z, buttonBgHoverColor.w);
    }


    // - - - Frame - - -
    public ImVec2 framePadding;
    public float frameRounding;
    public ImVec4 frameBgColor;
    public ImVec4 frameActiveColor;
    public ImVec4 frameHoverColor;

    public void applyFrameTheme()
    {
        ImGui.getStyle().setColor(ImGuiCol.FrameBg, frameBgColor.x, frameBgColor.y, frameBgColor.z, frameBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.FrameBgHovered, frameHoverColor.x, frameHoverColor.y, frameHoverColor.z, frameHoverColor.w);
        ImGui.getStyle().setColor(ImGuiCol.FrameBgActive, frameActiveColor.x, frameActiveColor.y, frameActiveColor.z, frameActiveColor.w);

        // - - - padding
        ImGui.getStyle().setFramePadding(framePadding.x, framePadding.y);
        ImGui.getStyle().setFrameRounding(frameRounding);
    }


    // - - - Tabs - - -

    public ImVec4 tabBgColor;
    public ImVec4 tabHoveredColor;
    public ImVec4 tabActiveColor;
    public ImVec4 tabUnfocusedColor;
    public ImVec4 tabUnfocusedActiveColor;
    public float tabRounding;
    public float tabBorder;

    public void applyTabTheme()
    {
        ImGui.getStyle().setColor(ImGuiCol.Tab, tabBgColor.x, tabBgColor.y, tabBgColor.z, tabBgColor.w);
        ImGui.getStyle().setColor(ImGuiCol.TabHovered, tabHoveredColor.x, tabHoveredColor.y, tabHoveredColor.z, tabHoveredColor.w);
        ImGui.getStyle().setColor(ImGuiCol.TabActive, tabActiveColor.x, tabActiveColor.y, tabActiveColor.z, tabActiveColor.w);
        ImGui.getStyle().setColor(ImGuiCol.TabUnfocused, tabUnfocusedColor.x, tabUnfocusedColor.y, tabUnfocusedColor.z, tabUnfocusedColor.w);
        ImGui.getStyle().setColor(ImGuiCol.TabUnfocusedActive, tabUnfocusedActiveColor.x, tabUnfocusedActiveColor.y, tabUnfocusedActiveColor.z, tabUnfocusedActiveColor.w);
        ImGui.getStyle().setColor(ImGuiCol.HeaderHovered, tabHoveredColor.x, tabHoveredColor.y, tabHoveredColor.z, tabHoveredColor.w);
        ImGui.getStyle().setTabRounding(tabRounding);
        ImGui.getStyle().setTabBorderSize(tabBorder);
    }


    // - - - Menu bar BG - - -

    public ImVec4 menuBarBg;

    public void applyMenuBarTheme()
    {
        ImGui.getStyle().setColor(ImGuiCol.MenuBarBg, menuBarBg.x, menuBarBg.y, menuBarBg.z, menuBarBg.w);
    }


    // - - - Pop Up - - -

    public float popupRounding;

    public void applyPopupBg()
    {
        ImGui.getStyle().setColor(ImGuiCol.PopupBg, popupBgColor.x, popupBgColor.y, popupBgColor.z, popupBgColor.w);
        ImGui.getStyle().setPopupRounding(popupRounding);
    }


    // - - - Checkbox - - -

    public ImVec4 checkMarkColor;

    public void applyCheckboxTheme()
    {
        ImGui.getStyle().setColor(ImGuiCol.CheckMark, checkMarkColor.x, checkMarkColor.y, checkMarkColor.z, checkMarkColor.w);
    }


    // - - - apply all
    public void applyTheme()
    {
        applyWindowTheme();
        applyButtonTheme();
        applyTreeNodeTheme();
        applyDockingTheme();
        applyCheckboxTheme();
        applyTabTheme();
        applyFrameTheme();
        applyMenuBarTheme();
        applyPopupBg();
    }

    public static void setDefaultTextColor(ImVec4 COLOR)
    {
        ImGui.pushStyleColor(ImGuiCol.Text, COLOR.x, COLOR.y, COLOR.z, COLOR.w);
    }

    public static void resetDefaultTextColor()
    {
        ImGui.popStyleColor();
    }
}