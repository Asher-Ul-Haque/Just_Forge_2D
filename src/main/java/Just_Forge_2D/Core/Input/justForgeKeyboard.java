package Just_Forge_2D.Core.Input;

import Just_Forge_2D.Utils.justForgeLogger;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


// - - - Keyboard input class
public class justForgeKeyboard
{

    // - - - Private variables - - -

    // - - - Singleton
    private static justForgeKeyboard keyboard;

    // - - - Key States
    private boolean isKeyPressed[] = new boolean[350];


    // - - - Functions - - -

    // - - - Singleton useless constructor
    private justForgeKeyboard() {}

    // - - - Initialize keyboard input system
    public static void init()
    {
        justForgeLogger.FORGE_LOG_INFO("Keyboard Input System Online");
        if (justForgeKeyboard.keyboard == null)
        {
            justForgeKeyboard.keyboard = new justForgeKeyboard();
        }
    }

    // - - - Callback for GLFW
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

    // - - - Get key state
    public static boolean isKeyPressed(int KEYCODE)
    {
        assert justForgeKeyboard.keyboard != null;
        return justForgeKeyboard.keyboard.isKeyPressed[KEYCODE];
    }

}
