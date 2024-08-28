package Just_Forge_2D.EditorSystem.Themes;

import Just_Forge_2D.EditorSystem.Themes.Theme;
import imgui.ImVec2;
import imgui.ImVec4;

public class IntelliTheme extends Theme
{
    public static ImVec4 quaternaryColor = new ImVec4(0.862f, 0.862f, 0.862f, 1.0f);
    public static ImVec4 secondaryColor = new ImVec4(0.9411f, 0.9411f, 0.9411f, 1.0f);
    public static ImVec4 tertiaryColor = new ImVec4(0.0f, 0.0f, 0.0f, 1.0f);
    public static ImVec4 primaryColor = new ImVec4(0.188235f, 0.188235f, 0.188235f, 1.0f);

    public IntelliTheme()
    {
        this.windowBgColor = secondaryColor;
        this.windowChildBgColor = secondaryColor;
        this.popupBgColor = secondaryColor;

        this.windowBorderSize = 4.0f;
        this.windowBorderColor = primaryColor;
        this.windowBorderShadowColor = quaternaryColor;

        this.windowTitleBgColor = primaryColor;
        this.windowTitleBgActiveColor = primaryColor;
        this.windowTitleBgCollapsedColor = primaryColor;
        this.windowRounding = 4.0f;

        this.windowPadding = new ImVec2(4.0f, 4.0f);

        this.buttonBgColor = primaryColor;
        this.buttonBgHoverColor = new ImVec4(primaryColor.x - 0.05f, primaryColor.y - 0.05f, primaryColor.z - 0.05f, primaryColor.w);
        this.buttonBgActiveColor = primaryColor;

        this.framePadding = new ImVec2(4.0f, 4.0f);
        this.frameRounding = 8.0f;
    }
}
