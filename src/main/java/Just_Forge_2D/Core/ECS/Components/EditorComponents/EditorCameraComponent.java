package Just_Forge_2D.Core.ECS.Components.EditorComponents;

import Just_Forge_2D.Core.Camera;
import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.Input.Keyboard;
import Just_Forge_2D.Core.Input.Mouse;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class EditorCameraComponent extends Component
{
    // - - - private variables - - -

    // - - - the camera
    private final Camera editorCamera;

    // - - - sensitivity
    private float dragDebounce = 0.32f;
    private float lerpTime = 0f;
    private float dragSensitivity = 10.0f;
    private float scrollSensitivity = 0.1f;

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
            this.editorCamera.position.sub(delta.mul(DELTA_TIME).mul(this.dragSensitivity));
            this.clickOrigin.lerp(mousePos, DELTA_TIME);
        }
        if (dragDebounce <= 0.0f && !Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_RIGHT))
        {
            dragDebounce = 0.32f;
        }

        if (Mouse.getScrollY() != 0.0f)
        {
            float addValue = (float)Math.pow(Math.abs(Mouse.getScrollY() * this.scrollSensitivity), 1 / this.editorCamera.getZoom());
            addValue *= -Math.signum(Mouse.getScrollY());
            editorCamera.addZoom(addValue);

        }

        if (Keyboard.isKeyPressed(GLFW_KEY_0))
        {
            reset = true;
        }
        if (reset)
        {
            editorCamera.position.lerp(new Vector2f(), lerpTime);
            editorCamera.setZoom(editorCamera.getZoom() + (0.5f - editorCamera.getZoom()) * lerpTime);
            this.lerpTime += 0.1f * DELTA_TIME;
            if (Math.abs(editorCamera.position.x) <= 5.0f && Math.abs(editorCamera.position.y) <= 5.0f)
            {
                editorCamera.position.set(0f, 0f);
                reset = false;
                this.lerpTime = 0f;
                this.editorCamera.setZoom(0.5f);
            }
        }
    }
}