package Just_Forge_2D.InputSystem;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButtons
{
    LEFT(GLFW_MOUSE_BUTTON_LEFT),
    RIGHT(GLFW_MOUSE_BUTTON_RIGHT),
    MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE),
    BUTTON_4(GLFW_MOUSE_BUTTON_4),
    BUTTON_5(GLFW_MOUSE_BUTTON_5),
    BUTTON_6(GLFW_MOUSE_BUTTON_6),
    BUTTON_7(GLFW_MOUSE_BUTTON_7),
    BUTTON_8(GLFW_MOUSE_BUTTON_8);

    final int buttonCode;

    // - - - Constructor to assign the GLFW mouse button code to the enum constant
    MouseButtons(int glfwButton)
    {
        this.buttonCode = glfwButton;
    }
}
