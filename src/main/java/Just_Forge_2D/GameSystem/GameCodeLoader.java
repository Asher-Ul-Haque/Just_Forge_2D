package Just_Forge_2D.GameSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.Utils.Logger;

import java.io.IOException;
import java.nio.file.*;
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
            game.init();
        }
        if (!watch && !EditorSystemManager.isRelease)
        {
            try
            {
                eyeBall = FileSystems.getDefault().newWatchService();
                Path codeDir = Paths.get(EditorSystemManager.projectDir + "/src/main/java");
                codeDir.register(eyeBall, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
                watch = true;

                // - - - Start the watcher thread
                startEyeBalling();
            }
            catch (IOException e)
            {
                Logger.FORGE_LOG_ERROR(e.getMessage());
                Logger.FORGE_LOG_ERROR("Failed to Eye Ball Game Code");
            }
        }
    }

    public static void loop(float DELTA_TIME)
    {
        if (success)
        {
            game.update(DELTA_TIME);
        }
    }

    public static void terminate()
    {
        if (success)
        {
            game.end();
        }
        closeEye();
    }

    private static void startEyeBalling()
    {
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

                List<WatchEvent<?>> events = key.pollEvents();
                if (events.isEmpty()) return;
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastModifiedTime > DEBOUNCE_DELAY)
                {
                    lastModifiedTime = currentTime;
                    Logger.FORGE_LOG_INFO("Change Detected");
                    GameManager.buildUserCode();
                }

                // - - - Reset the key, exit loop if the directory is inaccessible
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
