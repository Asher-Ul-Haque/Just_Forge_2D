package Just_Forge_2D.EditorSystem.EditorComponents;

import Just_Forge_2D.EditorSystem.Forge;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Settings;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

// - - - Grid Lines
public class GridlinesComponent extends Component
{
    public static Vector2f gridSize = new Vector2f(Settings.GRID_WIDTH(), Settings.GRID_HEIGHT());
    public static Vector4f gridColor = new Vector4f(Settings.DEBUG_PENCIL_DEFAULT_COLOR(), 1.0f);
    public static boolean showGrid = Settings.SHOW_GRID();


    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (!showGrid || Forge.isRuntimePlaying) return;
        Camera camera = GameWindow.getCurrentScene().getCamera();
        if (camera.getZoom() > 5f) return;
        Vector2f cameraPos = camera.getPosition();
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int) (cameraPos.x / gridSize.x) - 1) * gridSize.x;
        float firstY = ((int) (cameraPos.y / gridSize.y) - 1) * gridSize.y;

        int numVertLines = (int) (projectionSize.x * camera.getZoom() / gridSize.x) + 5;
        int numHorLines = (int) (projectionSize.y * camera.getZoom() / gridSize.y) + 2;

        int height = (int) ((projectionSize.y * camera.getZoom()) + (gridSize.y * 5));
        int width = (int) ((projectionSize.x * camera.getZoom()) + (gridSize.x * 5));

        int maxLines = Math.max(numHorLines, numVertLines);
        for (int i = 0; i < maxLines; ++i)
        {
            float x = firstX + (gridSize.x * i);
            float y = firstY + (gridSize.y * i);

            if (i < numVertLines)
            {
                DebugPencil.addLine(new Vector2f(x, firstY), new Vector2f(x, firstY + height), new Vector3f(gridColor.x, gridColor.y, gridColor.z));
            }

            if (i < numHorLines)
            {
                DebugPencil.addLine(new Vector2f(firstX, y), new Vector2f(firstX + width, y), new Vector3f(gridColor.x, gridColor.y, gridColor.z));
            }
        }
    }
}
