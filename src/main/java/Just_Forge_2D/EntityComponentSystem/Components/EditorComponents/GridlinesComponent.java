package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.RenderingSystems.DebugPencil;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.EditorWindow;
import org.joml.Vector2f;

// - - - Grid Lines
public class GridlinesComponent extends Component
{
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        Camera camera = EditorWindow.getCurrentScene().getCamera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int) (cameraPos.x / Configurations.GRID_WIDTH)) * Configurations.GRID_WIDTH;
        float firstY = ((int) (cameraPos.y / Configurations.GRID_HEIGHT)) * Configurations.GRID_HEIGHT;

        int numVertLines = (int) (projectionSize.x * camera.getZoom() / Configurations.GRID_WIDTH) + 2;
        int numHorLines = (int) (projectionSize.y * camera.getZoom() / Configurations.GRID_HEIGHT) + 2;

        int height = (int) ((projectionSize.y * camera.getZoom()) + (Configurations.GRID_HEIGHT * 5));
        int width = (int) ((projectionSize.x * camera.getZoom()) + (Configurations.GRID_WIDTH * 5));

        int maxLines = Math.max(numHorLines, numVertLines);
        for (int i = 0; i < maxLines; ++i)
        {
            float x = firstX + (Configurations.GRID_WIDTH * i);
            float y = firstY + (Configurations.GRID_HEIGHT * i);

            if (i < numVertLines)
            {
                DebugPencil.addLine(new Vector2f(x, firstY), new Vector2f(x, firstY + height));
            }

            if (i < numHorLines)
            {
                DebugPencil.addLine(new Vector2f(firstX, y), new Vector2f(firstX + width, y));
            }
        }
    }
}
