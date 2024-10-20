package Just_Forge_2D.EditorSystem.EditorComponents;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.InputSystem.Mouse;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Settings;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class EditorCameraComponent extends Component
{
    // - - - private variables - - -

    // - - - the camera
    private final Camera editorCamera;

    // - - - sensitivity
    public static float dragDebounce = Settings.DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE;
    public static float lerpTime = Settings.DEFAULT_EDITOR_CAMERA_LERP_TIME;
    public static float dragSensitivity = Settings.DEFAULT_EDITOR_CAMERA_DRAG_SENSITIVITY;
    public static float scrollSensitivity = Settings.DEFAULT_EDITOR_CAMERA_SCROLL_SENSITIVITY;

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
        if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT) && dragDebounce > 0)
        {
            this.clickOrigin = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());
            dragDebounce -= DELTA_TIME;
            return;
        }
        else if (Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT))
        {
            Vector2f mousePos = new Vector2f(Mouse.getWorldX(), Mouse.getWorldY());
            Vector2f delta = new Vector2f(mousePos).sub(this.clickOrigin);
            this.editorCamera.getPosition().sub(delta.mul(DELTA_TIME).mul(dragSensitivity));
            this.clickOrigin.lerp(mousePos, DELTA_TIME);
        }
        if (dragDebounce <= 0.0f && !Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT))
        {
            dragDebounce = Settings.DEFAULT_EDITOR_CAMERA_DRAG_DEBOUNCE;
        }

        if (Mouse.getScrollY() != 0.0f && this.editorCamera.getZoom() != 0)
        {
            float addValue = (float)Math.pow(Math.abs(Mouse.getScrollY() * scrollSensitivity), 1 / this.editorCamera.getZoom());
            addValue *= -Math.signum(Mouse.getScrollY());
            editorCamera.addZoom(addValue);
            if (addValue < 0) editorCamera.getPosition().lerp(new Vector2f(Mouse.getWorldX(), Mouse.getWorldY()).mul(this.editorCamera.getZoom()), scrollSensitivity);
        }

        if (Keyboard.isKeyPressed(Keys.NUM_0))
        {
            reset = true;
        }
        if (reset)
        {
            this.editorCamera.getPosition().lerp(new Vector2f(), lerpTime);
            editorCamera.setZoom(editorCamera.getZoom() + (Settings.DEFAULT_CAMERA_ZOOM - editorCamera.getZoom()) * lerpTime);
            lerpTime += 0.1f * DELTA_TIME;
            if ((Math.abs(this.editorCamera.getPosition().x) <= 1f && Math.abs(this.editorCamera.getPosition().y) <= 1f && Math.abs(this.editorCamera.getZoom() - 1.0f) <0.5f))
            {
                editorCamera.setPositionAbsolute(new Vector2f(0f, 0f));
                reset = false;
                lerpTime = Settings.DEFAULT_EDITOR_CAMERA_LERP_TIME;
                this.editorCamera.setZoom(Settings.DEFAULT_CAMERA_ZOOM);
            }
        }
    }
}