package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import imgui.ImGui;

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
        val = GridlinesComponent.showGrid;
        if (ImGui.checkbox( "Show Grid", val))
        {
            val = !val;
            GridlinesComponent.showGrid = val;
        }
        Theme.resetDefaultTextColor();
        Widgets.drawVec2Control("Grid Size", GridlinesComponent.gridSize);
        Widgets.colorPicker4("Grid Color", GridlinesComponent.gridColor);
        ImGui.end();
    }
}