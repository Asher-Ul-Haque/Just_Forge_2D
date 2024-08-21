package Just_Forge_2D.EditorSystem.EditorComponents;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.DefaultValues;
import org.joml.Vector2f;

public class EditorCameraComponent extends Component
{
    // - - - private variables - - -

    // - - - the camera
    private final Camera editorCamera;

    // - - - sensitivity
    private float dragDebounce = DefaultValues.DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE;
    private float lerpTime = DefaultValues.DEFAULT_EDITOR_CAMERA_LERP_TIME;
    private float dragSensitivity = DefaultValues.DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVTY;
    private float scrollSensitivity = DefaultValues.DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY;

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