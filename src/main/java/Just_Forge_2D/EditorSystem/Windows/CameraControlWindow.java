package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.SceneSystem.Camera;
import imgui.ImGui;

public class CameraControlWindow
{
    public static void render()
    {
        ImGui.begin("Camera Controls");

        Camera worldCamera = MainWindow.getCurrentScene().getCamera();
        if (worldCamera != null)
        {
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);

            Widgets.drawVec2Control("Position", worldCamera.position);
            worldCamera.setZoom(Widgets.drawFloatControl("Zoom", worldCamera.getZoom()));
            EditorCameraComponent.dragDebounce = Widgets.drawFloatControl("Drag Debounce", EditorCameraComponent.dragDebounce);
            EditorCameraComponent.lerpTime = Widgets.drawFloatControl("Lerp Time", EditorCameraComponent.lerpTime);
            EditorCameraComponent.dragSensitivity = Widgets.drawFloatControl("Drag Sensitivity", EditorCameraComponent.dragSensitivity);
            EditorCameraComponent.scrollSensitivity = Widgets.drawFloatControl("Scroll Sensitivity", EditorCameraComponent.scrollSensitivity);
        }
        ImGui.end();
    }
}
