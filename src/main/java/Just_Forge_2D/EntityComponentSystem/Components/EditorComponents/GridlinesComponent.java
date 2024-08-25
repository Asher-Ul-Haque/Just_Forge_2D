package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents;

import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EditorSystem.EditorWindow;
import Just_Forge_2D.Renderer.DebugPencil;
import Just_Forge_2D.Utils.DefaultValues;
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

        float firstX = ((int) (cameraPos.x / DefaultValues.GRID_WIDTH)) * DefaultValues.GRID_WIDTH;
        float firstY = ((int) (cameraPos.y / DefaultValues.GRID_HEIGHT)) * DefaultValues.GRID_HEIGHT;

        int numVertLines = (int) (projectionSize.x * camera.getZoom() / DefaultValues.GRID_WIDTH) + 2;
        int numHorLines = (int) (projectionSize.y * camera.getZoom() / DefaultValues.GRID_HEIGHT) + 2;

        int height = (int) ((projectionSize.y * camera.getZoom()) + (DefaultValues.GRID_HEIGHT * 5));
        int width = (int) ((projectionSize.x * camera.getZoom()) + (DefaultValues.GRID_WIDTH * 5));

        int maxLines = Math.max(numHorLines, numVertLines);
        for (int i = 0; i < maxLines; ++i)
        {
            float x = firstX + (DefaultValues.GRID_WIDTH * i);
            float y = firstY + (DefaultValues.GRID_HEIGHT * i);

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
