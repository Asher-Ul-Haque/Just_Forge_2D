package Just_Forge_2D.EditorSystem.Themes;

import imgui.ImVec2;
import imgui.ImVec4;

public class CleanTheme extends Theme
{
    public CleanTheme()
    {
        this.primaryColor = new ImVec4(0.188235f, 0.188235f, 0.188235f, 1.0f);
        this.secondaryColor = new ImVec4(0.9411f, 0.9411f, 0.9411f, 1.0f);
        this.tertiaryColor = new ImVec4(0.0f, 0.0f, 0.0f, 1.0f);
        this.quaternaryColor = new ImVec4(0.862f, 0.862f, 0.862f, 1.0f);

        this.windowBgColor = secondaryColor;
        this.windowChildBgColor = secondaryColor;
        this.popupBgColor = secondaryColor;

        this.windowBorderSize = 4.0f;
        this.windowBorderColor = primaryColor;
        this.windowBorderShadowColor = quaternaryColor;

        this.windowTitleBgColor = tertiaryColor;
        this.windowTitleBgActiveColor = tertiaryColor;
        this.windowTitleBgCollapsedColor = tertiaryColor;
        this.windowRounding = 0.0f;

        this.windowPadding = new ImVec2(0.0f, 0.0f);

        this.buttonBgColor = primaryColor;
        this.buttonBgHoverColor = tertiaryColor;
        this.buttonBgActiveColor = primaryColor;

        this.framePadding = new ImVec2(4.0f, 4.0f);
        this.frameRounding = 0.0f;

        this.dockingBorderColor = tertiaryColor;
        this.dockingBgColor = primaryColor;
        this.dockGripperColor = secondaryColor;

        this.treeNodeBgColor = primaryColor;
        this.treeNodeTextColor = secondaryColor;
        this.treeNodeIndent = 20f;

        this.tabBgColor = tertiaryColor;
        this.tabHoveredColor = tertiaryColor;
        this.tabActiveColor = tertiaryColor;
        this.tabUnfocusedColor = primaryColor;
        this.tabUnfocusedActiveColor = tertiaryColor;
        this.tabRounding = 0f;
        this.tabBorder = 4f;

        this.checkMarkColor = this.secondaryColor;
        this.frameBgColor = this.tertiaryColor;
        this.frameActiveColor = this.tertiaryColor;
        this.frameHoverColor = this.tertiaryColor;

        this.menuBarBg = this.tertiaryColor;
        this.popupBg = this.primaryColor;
    }
}
