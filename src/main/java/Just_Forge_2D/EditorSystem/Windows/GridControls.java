package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Camera;
import imgui.ImGui;
import org.joml.Vector4f;

public class GridControls
{
    public static void render()
    {
        ImGui.begin("Grid Controls");
        Widgets.drawVec2Control("Grid Size", GridlinesComponent.gridSize);
        Widgets.colorPicker4("Grid Color", GridlinesComponent.gridColor);
        ImGui.end();
    }
}