package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents;

import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.DefaultValues;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

// - - - Grid Lines
public class GridlinesComponent extends Component
{
    public static Vector2f gridSize = new Vector2f(DefaultValues.GRID_WIDTH, DefaultValues.GRID_HEIGHT);
    public static Vector4f gridColor = new Vector4f(DefaultValues.DEBUG_PENCIL_DEFAULT_COLOR, 1.0f);



    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        Camera camera = MainWindow.getCurrentScene().getCamera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int) (cameraPos.x / gridSize.x)) * gridSize.x;
        float firstY = ((int) (cameraPos.y / gridSize.y)) * gridSize.y;

        int numVertLines = (int) (projectionSize.x * camera.getZoom() / gridSize.x) + 2;
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
