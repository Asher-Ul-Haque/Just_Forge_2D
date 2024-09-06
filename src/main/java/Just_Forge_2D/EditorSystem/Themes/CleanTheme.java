package Just_Forge_2D.EditorSystem.Themes;

import Just_Forge_2D.EditorSystem.ImGUIManager;
import imgui.ImVec2;
import imgui.ImVec4;
import org.joml.Vector4f;

public class CleanTheme extends Theme
{
    public CleanTheme(boolean DARK)
    {
        this.secondaryColor = new ImVec4(0.129411765f, 0.1450980f, 0.16078f, 1.0f);
        this.primaryColor = new ImVec4(0.97647f, 0.97254902f, 0.968627541f, 1.0f);
        this.tertiaryColor = new ImVec4(0.203921569f, 0.22745f, 0.2509f, 1.0f);
        this.quaternaryColor = new ImVec4(0.8747f, 0.892156f, 0.91760f, 1.0f);
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
        this.popupBgColor = primaryColor;

        this.windowBorderSize = 0.0f;
        this.windowBorderColor = secondaryColor;
        this.windowBorderShadowColor = secondaryColor;

        this.windowTitleBgColor = secondaryColor;
        this.windowTitleBgActiveColor = secondaryColor;
        this.windowTitleBgCollapsedColor = tertiaryColor;
        this.windowRounding = 0.0f;

        this.windowPadding = new ImVec2(4.0f, 4.0f);

        this.buttonBgColor = secondaryColor;
        this.buttonBgHoverColor = tertiaryColor;
        this.buttonBgActiveColor = secondaryColor;

        this.framePadding = new ImVec2(4.0f, 4.0f);
        this.frameRounding = 4.0f;

        this.dockingBorderColor = tertiaryColor;
        this.dockingBgColor = secondaryColor;
        this.dockGripperColor = quaternaryColor;

        this.treeNodeBgColor = secondaryColor;
        this.treeNodeTextColor = primaryColor;
        this.treeNodeIndent = 16f;

        this.tabBgColor = secondaryColor;
        this.tabHoveredColor = tertiaryColor;
        this.tabActiveColor = secondaryColor;
        this.tabUnfocusedColor = secondaryColor;
        this.tabUnfocusedActiveColor = secondaryColor;
        this.tabRounding = 4f;
        this.tabBorder = 0f;

        this.checkMarkColor = this.primaryColor;
        this.frameBgColor = this.secondaryColor;
        this.frameActiveColor = this.tertiaryColor;
        this.frameHoverColor = this.secondaryColor;

        this.menuBarBg = this.secondaryColor;
        this.popupBg = this.primaryColor;
    }
}
