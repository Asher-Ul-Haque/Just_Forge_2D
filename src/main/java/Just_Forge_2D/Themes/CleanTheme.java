package Just_Forge_2D.Themes;

import Just_Forge_2D.Utils.Settings;
import imgui.ImVec4;

public class CleanTheme extends Theme
{
    public CleanTheme(boolean DARK)
    {
        this.secondaryColor = Settings.DEFAULT_CLEAN_THEME_SECONDARY_COLOR();
        this.primaryColor = Settings.DEFAULT_CLEAN_THEME_PRIMARY_COLOR();
        this.tertiaryColor = Settings.DEFAULT_CLEAN_THEME_TERTIARY_COLOR();
        this.quaternaryColor = Settings.DEFAULT_CLEAN_THEME_QUATERNARY_COLOR();
        if (DARK)
        {
            ImVec4 temp = new ImVec4(this.secondaryColor);
            this.secondaryColor = this.primaryColor;
            this.primaryColor = temp;
            temp = new ImVec4(tertiaryColor);
            this.tertiaryColor = this.quaternaryColor;
            this.quaternaryColor = temp;
        }

        this.windowBgColor = primaryColor;
        this.windowChildBgColor = primaryColor;
        this.popupBgColor = secondaryColor;
        this.popupRounding = Settings.DEFAULT_POPUP_ROUNDING();

        this.windowBorderSize = Settings.DEFAULT_WINDOW_BORDER_SIZE();
        this.windowBorderColor = secondaryColor;
        this.windowBorderShadowColor = secondaryColor;

        this.windowTitleBgColor = secondaryColor;
        this.windowTitleBgActiveColor = secondaryColor;
        this.windowTitleBgCollapsedColor = tertiaryColor;
        this.windowRounding = Settings.DEFAULT_WINDOW_ROUNDING();

        this.windowPadding = Settings.DEFAULT_WINDOW_PADDING();

        this.buttonBgColor = secondaryColor;
        this.buttonBgHoverColor = tertiaryColor;
        this.buttonBgActiveColor = secondaryColor;

        this.framePadding = Settings.DEFAULT_FRAME_PADDING();
        this.frameRounding = Settings.DEFAULT_FRAME_ROUNDING();

        this.dockingBorderColor = tertiaryColor;
        this.dockingBgColor = secondaryColor;
        this.dockGripperColor = quaternaryColor;

        this.treeNodeBgColor = secondaryColor;
        this.treeNodeTextColor = primaryColor;
        this.treeNodeIndent = Settings.DEFAULT_TREE_NODE_INDENT();

        this.tabBgColor = secondaryColor;
        this.tabHoveredColor = tertiaryColor;
        this.tabActiveColor = secondaryColor;
        this.tabUnfocusedColor = secondaryColor;
        this.tabUnfocusedActiveColor = secondaryColor;
        this.tabRounding = Settings.DEFAULT_TAB_ROUNDING();
        this.tabBorder = Settings.DEFAULT_TAB_BORDER();

        this.checkMarkColor = this.primaryColor;
        this.frameBgColor = this.secondaryColor;
        this.frameActiveColor = this.tertiaryColor;
        this.frameHoverColor = this.secondaryColor;

        this.menuBarBg = this.secondaryColor;
    }
}