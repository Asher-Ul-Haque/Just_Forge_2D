package Just_Forge_2D.InputSystem;

import Utils.Logger;

import static org.lwjgl.glfw.GLFW.*;


// - - - Keyboard input class
public class Keyboard
{

    // - - - Private variables - - -

    // - - - Singleton
    private static Keyboard keyboard;

    // - - - Key States
    private final int totalKeys = GLFW_KEY_LAST + 1;
    private final boolean[] isKeyPressed = new boolean[totalKeys];
    private final boolean[] isKeyBeginPress = new boolean[totalKeys];


    // - - - Functions - - -

    // - - - Singleton useless constructor
    private Keyboard() {}

    // - - - Initialize keyboard input system
    public static Keyboard get()
    {
        if (Keyboard.keyboard == null)
        {
            Keyboard.keyboard = new Keyboard();
            Logger.FORGE_LOG_INFO("Keyboard Input System Online");
        }
        return Keyboard.keyboard;
    }

    // - - - Callback for GLFW
    public static void keyCallback(long WINDOW, int KEY, int SCANCODE, int ACTION, int MODIFIER)
    {
        switch (ACTION)
        {
            case GLFW_PRESS:
                if (!get().isKeyPressed[KEY])
                {
                    get().isKeyBeginPress[KEY] = true;
                }
                get().isKeyPressed[KEY] = true;
                break;

            case GLFW_RELEASE:
                get().isKeyPressed[KEY] = false;
                get().isKeyBeginPress[KEY] = false; // Reset after release
                break;

            default:
                break;
        }
    }

    // - - - Get key state
    public static boolean isKeyPressed(Keys KEY)
    {
        return get().isKeyPressed[KEY.keyCode];
    }

    public static boolean isKeyBeginPress(Keys KEY)
    {
        if (get().isKeyBeginPress[KEY.keyCode])
        {
            get().isKeyBeginPress[KEY.keyCode] = false; // Reset for next check
            return true;
        }
        return false;
    }

    // - - - ANY PRESSED
    public static boolean isAnyKeyPressed()
    {
        for (boolean b : keyboard.isKeyPressed)
        {
            if (b) return b;
        }
        return false;
    }

    public static void reset()
    {
        for (boolean b : keyboard.isKeyPressed)
        {
            b = false;
        }
        for (boolean b : keyboard.isKeyBeginPress)
        {
            b = false;
        }
    }
}