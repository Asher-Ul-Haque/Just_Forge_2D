package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import imgui.ImGui;

public class GridControls
{
    public static boolean snapToGrid = true;
    public static void render()
    {
        ImGui.begin(Icons.DigitalTachograph + "  Grid Controls");
        Widgets.text("");
        snapToGrid = Widgets.drawBoolControl(Icons.MousePointer + "  Snap to Grid", snapToGrid);
        GridlinesComponent.showGrid = Widgets.drawBoolControl((GridlinesComponent.showGrid ? Icons.Eye : Icons.EyeSlash) + "  Show Grid", GridlinesComponent.showGrid);
        Widgets.drawVec2Control(Icons.BorderAll + "  Grid Size", GridlinesComponent.gridSize);
        Widgets.colorPicker4(Icons.EyeDropper + "  Grid Color", GridlinesComponent.gridColor);
        ImGui.end();
    }
}