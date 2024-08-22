package Just_Forge_2D.ForgeEditor.Configurations;

import org.joml.Vector4f;

public record ColorScheme()
{
    // - - - frame color
    public static Vector4f frameBg = new Vector4f(0.2f, 0.2f, 0.2f, 0.2f);
    public static Vector4f frameBgHover = new Vector4f(0.3f, 0.3f, 0.3f, 0.3f);
    public static Vector4f frameBgActive = new Vector4f(0.4f, 0.4f, 0.4f, 0.4f);

    // - - - slider color
    public static Vector4f sliderGrab = new Vector4f(0.2f, 0.7f, 0.2f, 1.0f);
    public static Vector4f sliderGrabActive = new Vector4f(0.3f, 0.8f, 0.3f, 1.0f);
}
