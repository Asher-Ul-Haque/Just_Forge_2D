package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;

import static Just_Forge_2D.EditorSystem.EditorSystemManager.projectDir;

public class GameManager
{
    private static Game game;
    private static boolean success = false;
    public static void buildUserCode()
    {
        try
        {
            if (!EditorSystemManager.isRelease)
            {
                String gradlewCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "./gradlew.bat" : "./gradlew";
                ProcessBuilder processBuilder = new ProcessBuilder(gradlewCommand, "build");
                processBuilder.directory(new File(projectDir));

                processBuilder.inheritIO();

                Process process = processBuilder.start();

                int exitCode = process.waitFor();
                if (exitCode != 0)
                {
                    Logger.FORGE_LOG_FATAL("Error in user code. Exit code : " + exitCode);
                    return;
                }
            }
            try
            {
                File classesDir = new File(projectDir + "/build/classes/java/main");
                URLClassLoader classLoader = new URLClassLoader(new URL[]{classesDir.toURI().toURL()});

                Class<?> gameClass = classLoader.loadClass("Main");
                Constructor<?> constructor = gameClass.getConstructor();
                game = (Game) constructor.newInstance();
                success = true;
            }
            catch (Exception e)
            {
                Logger.FORGE_LOG_FATAL("Couldn't find any entry point in the user code. Are you sure that you implemented the Game Interface");
                return;
            }
            game.init();
            Logger.FORGE_LOG_TRACE("Build successful");
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_FATAL("Failed to build user code: " + e.getCause());
        }
        new Thread(() ->
        {

        }).start();
    }

    public static void init()
    {
        if (success) game.init();
    }

    public static void loop(float DELTA_TIME)
    {
        if (success) game.update(DELTA_TIME);
    }

    public static void terminate()
    {
        if (success) game.end();
    }
}
