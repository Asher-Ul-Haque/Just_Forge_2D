package Just_Forge_2D.EditorSystem.Themes;

import imgui.ImVec2;
import imgui.ImVec4;

public class CleanTheme extends Theme
{
    public CleanTheme()
    {
        this.secondaryColor = new ImVec4(0.129411765f, 0.1450980f, 0.16078f, 1.0f);
        this.primaryColor = new ImVec4(0.97647f, 0.97254902f, 0.968627541f, 1.0f);
        this.tertiaryColor = new ImVec4(0.0f, 0.0f, 0.0f, 1.0f);
        this.quaternaryColor = new ImVec4(0.862f, 0.862f, 0.862f, 1.0f);

        this.windowBgColor = primaryColor;
        this.windowChildBgColor = primaryColor;
        this.popupBgColor = primaryColor;

        this.windowBorderSize = 0.0f;
        this.windowBorderColor = secondaryColor;
        this.windowBorderShadowColor = quaternaryColor;

        this.windowTitleBgColor = tertiaryColor;
        this.windowTitleBgActiveColor = tertiaryColor;
        this.windowTitleBgCollapsedColor = tertiaryColor;
        this.windowRounding = 0.0f;

        this.windowPadding = new ImVec2(0.0f, 0.0f);

        this.buttonBgColor = secondaryColor;
        this.buttonBgHoverColor = tertiaryColor;
        this.buttonBgActiveColor = primaryColor;

        this.framePadding = new ImVec2(4.0f, 4.0f);
        this.frameRounding = 0.0f;

        this.dockingBorderColor = tertiaryColor;
        this.dockingBgColor = primaryColor;
        this.dockGripperColor = secondaryColor;

        this.treeNodeBgColor = secondaryColor;
        this.treeNodeTextColor = primaryColor;
        this.treeNodeIndent = 16f;

        this.tabBgColor = primaryColor;
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
