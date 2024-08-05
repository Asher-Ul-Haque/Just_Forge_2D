package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.NonPickableComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.Scene.Scene;
import Just_Forge_2D.Core.Window;
import Just_Forge_2D.Utils.justForgeLogger;
import imgui.ImGui;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;


// - - - Properties Window
public class PropertiesWindow
{
    // - - - private variables
    private GameObject activeGameObject = null;
    private final ObjectSelector selector;
    private float debounce = 0.2f;


    // - - - Functions - - -

    // - - - Constructor
    public PropertiesWindow(ObjectSelector SELECTOR)
    {
        this.selector = SELECTOR;
    }

    // - - - usage
    public void update(float DELTA_TIME, Scene CURRENT_SCENE)
    {
        debounce -= DELTA_TIME;
        if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0)
        {
            int x = (int)Mouse.getScreenX();
            int y = (int)Mouse.getScreenY();
            int gameObjectID = this.selector.readPixel(x, y);
            GameObject picked = Window.getCurrentScene().getGameObject(gameObjectID);
            if (picked != null && picked.getCompoent(NonPickableComponent.class) == null)
            {
                justForgeLogger.FORGE_LOG_DEBUG("Currently active object: " + picked.toString());
                this.activeGameObject = picked;
            }
            else if (picked == null && !Mouse.isDragging())
            {
                justForgeLogger.FORGE_LOG_DEBUG("Currently active object: Null");
                activeGameObject = null;
            }
            this.debounce = 0.2f;
        }
    }

    // - - - getter
    public GameObject getActiveGameObject()
    {
        return this.activeGameObject;
    }

    // - - - ignore this, this is for the editor
    public void editorGUI()
    {
        if (activeGameObject != null)
        {
            ImGui.begin("Properties");
            activeGameObject.editorGUI();
            ImGui.end();
        }
    }

}
