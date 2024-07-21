package Just_Forge_2D;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class justForgeKeyboard
{
    private static justForgeKeyboard keyboard;
    private boolean isKeyPressed[] = new boolean[350];

    private justForgeKeyboard()
    {

    }

    protected static void init()
    {
        if (justForgeKeyboard.keyboard == null)
        {
            justForgeKeyboard.keyboard = new justForgeKeyboard();
        }
    }

    public static void keyCallback(long WINDOW, int KEY, int SCANCODE, int ACTION, int MODIFIER)
    {
        assert justForgeKeyboard.keyboard != null;
        switch (ACTION)
        {
            case GLFW_PRESS:
                justForgeKeyboard.keyboard.isKeyPressed[KEY] = true;
                break;

            case GLFW_RELEASE:
                justForgeKeyboard.keyboard.isKeyPressed[KEY] = false;
                break;

            default:
                break;
        }
    }

    public static boolean isKeyPressed(int KEYCODE)
    {
        assert justForgeKeyboard.keyboard != null;
        return justForgeKeyboard.keyboard.isKeyPressed[KEYCODE];
    }

}
