package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class Logger
{
    private static final String ANSI_RESET = "\u001B[0m";

    // - - - Colors
    private static final String ANSI_RED_BG = "\u001B[41m";      // Red background
    private static final String ANSI_RED = "\u001B[31m";        // Red
    private static final String ANSI_PASTEL_RED = "\u001B[38;5;216m"; // Light Pink
    private static final String ANSI_BLUE = "\u001B[34m";        // Blue
    private static final String ANSI_GREEN = "\u001B[32m";       // Green
    private static final String ANSI_PURPLE = "\u001B[35m";        // PURPLE (light blue)


    // - - - Logging functions - - -
    public static void FORGE_LOG_FATAL(Object... ARGS)
    {
        if (!EditorSystemManager.isRelease) System.out.println(ANSI_RESET + ANSI_RED_BG + "[FATAL]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_ERROR(Object... ARGS)
    {
        if (!EditorSystemManager.isRelease)
        {
            System.out.println(ANSI_RED + "[ERROR]:   \t" + formatMessage(ARGS) + ANSI_RESET);
//            TinyFileDialogs.tinyfd_notifyPopup("Error", formatMessage(ARGS), "error");
        }
    }

    public static void FORGE_LOG_WARNING(Object... ARGS)
    {
        if (!EditorSystemManager.isRelease) System.out.println(ANSI_PASTEL_RED + "[WARNING]: \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_DEBUG(Object... ARGS)
    {
        if (!EditorSystemManager.isRelease) System.out.println(ANSI_BLUE + "[DEBUG]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_TRACE(Object... ARGS)
    {
        if (!EditorSystemManager.isRelease) System.out.println(ANSI_PURPLE + "[TRACE]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_INFO(Object... ARGS)
    {
        if (!EditorSystemManager.isRelease) System.out.println(ANSI_GREEN + "[INFO]:    \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    private static String formatMessage(Object... ARGS)
    {
        StringBuilder message = new StringBuilder();
        for (Object o : ARGS)
        {
            try
            {
                message.append(o.toString());
            }
            catch (Exception e)
            {
                FORGE_LOG_WARNING("Tried to use Logger to log nonsense that cant be converted to string");
            }
        }
        return message.toString();
    }
}
