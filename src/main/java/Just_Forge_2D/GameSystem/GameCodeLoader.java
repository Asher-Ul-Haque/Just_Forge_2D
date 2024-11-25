package Just_Forge_2D.GameSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import static Just_Forge_2D.GameSystem.GameManager.game;
import static Just_Forge_2D.GameSystem.GameManager.success;
import static java.nio.file.StandardWatchEventKinds.*;

public class GameCodeLoader
{
    private static WatchService eyeBall;
    private static long lastModifiedTime = 0;
    private static final long DEBOUNCE_DELAY = 5000;  // Debounce delay for recompilation (5 seconds)
    private static volatile boolean watch = false;  // Flag to control the watcher thread
    private static Thread watcherThread;

    public static void init()
    {
        if (success)
        {
            try
            {
                game.init();
            }
            catch (Throwable e)
            {
                handleUserCodeException(e);
            }
        }
    }

    private static void handleUserCodeException(Throwable e)
    {
        StackTraceElement[] elements = e.getStackTrace();
        TinyFileDialogs.tinyfd_notifyPopup(e.getClass().getSimpleName(), "User Code : " + elements[0].getFileName() + " : " + elements[0].getLineNumber(), "error");
        Logger.FORGE_LOG_FATAL(e.getMessage());
    }

    public static void loop(float DELTA_TIME)
    {
        if (success)
        {
            try
            {
                game.update(DELTA_TIME);
            }
            catch (Throwable e)
            {
                handleUserCodeException(e);
            }
        }
    }

    public static void terminate()
    {
        if (success)
        {
            try
            {
                game.end();
            }
            catch (Throwable e)
            {
                handleUserCodeException(e);
            }
        }
        closeEye();
    }

    private static void registerAllDirectories(Path start) throws IOException
    {
        Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException
            {
                dir.register(eyeBall, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                return FileVisitResult.CONTINUE;
            }
        });
    }
    public static void openEye()
    {
        if (!watch && !EditorSystemManager.isRelease)
        {
            try
            {
                eyeBall = FileSystems.getDefault().newWatchService();
                registerAllDirectories(Paths.get(EditorSystemManager.projectDir + "/src/main/java"));
                registerAllDirectories(Paths.get(EditorSystemManager.projectDir + "/Assets"));
                watch = true;

                // - - - Start the watcher thread - -

                // - - -If the watcher thread is already running, we don't start a new one
                if (watcherThread != null && watcherThread.isAlive())
                {
                    return;
                }

                watcherThread = new Thread(() ->
                {
                    while (watch)
                    {
                        WatchKey key;
                        try
                        {
                            // Wait for a file system event
                            key = eyeBall.take();
                        }
                        catch (InterruptedException e)
                        {
                            Logger.FORGE_LOG_ERROR("Eye Balling interrupted.");
                            return;
                        }

                        // Retrieve all events for the key
                        List<WatchEvent<?>> events = key.pollEvents();
                        if (!events.isEmpty())
                        {
                            long currentTime = System.currentTimeMillis();
                            if (currentTime - lastModifiedTime > DEBOUNCE_DELAY)
                            {
                                lastModifiedTime = currentTime;
                                try
                                {
                                    Thread.sleep(10);
                                }
                                catch (Exception e)
                                {
                                    Logger.FORGE_LOG_WARNING("The Eye was woken up from its slumber before it could wait for the program to sleep");
                                    Logger.FORGE_LOG_ERROR(e.getMessage());
                                }

                                // Check if changes were in src/main/java or Assets
                                Path changedDir = (Path) key.watchable();
                                String changedDirPath = changedDir.toString();
                                Logger.FORGE_LOG_INFO("Change Detected in " + changedDirPath);

                                if (changedDirPath.contains("src/main/java"))
                                {
                                    GameManager.buildUserCode();
                                }
                                else if (changedDirPath.contains("Assets"))
                                {
                                    GameWindow.getCurrentScene().getRenderer().reload();
                                }
                            }
                        }

                        // Reset the key and stop watching if it becomes invalid
                        boolean valid = key.reset();
                        if (!valid)
                        {
                            Logger.FORGE_LOG_ERROR("Directory no longer accessible, stopping watcher.");
                            break;
                        }
                    }
                });

                // - - - Start the thread
                watcherThread.start();
            }
            catch (IOException e)
            {
                Logger.FORGE_LOG_ERROR(e.getMessage());
                Logger.FORGE_LOG_ERROR("Failed to Eye Ball");
            }
        }
    }

    public static void closeEye()
    {
        watch = false;

        // - - - Interrupt the watcher thread to stop it
        if (watcherThread != null)
        {
            watcherThread.interrupt();
            try
            {
                watcherThread.join();  // - - - Wait for the thread to finish
            }
            catch (InterruptedException e)
            {
                Logger.FORGE_LOG_ERROR("Failed to stop watcher thread cleanly.");
            }
        }

        // - - - Close the WatchService
        if (eyeBall != null)
        {
            try
            {
                eyeBall.close();
            }
            catch (IOException e)
            {
                Logger.FORGE_LOG_ERROR("Failed to close WatchService: " + e.getMessage());
            }
        }
    }
}
