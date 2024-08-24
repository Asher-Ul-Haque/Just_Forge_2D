package Just_Forge_2D.ForgeEditor.Themes;

import imgui.ImVec2;
import imgui.ImVec4;

public class IntelliTheme extends Theme
{
    public static ImVec4 primaryColor = new ImVec4(0.5843f, 0.35294f, 0.8784f, 1.0f);
    public static ImVec4 secondaryColor = new ImVec4(0.0784f, 0.08235f, 0.1333f, 0.5f);
    public static ImVec4 tertiaryColor = new ImVec4(0.894117f, 0.80784f, 1.0f, 1.0f);

    public IntelliTheme()
    {
        this.windowBgColor = secondaryColor;
        this.windowChildBgColor = secondaryColor;
        this.popupBgColor = secondaryColor;

        this.windowBorderSize = 4.0f;
        this.windowBorderColor = primaryColor;
        this.windowBorderShadowColor = primaryColor;

        this.windowTitleBgColor = primaryColor;
        this.windowTitleBgActiveColor = primaryColor;
        this.windowTitleBgCollapsedColor = primaryColor;
        this.windowRounding = 0.0f;

        this.windowPadding = new ImVec2(4.0f, 4.0f);

        this.buttonBgColor = primaryColor;
        this.buttonBgHoverColor = new ImVec4(primaryColor.x - 0.05f, primaryColor.y - 0.05f, primaryColor.z - 0.05f, primaryColor.w);
        this.buttonBgActiveColor = primaryColor;

        this.buttonTextColor = tertiaryColor;
        this.framePadding = new ImVec2(4.0f, 4.0f);
        this.frameRounding = 4.0f;

        applyTheme();
    }
}
