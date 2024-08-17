package Just_Forge_2D;

import Just_Forge_2D.RenderingSystems.Renderer;
import Just_Forge_2D.SceneSystem.SceneManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.Window;

import java.util.HashMap;
import java.util.Locale;

public class Forge
{
    private static final HashMap<Window, Renderer> windowRendererHashMap = new HashMap<>();
    private static final SceneManager sceneManager = new SceneManager();
    public static void assignRenderer(Window WINDOW, Renderer RENDERER)
    {
        Logger.FORGE_LOG_INFO("Linking " + WINDOW.getTitle() + " to Renderer " + RENDERER);
        windowRendererHashMap.put(WINDOW, RENDERER);
    }

    public static Renderer getRenderer(Window WINDOW)
    {
        Renderer renderer = windowRendererHashMap.get(WINDOW);
        if (renderer == null)
        {
            Logger.FORGE_LOG_WARNING("No renderer has been assigned to : " + WINDOW);
            Renderer newRenderer = new Renderer(WINDOW.getTitle());
            assignRenderer(WINDOW, newRenderer);
            return newRenderer;
        }
        return renderer;
    }

    public static void update(float DELTA_TIME){}
}
