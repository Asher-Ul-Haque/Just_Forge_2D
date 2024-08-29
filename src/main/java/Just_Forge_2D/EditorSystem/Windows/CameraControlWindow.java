package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Camera;
import imgui.ImGui;

public class CameraControlWindow
{
    public static void render()
    {
        Camera worldCamera = Mouse.getWorldCamera();
        if (worldCamera != null)
        {
            ImGui.begin("Camera Controls");
            Widgets.drawVec2Control("Position", worldCamera.position);
            worldCamera.setZoom(Widgets.drawFloatControl("Zoom", worldCamera.getZoom()));
            ImGui.end();
        }
    }
}
