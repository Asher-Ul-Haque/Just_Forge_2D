package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.EditorCameraComponent;
import Just_Forge_2D.EntityComponentSystem.Components.EditorComponents.GridlinesComponent;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Camera;
import imgui.ImGui;
import org.joml.Vector4f;

public class GridControls
{
    public static boolean snapToGrid = true;
    public static void render()
    {
        ImGui.begin("Grid Controls");
        boolean val = snapToGrid;
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        if (ImGui.checkbox( "Snap To Grid", val))
        {
            val = !val;
            snapToGrid = val;
        }
        Theme.resetDefaultTextColor();
        Widgets.drawVec2Control("Grid Size", GridlinesComponent.gridSize);
        Widgets.colorPicker4("Grid Color", GridlinesComponent.gridColor);

        ImGui.end();
    }
}