package Just_Forge_2D.Themes;

import Just_Forge_2D.Utils.Settings;
import imgui.ImVec4;

public class CatppuccinTheme extends Theme
{

    public CatppuccinTheme(boolean DARK) {
        // Catpuccino Color Palette (adjust as needed)
        ImVec4 text = new ImVec4(0.46f, 0.47f, 0.53f, 1.0f); // Text
        ImVec4 surface0 = new ImVec4(0.93f, 0.93f, 0.95f, 1.0f); // Surface 0
        ImVec4 surface1 = new ImVec4(0.89f, 0.89f, 0.91f, 1.0f); // Surface 1
        ImVec4 surface2 = new ImVec4(0.85f, 0.85f, 0.88f, 1.0f); // Surface 2
        ImVec4 accent = new ImVec4(0.68f, 0.71f, 0.94f, 1.0f); // Accent
        ImVec4 accent2 = new ImVec4(0.57f, 0.60f, 0.78f, 1.0f); // Accent 2

        if (DARK)
        {
            // Adjust colors for dark mode
            text = new ImVec4(0.95f, 0.94f, 0.92f, 1.0f);
            surface0 = new ImVec4(0.11f, 0.11f, 0.11f, 1.0f);
            surface1 = new ImVec4(0.16f, 0.16f, 0.16f, 1.0f);
            surface2 = new ImVec4(0.21f, 0.21f, 0.21f, 1.0f);
            accent = new ImVec4(0.72f, 0.77f, 0.97f, 1.0f);
            accent2 = new ImVec4(0.61f, 0.65f, 0.87f, 1.0f);
        }

        this.primaryColor = accent;
        this.secondaryColor = surface0;
        this.tertiaryColor = accent2;
        this.quaternaryColor = surface2;

        this.windowBgColor = surface0;
        this.windowChildBgColor = surface1;
        this.popupBgColor = surface1;
        this.popupRounding = 8;

        this.windowBorderSize = Settings.DEFAULT_WINDOW_BORDER_SIZE();
        this.windowBorderColor = surface2;
        this.windowBorderShadowColor = surface2;

        this.windowTitleBgColor = surface2;
        this.windowTitleBgActiveColor = surface2;
        this.windowTitleBgCollapsedColor = surface2;
        this.windowRounding = 8;

        this.windowPadding = Settings.DEFAULT_WINDOW_PADDING();

        this.buttonBgColor = surface2;
        this.buttonBgHoverColor = accent2;
        this.buttonBgActiveColor = accent;

        this.framePadding = Settings.DEFAULT_FRAME_PADDING();
        this.frameRounding = 8;

        this.dockingBorderColor = surface2;
        this.dockingBgColor = surface1;
        this.dockGripperColor = surface2;

        this.treeNodeBgColor = surface1;
        this.treeNodeTextColor = text;
        this.treeNodeIndent = Settings.DEFAULT_TREE_NODE_INDENT();

        this.tabBgColor = surface2;
        this.tabHoveredColor = accent2;
        this.tabActiveColor = accent;
        this.tabUnfocusedColor = surface2;
        this.tabUnfocusedActiveColor = surface2;
        this.tabRounding = 8;
        this.tabBorder = Settings.DEFAULT_TAB_BORDER();

        this.checkMarkColor = accent;
        this.frameBgColor = surface2;
        this.frameActiveColor = accent2;
        this.frameHoverColor = surface2;

        this.menuBarBg = surface2;
    }
}