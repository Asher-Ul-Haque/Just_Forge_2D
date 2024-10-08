package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.WindowSystem.MainWindow;
import imgui.ImGui;
import org.joml.Vector4f;

public class CameraControlWindow
{
    public static void render()
    {
        ImGui.begin("Camera Controls");

        Camera worldCamera = MainWindow.getCurrentScene().getCamera();
        if (worldCamera != null)
        {
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);

            Widgets.drawVec2Control("Position", worldCamera.getPosition());
            worldCamera.setZoom(Widgets.drawFloatControl("Zoom", worldCamera.getZoom()));
            Widgets.drawVec3Control("Rotation", worldCamera.getRotationVector());
            EditorCameraComponent.dragDebounce = Widgets.drawFloatControl("Drag Debounce", EditorCameraComponent.dragDebounce);
            EditorCameraComponent.lerpTime = Widgets.drawFloatControl("Lerp Time", EditorCameraComponent.lerpTime);
            EditorCameraComponent.dragSensitivity = Widgets.drawFloatControl("Drag Sensitivity", EditorCameraComponent.dragSensitivity);
            EditorCameraComponent.scrollSensitivity = Widgets.drawFloatControl("Scroll Sensitivity", EditorCameraComponent.scrollSensitivity);
        }
        Vector4f clearColor = MainWindow.get().getClearColor();
        Widgets.colorPicker4("Background", clearColor);
        if (!MainWindow.get().getClearColor().equals(clearColor)) MainWindow.get().setClearColor(clearColor);
        ImGui.end();
    }
}
