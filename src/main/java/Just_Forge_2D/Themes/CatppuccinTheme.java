package Just_Forge_2D.Themes;

import Just_Forge_2D.Utils.Settings;
import imgui.ImVec2;
import imgui.ImVec4;

@Deprecated
public class CatppuccinTheme extends Theme
{
    public CatppuccinTheme(boolean DARK)
    {
        // Pastel colors for light and dark modes
        ImVec4 text = new ImVec4(0.40f, 0.45f, 0.50f, 1.0f);        // Text: Soft gray-blue
        ImVec4 surface0 = new ImVec4(0.98f, 0.96f, 0.95f, 1.0f);    // Surface 0: Very light pink
        ImVec4 surface1 = new ImVec4(0.92f, 0.90f, 0.92f, 1.0f);    // Surface 1: Lavender gray
        ImVec4 surface2 = new ImVec4(0.85f, 0.82f, 0.84f, 1.0f);    // Surface 2: Light mauve
        ImVec4 accent = new ImVec4(0.67f, 0.85f, 0.92f, 1.0f);      // Accent: Soft blue
        ImVec4 accent2 = new ImVec4(0.83f, 0.68f, 0.79f, 1.0f);     // Accent 2: Pastel purple-pink
        ImVec4 highlight = new ImVec4(0.73f, 0.90f, 0.78f, 1.0f);   // Highlight: Mint green

        if (DARK)
        {
            // Dark Mode Adjustments
            text = new ImVec4(0.92f, 0.90f, 0.85f, 1.0f);          // Text: Off-white
            surface0 = new ImVec4(0.16f, 0.14f, 0.18f, 1.0f);      // Surface 0: Deep mauve
            surface1 = new ImVec4(0.24f, 0.22f, 0.26f, 1.0f);      // Surface 1: Dark lavender
            surface2 = new ImVec4(0.32f, 0.30f, 0.34f, 1.0f);      // Surface 2: Charcoal purple
            accent = new ImVec4(0.75f, 0.82f, 0.94f, 1.0f);        // Accent: Soft pastel blue
            accent2 = new ImVec4(0.88f, 0.72f, 0.80f, 1.0f);       // Accent 2: Pastel pink-lavender
            highlight = new ImVec4(0.78f, 0.92f, 0.80f, 1.0f);     // Highlight: Light mint green
        }

        // Text and primary colors
        this.textColor = text;
        this.textAltColor = text;
        this.primaryColor = accent;
        this.secondaryColor = surface0;
        this.tertiaryColor = accent2;
        this.quaternaryColor = surface2;

        // Window and popup settings
        this.windowBgColor = surface0;
        this.windowChildBgColor = surface1;
        this.popupBgColor = surface1;
        this.popupRounding = 8;

        this.windowBorderSize = Settings.DEFAULT_WINDOW_BORDER_SIZE();
        this.windowBorderColor = surface2;
        this.windowBorderShadowColor = surface1;

        this.windowTitleBgColor = surface2;
        this.windowTitleBgActiveColor = accent;
        this.windowTitleBgCollapsedColor = surface2;
        this.windowRounding = 8;
        this.windowPadding = Settings.DEFAULT_WINDOW_PADDING();

        // Button colors
        this.buttonBgColor = surface2;
        this.buttonBgHoverColor = highlight;
        this.buttonBgActiveColor = accent;

        // Frame colors and rounding
        this.frameBgColor = surface2;
        this.frameHoverColor = highlight;
        this.frameActiveColor = accent2;
        this.frameRounding = 8;

        // Tabs colors and rounding
        this.tabBgColor = surface2;
        this.tabHoveredColor = accent2;
        this.tabActiveColor = accent;
        this.tabUnfocusedColor = surface2;
        this.tabUnfocusedActiveColor = surface2;
        this.tabRounding = 8;
        this.tabBorder = Settings.DEFAULT_TAB_BORDER();

        // Sliders and drag controls
        this.framePadding = new ImVec2(4, 4);
        this.popupRounding = 8;


        // Checkboxes and radio buttons
        this.checkMarkColor = accent;


        // Tooltips, menus, and docking
        this.menuBarBg = surface2;
        this.dockingBorderColor = surface2;
        this.dockingBgColor = surface1;
        this.dockGripperColor = surface2;

        // Tree and table elements
        this.treeNodeBgColor = surface1;
        this.treeNodeTextColor = text;
        this.treeNodeIndent = Settings.DEFAULT_TREE_NODE_INDENT();
    }
}
