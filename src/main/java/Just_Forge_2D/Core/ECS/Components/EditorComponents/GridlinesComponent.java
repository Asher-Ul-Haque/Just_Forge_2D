package Just_Forge_2D.Core.ECS.Components.EditorComponents;

import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Renderer.DebugPencil;
import Just_Forge_2D.Utils.Configurations;
import org.joml.Vector2f;

// - - - Grid Lines
public class GridlinesComponent extends Component
{
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        Camera camera = ForgeDynamo.getCurrentScene().getCamera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();

        int firstX = ((int) (cameraPos.x / Configurations.GRID_WIDTH) - 1) * Configurations.GRID_WIDTH;
        int firstY = ((int) (cameraPos.y / Configurations.GRID_HEIGHT) - 1) * Configurations.GRID_HEIGHT;

        int numVertLines = (int) (projectionSize.x * camera.getZoom() / Configurations.GRID_WIDTH) + 2;
        int numHorLines = (int) (projectionSize.y * camera.getZoom() / Configurations.GRID_HEIGHT) + 2;

        int height = (int) (projectionSize.y * camera.getZoom() + Configurations.GRID_HEIGHT * 2);
        int width = (int) (projectionSize.x * camera.getZoom() + Configurations.GRID_WIDTH * 2);

        int maxLines = Math.max(numHorLines, numVertLines);
        for (int i = 0; i < maxLines; ++i)
        {
            int x = firstX + (Configurations.GRID_WIDTH * i);
            int y = firstY + (Configurations.GRID_HEIGHT * i);

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
