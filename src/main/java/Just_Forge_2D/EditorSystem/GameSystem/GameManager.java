package Just_Forge_2D.EditorSystem.GameSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import static Just_Forge_2D.EditorSystem.EditorSystemManager.projectDir;
import static Just_Forge_2D.EditorSystem.ProjectManager.copyDirectory;

public class GameManager
{
    private static Game game;
    private static boolean success = false;
    private static String destinationDirPath;

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

    public static void compileCode()
    {
        Logger.FORGE_LOG_TRACE("Building Game");
        ImGui.begin("Building the game");
        ImGui.textColored(0, 0, 0, 1, "wait please");
        new Thread(() ->
        {
            try
            {
                String gradlewCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "./gradlew.bat" : "./gradlew";
                ProcessBuilder processBuilder = new ProcessBuilder(gradlewCommand, "shadowJar");
                processBuilder.directory(new File(projectDir));

                processBuilder.inheritIO();

                Process process = processBuilder.start();

                int exitCode = process.waitFor();
                if (exitCode != 0)
                {
                    TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "Error in user code. Exit code : " + exitCode, "error");
                    Logger.FORGE_LOG_FATAL("Error in user code. Exit code : " + exitCode);
                    return;
                }
                try
                {
                    File classesDir = new File(projectDir + "/build/classes/java/main");
                    URLClassLoader classLoader = new URLClassLoader(new URL[]{classesDir.toURI().toURL()});

                    Class<?> gameClass = classLoader.loadClass("Main");
                    Constructor<?> constructor = gameClass.getConstructor();
                    game = (Game) constructor.newInstance();
                    destinationDirPath = TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", projectDir);
                    if (destinationDirPath != null)
                    {
                        try
                        {
                            File[] directory = new File[]{new File(destinationDirPath + "/Assets"), new File(destinationDirPath + "/Saves") };
                            for (File dir : directory)
                            {
                                if (!dir.exists())
                                {
                                    try
                                    {
                                        if (dir.mkdir()) Logger.FORGE_LOG_INFO("Created the Forge Projects Directory");
                                        else Logger.FORGE_LOG_ERROR("Couldnt create the Forge Projects Directory");
                                    }
                                    catch (SecurityException e)
                                    {
                                        Logger.FORGE_LOG_FATAL("Security exception : " + e.getMessage());
                                    }
                                }
                            }

                            copyDirectory(projectDir + "/build/libs", destinationDirPath);
                            copyDirectory(projectDir + "/Assets", destinationDirPath + "/Assets");
                            copyDirectory(projectDir + "/Saves", destinationDirPath+ "/Saves");
                            File[] jarFiles = new File(destinationDirPath).listFiles((dir, name) -> name.endsWith(".jar"));

                            if (jarFiles == null || jarFiles.length == 0)
                            {
                                Logger.FORGE_LOG_ERROR("No JAR files found in the directory.");
                            }

                            File jarToExecute = Arrays.stream(jarFiles)
                                    .filter(jar -> jar.getName().endsWith("-all.jar"))
                                    .findFirst()
                                    .orElse(null);

                            if (jarToExecute == null) {
                                Logger.FORGE_LOG_ERROR("No JAR file ending with '-all' found.");
                                return;
                            }

                            // Delete other JAR files
                            for (File jar : jarFiles) {
                                if (!jar.equals(jarToExecute)) {
                                    if (jar.delete()) {
                                        Logger.FORGE_LOG_TRACE("Deleted: " + jar.getName());
                                    } else {
                                        Logger.FORGE_LOG_TRACE("Failed to delete: " + jar.getName());
                                    }
                                }
                            }

                            // Execute the '-all' JAR file

                            TinyFileDialogs.tinyfd_beep();
                            TinyFileDialogs.tinyfd_notifyPopup("Successfully compiled project", "Your game has been made", "info");
                        }
                        catch (Exception e)
                        {
                            TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "Please use terminal, I dont want to code more", "error");
                        }
                    }
                }
                catch (Exception e)
                {
                    TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "Couldn't find any entry point in the user code. Are you sure that you implemented the Game Interface", "error");
                    Logger.FORGE_LOG_FATAL("Couldn't find any entry point in the user code. Are you sure that you implemented the Game Interface");
                    return;
                }
                Logger.FORGE_LOG_TRACE("Build successful");

            }
            catch (Exception e)
            {
                TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "Failed to build user code: \" + e.getCause()", "error");
                Logger.FORGE_LOG_FATAL("Failed to build user code: " + e.getCause());
            }
        }).start();
        ImGui.end();
    }

    public static void runCode() {
        new Thread(() -> {
            File destinationDir = new File(destinationDirPath);

            if (!destinationDir.exists() || !destinationDir.isDirectory()) {
                Logger.FORGE_LOG_ERROR("The specified destination directory does not exist or is not a directory.");
                TinyFileDialogs.tinyfd_notifyPopup("Error", "The specified destination directory does not exist or is not a directory", "error");
                return;
            }

            File[] jarFiles = destinationDir.listFiles((dir, name) -> name.endsWith(".jar"));

            if (jarFiles == null || jarFiles.length == 0) {
                Logger.FORGE_LOG_ERROR("No JAR files found in the directory.");
                TinyFileDialogs.tinyfd_notifyPopup("Error", "No JAR files found in your directory", "error");
                return;
            }

            File jarToExecute = Arrays.stream(jarFiles)
                    .filter(jar -> jar.getName().endsWith("-all.jar"))
                    .findFirst()
                    .orElse(null);

            if (jarToExecute == null) {
                Logger.FORGE_LOG_ERROR("No JAR file ending with '-all' found.");
                TinyFileDialogs.tinyfd_notifyPopup("Error", "No executable JAR files found", "error");
                return;
            }

            try {
                // Change directory to destinationDir
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarToExecute.getName());
                processBuilder.directory(destinationDir);
                processBuilder.inheritIO(); // To show the output of the JAR in the console
                Process process = processBuilder.start();
                process.waitFor();  // Wait for the JAR process to finish
            } catch (Exception e) {
                Logger.FORGE_LOG_FATAL(e.getCause());
            }
        }).start(); // Start the thread
    }

}
