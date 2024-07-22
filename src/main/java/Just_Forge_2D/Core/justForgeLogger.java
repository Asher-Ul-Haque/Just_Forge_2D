package Just_Forge_2D.Core;

public class justForgeLogger
{
    private static final String ANSI_RESET = "\u001B[0m";

    // - - - Colors
    private static final String ANSI_RED_BG = "\u001B[41m";      // Red background
    private static final String ANSI_RED = "\u001B[31m";        // Red
    private static final String ANSI_PASTEL_RED = "\u001B[38;5;216m"; // Light Pink
    private static final String ANSI_BLUE = "\u001B[34m";        // Blue
    private static final String ANSI_GREEN = "\u001B[32m";       // Green
    private static final String ANSI_CYAN = "\u001B[36m";        // Cyan (light blue)


    // - - - Logging functions - - -
    public static void FORGE_LOG_FATAL(Object... ARGS)
    {
        System.out.println(ANSI_RESET + ANSI_RED_BG + "[FATAL]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_ERROR(Object... ARGS)
    {
        System.out.println(ANSI_RED + "[ERROR]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_WARNING(Object... ARGS)
    {
        System.out.println(ANSI_PASTEL_RED + "[WARNING]: \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_DEBUG(Object... ARGS)
    {
        System.out.println(ANSI_BLUE + "[DEBUG]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_TRACE(Object... ARGS)
    {
        System.out.println(ANSI_CYAN + "[TRACE]:   \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    public static void FORGE_LOG_INFO(Object... ARGS)
    {
        System.out.println(ANSI_GREEN + "[INFO]:    \t" + formatMessage(ARGS) + ANSI_RESET);
    }

    private static String formatMessage(Object... ARGS)
    {
        String message = "";
        for (Object o : ARGS)
        {
            try
            {
                message += o.toString();
            }
            catch (Exception e)
            {
                FORGE_LOG_WARNING("Tried to use Just_Forge_2D.Core.logger to log nonesense that cant be converted to string");
            }
        }
        return message;
    }
}
