package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;

public class CameraControlWindow
{
    public static void render()
    {
        ImGui.begin(Icons.Camera + "  Camera Controls");
        Widgets.text("");

        Camera worldCamera = GameWindow.getCurrentScene().getCamera();
        if (worldCamera != null)
        {
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);

            Widgets.drawVec2Control(Icons.Crosshairs + "  Position", worldCamera.getPosition());
            worldCamera.setZoom(Widgets.drawFloatControl(Icons.Binoculars + "  Zoom", worldCamera.getZoom()));
            Widgets.drawVec3Control(Icons.Redo + "  Rotation", worldCamera.getRotationVector());
            EditorCameraComponent.dragDebounce = Widgets.drawFloatControl(Icons.MousePointer + "  Drag Debounce", EditorCameraComponent.dragDebounce);
            EditorCameraComponent.lerpTime = Widgets.drawFloatControl(Icons.Stopwatch + "  Lerp Time", EditorCameraComponent.lerpTime);
            EditorCameraComponent.dragSensitivity = Widgets.drawFloatControl("Drag Sensitivity", EditorCameraComponent.dragSensitivity);
            EditorCameraComponent.scrollSensitivity = Widgets.drawFloatControl("Scroll Sensitivity", EditorCameraComponent.scrollSensitivity);
        }
        ImGui.end();
    }
}
