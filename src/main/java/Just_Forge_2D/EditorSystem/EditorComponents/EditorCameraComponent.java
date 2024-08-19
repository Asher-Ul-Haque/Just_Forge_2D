package Just_Forge_2D.EditorSystem.EditorComponents;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.InputSystem.MouseButtons;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Configurations;
import org.joml.Vector2f;

public class EditorCameraComponent extends Component
{
    // - - - private variables - - -

    // - - - the camera
    private final Camera editorCamera;

    // - - - sensitivity
    private float dragDebounce = Configurations.DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE;
    private float lerpTime = Configurations.DEFAULT_EDITOR_CAMERA_LERP_TIME;
    private float dragSensitivity = Configurations.DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVTY;
    private float scrollSensitivity = Configurations.DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY;

    // - - - everything else
    private Vector2f clickOrigin = new Vector2f();
    private boolean reset = true;


    // - - - Functions - - -

    // - - - constructor
    public EditorCameraComponent(Camera EDITOR_CAMERA)
    {
        this.editorCamera = EDITOR_CAMERA;
    }

    // - - - usage
}