package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Camera;
import imgui.ImGui;

public class CameraControlWindow
{
    public static void render()
    {
        ImGui.begin("Camera Controls");

        Camera worldCamera = Mouse.getWorldCamera();
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
