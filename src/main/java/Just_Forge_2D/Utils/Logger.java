package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.EditorSystemManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;

public class Logger
{
    private static final String ANSI_RESET = "\u001B[0m";

    // - - - Colors
    private static final String ANSI_RED_BG = "\u001B[41m"; // Red background
    private static final String ANSI_RED = "\u001B[31m"; // Red
    private static final String ANSI_PASTEL_RED = "\u001B[38;5;216m"; // Light Pink
    private static final String ANSI_BLUE = "\u001B[34m"; // Blue
    private static final String ANSI_GREEN = "\u001B[32m"; // Green
    private static final String ANSI_PURPLE = "\u001B[35m"; // PURPLE (light blue)

    // - - -Log file path
    private static String stamp = LocalDate.now() + "_" + LocalTime.now().getHour() + ":" + LocalTime.now().getMinute();
    private static final Path LOG_FILE_PATH = Paths.get("logs", stamp + ".justForgeLog");

    static
    {
        if (!EditorSystemManager.isRelease)
        {
            // - - - Ensure the log file and directory are created at the start
            try
            {
                // - - - Create directories if they do not exist
                Files.createDirectories(LOG_FILE_PATH.getParent());

                // - - - Create the log file if it does not exist
                if (!Files.exists(LOG_FILE_PATH)) Files.createFile(LOG_FILE_PATH);

                // - - - Initialize the log file with a session start message
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH.toString(), true)))
                {
                    writer.write(stamp);
                    writer.write("\n---- Log Session Started ----\n");
                }

                // - - - Redirect System.out and System.err to the custom logger
                PrintStream logStream = new PrintStream(new LoggerOutputStream());
                System.setOut(logStream);
                System.setErr(logStream);

            }
            catch (IOException e)
            {
                System.out.println(ANSI_RED + "[ERROR]: Failed to initialize log file: " + e.getMessage() + ANSI_RESET);
            }
        }
    }

    // - - - Logging functions - - -
    public static void FORGE_LOG_FATAL(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[FATAL]", ARGS);
        System.out.println(ANSI_RESET + ANSI_RED_BG + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_ERROR(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[ERROR]", ARGS);
        System.out.println(ANSI_RED + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_WARNING(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[WARNING]", ARGS);
        System.out.println(ANSI_PASTEL_RED + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_DEBUG(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[DEBUG]", ARGS);
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_TRACE(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[TRACE]", ARGS);
        System.out.println(ANSI_PURPLE + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_INFO(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[INFO]", ARGS);
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
        writeToFile(message);
    }

    private static String formatMessage(String level, Object... ARGS)
    {
        StringBuilder message = new StringBuilder();
        message.append(level).append(": \t");

        for (Object o : ARGS)
        {
            try
            {
                message.append(o.toString());
            }
            catch (Exception e)
            {
                message.append("[Invalid Object]");
            }
        }

        return message.toString();
    }

    private static void writeToFile(String message)
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH.toString(), true)))
        {
            writer.write(message + "\n");
        }
        catch (IOException e)
        {
            System.out.println(ANSI_RED + "[ERROR]: Failed to write to log file: " + e.getMessage() + ANSI_RESET);
        }
    }

    private static class LoggerOutputStream extends OutputStream
    {
        private final StringBuilder buffer = new StringBuilder();

        @Override
        public void write(int b) throws IOException
        {
            if (b == '\n')
            {
                String line = buffer.toString();
                writeToFile(line);
                buffer.setLength(0);
            }
            else
            {
                buffer.append((char) b);
            }
        }
    }
}