package Just_Forge_2D.Utils;

import Just_Forge_2D.EditorSystem.Forge;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Logger
{
    // - - -Log file path
    private static final String stamp = LocalDate.now() + "_" + LocalTime.now().getHour() + "_" + LocalTime.now().getMinute()  + "_" + LocalTime.now().getSecond();
    public static Path LOG_FILE_PATH = Paths.get("logs", "latest.justForgeLog");

    private static final int maxWriteBuffer = 15;
    private static final int maxReadBuffer = 1024;
    private static final List<String> writeBuffer = new ArrayList<>(maxWriteBuffer);
    private static final Deque<String> readBuffer = new ArrayDeque<>(maxReadBuffer);

    private static void addToReadBuffer(String MESSAGE)
    {
        if (Forge.isRelease) System.out.println(MESSAGE);
        else
        {
            if (readBuffer.size() >= maxReadBuffer)
            {
                readBuffer.pollFirst(); // Remove the oldest message if the buffer is full
            }
            readBuffer.addLast(MESSAGE);
        }
    }

    static
    {
        if (!Forge.isRelease)
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
                PrintStream logErrorStream = new PrintStream(new LoggerOutputStream(true)); // true = for System.err
                PrintStream logOutputStream = new PrintStream(new LoggerOutputStream(false));
                System.setErr(logErrorStream);
                if (!Forge.isRelease) System.setOut(logOutputStream);
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
        String message = formatMessage("[FATAL]  ", ARGS);
        addToReadBuffer(message);
        writeToFile(message);
    }

    public static void FORGE_LOG_ERROR(Object... ARGS)
    {
        String message = formatMessage("[ERROR]  ", ARGS);
        addToReadBuffer(message);
        writeToFile(message);
    }

    public static void FORGE_LOG_WARNING(Object... ARGS)
    {
        if (Forge.isRelease) return;
        String message = formatMessage("[WARNING]", ARGS);
        addToReadBuffer(message);
        writeToFile(message);
    }

    public static void FORGE_LOG_DEBUG(Object... ARGS)
    {
        if (Forge.isRelease) return;
        String message = formatMessage("[DEBUG]  ", ARGS);
        addToReadBuffer(message);
        writeToFile(message);
    }

    public static void FORGE_LOG_TRACE(Object... ARGS)
    {
        if (Forge.isRelease) return;
        String message = formatMessage("[TRACE]  ", ARGS);
        addToReadBuffer(message);
        writeToFile(message);
    }

    public static void FORGE_LOG_INFO(Object... ARGS)
    {
        if (Forge.isRelease) return;
        String message = formatMessage("[INFO]   ", ARGS);
        addToReadBuffer(message);
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
                if (isErrStream) formatMessage("[FATAL]  ", line);
                writeToFile(line);
                addToReadBuffer(line);
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
        if (Forge.isRelease) return;
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
            System.out.println("[ERROR]: Failed to write to log file: " + e.getMessage());
        }
    }

    public static String[] getReadBuffer()
    {
        return readBuffer.toArray(new String[0]);
    }
}