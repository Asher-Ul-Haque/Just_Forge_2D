package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.EditorSystemManager;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    private static final String stamp = LocalDate.now() + "_" + LocalTime.now().getHour() + "_" + LocalTime.now().getMinute()  + "_" + LocalTime.now().getSecond();
    private static Path LOG_FILE_PATH = Paths.get("logs", "latest.justForgeLog");

    private static final int maxWriteBuffer = 15;
    private static final int maxReadBuffer = 1024;
    private static final List<String> writeBuffer = new ArrayList<>(maxWriteBuffer);
    private static final String[] readBuffer = new String[maxReadBuffer];
    private static int readBufferIndex = 0;

    private static void addToReadBuffer(String MESSAGE)
    {
        readBuffer[readBufferIndex] = MESSAGE;
        readBufferIndex = (readBufferIndex + 1) % maxReadBuffer;
    }

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
                else
                {
                    BufferedReader reader = Files.newBufferedReader(LOG_FILE_PATH);
                    String first = reader.readLine();

                    if (first != null & !first.trim().isEmpty())
                    {
                        String cleaned = first.trim();
                        Path oldLog = Paths.get("logs", cleaned + ".justForgeLog");
                        try
                        {
                            Files.move(LOG_FILE_PATH, oldLog);
                        }
                        catch (FileAlreadyExistsException e)
                        {
                            Logger.FORGE_LOG_ERROR("Bro let the engine breathe");
                            Files.delete(oldLog);
                            Files.move(LOG_FILE_PATH, oldLog);
                        }
                        Files.createFile(LOG_FILE_PATH);
                    }
                    else
                    {
                        Files.delete(LOG_FILE_PATH);
                        LOG_FILE_PATH = Paths.get("logs", stamp + ".justForgeLog");
                    }
                }

                // - - - Initialize the log file with a session start message
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH.toString(), true)))
                {
                    writer.write(stamp);
                    writer.write("\n---- Log Session Started ----\n");
                }

                // - - - Redirect System.err to the custom logger (System.out can stay normal)
                PrintStream logStream = new PrintStream(new LoggerOutputStream(true)); // true = for System.err
                if (!EditorSystemManager.isRelease) System.setErr(logStream);
            }

            catch (IOException e)
            {
                Logger.FORGE_LOG_FATAL("Failed to initialize log file: " + e.getMessage());
                Logger.FORGE_LOG_FATAL(e.getCause());
            }
        }
    }

    // - - - Logging functions - - -
    public static void FORGE_LOG_FATAL(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[FATAL]  ", ARGS);
        addToReadBuffer(message);
        System.out.println(ANSI_RESET + ANSI_RED_BG + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_ERROR(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[ERROR]  ", ARGS);
        addToReadBuffer(message);
        System.out.println(ANSI_RED + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_WARNING(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[WARNING]", ARGS);
        addToReadBuffer(message);
        System.out.println(ANSI_PASTEL_RED + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_DEBUG(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[DEBUG]  ", ARGS);
        addToReadBuffer(message);
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_TRACE(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[TRACE]  ", ARGS);
        addToReadBuffer(message);
        System.out.println(ANSI_PURPLE + message + ANSI_RESET);
        writeToFile(message);
    }

    public static void FORGE_LOG_INFO(Object... ARGS)
    {
        if (EditorSystemManager.isRelease) return;
        String message = formatMessage("[INFO]   ", ARGS);
        addToReadBuffer(message);
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
        writeToFile(message);
    }

    private static String formatMessage(String level, Object... ARGS)
    {
        StringBuilder message = new StringBuilder();
        message.append(level).append("\t");

        for (Object o : ARGS)
        {
            try
            {
                message.append(o.toString());
            }
            catch (Exception e)
            {
                message.append("[Invalid Object]").append(e.getMessage());
            }
        }

        return message.toString();
    }

    private static void writeToFile(String message)
    {
        writeBuffer.add(message);
        if (writeBuffer.size() >= 10) // Adjust buffer threshold if needed
        {
            flushToFile();
        }
    }

    private static class LoggerOutputStream extends OutputStream
    {
        private final StringBuilder buffer = new StringBuilder();
        private final boolean isErrStream;

        public LoggerOutputStream(boolean isErrStream)
        {
            this.isErrStream = isErrStream;
        }

        @Override
        public void write(int b) throws IOException
        {
            if (b == '\n')
            {
                String line = buffer.toString();
                writeToFile(line);
                buffer.setLength(0);

                // - - - If it's System.err, flush the buffer immediately
                if (isErrStream)
                {
                    flushToFile();
                }
            }
            else
            {
                buffer.append((char) b);
            }
        }
    }

    public static void flushToFile()
    {
        if (EditorSystemManager.isRelease) return;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH.toString(), true)))
        {
            for (String message : writeBuffer)
            {
                writer.write(message + "\n");
            }
            writeBuffer.clear();
        }
        catch (IOException e)
        {
            System.out.println(ANSI_RED + "[ERROR]: Failed to write to log file: " + e.getMessage() + ANSI_RESET);
        }
    }

    public static String[] getReadBuffer()
    {
        return readBuffer;
    }
}
