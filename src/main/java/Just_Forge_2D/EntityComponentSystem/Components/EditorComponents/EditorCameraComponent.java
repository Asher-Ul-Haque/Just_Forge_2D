package Just_Forge_2D.EntityComponentSystem.Components.EditorComponents;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.InputSystem.MouseButtons;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Configurations;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_0;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

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
    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        this.editorCamera.adjustProjection();
        if (Mouse.isMouseButtonDown(MouseButtons.RIGHT) && dragDebounce > 0)
        {
            this.clickOrigin = new Vector2f(EditorSystemManager.getWorldX(), EditorSystemManager.getWorldY());
            dragDebounce -= DELTA_TIME;
            return;
        }
        else if (Mouse.isMouseButtonDown(MouseButtons.RIGHT))
        {
            Vector2f mousePos = new Vector2f(EditorSystemManager.getWorldX(), EditorSystemManager.getWorldY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            this.editorCamera.position.sub(delta.mul(DELTA_TIME).mul(this.dragSensitivity));
            this.clickOrigin.lerp(mousePos, DELTA_TIME);
        }
        if (dragDebounce <= 0.0f && !Mouse.isMouseButtonDown(MouseButtons.RIGHT))
        {
            dragDebounce = Configurations.DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE;
        }

        if (Mouse.getScrollY() != 0.0f)
        {
            float addValue = (float)Math.pow(Math.abs(Mouse.getScrollY() * this.scrollSensitivity), 1 / this.editorCamera.getZoom());
            addValue *= -Math.signum(Mouse.getScrollY());
            editorCamera.addZoom(addValue);

        }

        if (Keyboard.isKeyPressed(Keys.NUM_0))
        {
            reset = true;
        }
        if (reset)
        {
            editorCamera.position.lerp(new Vector2f(), lerpTime);
            editorCamera.setZoom(editorCamera.getZoom() + (Configurations.DEFAULT_CAMERA_ZOOM - editorCamera.getZoom()) * lerpTime);
            this.lerpTime += 0.1f * DELTA_TIME;
            if (Math.abs(editorCamera.position.x) <= 1f && Math.abs(editorCamera.position.y) <= 1f)
            {
                editorCamera.position.set(0f, 0f);
                reset = false;
                this.lerpTime = Configurations.DEFAULT_EDITOR_CAMERA_LERP_TIME;
                this.editorCamera.setZoom(Configurations.DEFAULT_CAMERA_ZOOM);
            }
        }
    }
}