package Just_Forge_2D.Core.Input;

import Just_Forge_2D.Utils.justForgeLogger;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


// - - - Keyboard input class
public class Keyboard
{

    // - - - Private variables - - -

    // - - - Singleton
    private static Keyboard keyboard;

    // - - - Key States
    private final int totalKeys = 350;
    private final boolean[] isKeyPressed = new boolean[totalKeys];


    // - - - Functions - - -

    // - - - Singleton useless constructor
    private Keyboard() {}

    // - - - Initialize keyboard input system
    public static Keyboard get()
    {
        if (Keyboard.keyboard == null)
        {
            Keyboard.keyboard = new Keyboard();
            justForgeLogger.FORGE_LOG_INFO("Keyboard Input System Online");
        }
        return Keyboard.keyboard;
    }

    // - - - Callback for GLFW
    public static void keyCallback(long WINDOW, int KEY, int SCANCODE, int ACTION, int MODIFIER)
    {
        switch (ACTION)
        {
            case GLFW_PRESS:
                get().isKeyPressed[KEY] = true;
                break;

            case GLFW_RELEASE:
                get().isKeyPressed[KEY] = false;
                break;

            default:
                break;
        }
    }

    // - - - Get key state
    public static boolean isKeyPressed(int KEYCODE)
    {
        return get().isKeyPressed[KEYCODE];
    }
}