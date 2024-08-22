package Just_Forge_2D.ForgeEditor.Configurations;

import org.joml.Vector2f;

public record WidgetConfig()
{
    // - - - slider
    public static float grabRounding = 4.0f;
    public static float minGrabSize = 10.0f;

    // - - - frame padding and rounding
    public static Vector2f framePadding = new Vector2f(8.0f, 4.0f);
    public static float frameRounding = 6.0f;
    public static float frameBorderSize = 1.0f;
}
