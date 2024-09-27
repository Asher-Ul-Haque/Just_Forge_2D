package Just_Forge_2D.EditorSystem.GameSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Utils.Logger;
import imgui.ImGui;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static Just_Forge_2D.EditorSystem.ProjectManager.copyDirectory;

public class GameManager
{
    protected static Game game;
    protected static boolean success = false;
    private static Path destinationDirPath;

    // - - - Code Loading - - -

    public static void buildUserCode() {
        Logger.FORGE_LOG_INFO("Reading Your Code");

        try {
            if (!EditorSystemManager.isRelease) {
                String gradlewCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "gradlew.bat" : "./gradlew";
                ProcessBuilder processBuilder = new ProcessBuilder(gradlewCommand, "build");
                processBuilder.directory(new File(EditorSystemManager.projectDir));
                processBuilder.inheritIO();

                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    Logger.FORGE_LOG_FATAL("Error in user code. Exit code : " + exitCode);
                    return;
                }
            }

            try {
                Path projectPath = Paths.get(EditorSystemManager.projectDir);
                Path classesDir = projectPath.resolve("build/classes/java/main");
                URLClassLoader classLoader = new URLClassLoader(new URL[]{classesDir.toUri().toURL()});

                Class<?> gameClass = classLoader.loadClass("Main");
                Constructor<?> constructor = gameClass.getConstructor();
                game = (Game) constructor.newInstance();
                success = true;
            } catch (Exception e) {
                Logger.FORGE_LOG_ERROR(e.getMessage());
                Logger.FORGE_LOG_FATAL("Couldn't find an entry point in the user code. Ensure that the Game Interface is implemented.");
                return;
            }

            game.init();
            Logger.FORGE_LOG_TRACE("Build successful");
        } catch (Exception e) {
            Logger.FORGE_LOG_FATAL("Failed to build user code: " + e.getMessage());
        }


        // Check the success variable here if needed
        if (success) {
            Logger.FORGE_LOG_INFO("The build was successful, and the Main class was loaded.");
        } else {
            Logger.FORGE_LOG_WARNING("The build process failed or the Main class was not loaded correctly.");
        }
    }

    public static void compileCode()
    {
        Logger.FORGE_LOG_TRACE("Building Game");
        ImGui.begin("Building the game");
        ImGui.textColored(0, 0, 0, 1, "Please wait...");

        new Thread(() ->
        {
            try
            {
                String gradlewCommand = System.getProperty("os.name").toLowerCase().contains("win") ? "./gradlew.bat" : "./gradlew";
                ProcessBuilder processBuilder = new ProcessBuilder(gradlewCommand, "shadowJar");
                processBuilder.directory(new File(EditorSystemManager.projectDir.toString()));
                processBuilder.inheritIO();

                Process process = processBuilder.start();
                int exitCode = process.waitFor();
                if (exitCode != 0)
                {
                    TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "Error in user code. Exit code : " + exitCode, "error");
                    Logger.FORGE_LOG_FATAL("Error in user code. Exit code : " + exitCode);
                    Logger.FORGE_LOG_FATAL(process.errorReader().toString());
                    return;
                }

                Path projectPath = Paths.get(EditorSystemManager.projectDir);
                Path assetsPath = projectPath.resolve("Assets");
                Path savesPath = projectPath.resolve("Scenes");

                destinationDirPath = Paths.get(TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", projectPath.toString()));
                if (destinationDirPath != null)
                {
                    try
                    {
                        File[] directoriesToCreate = {new File(destinationDirPath.resolve("Assets").toString()), new File(destinationDirPath.resolve("Saves").toString())};
                        for (File dir : directoriesToCreate)
                        {
                            if (!dir.exists())
                            {
                                if (dir.mkdir()) Logger.FORGE_LOG_INFO("Created directory: " + dir.getPath());
                                else Logger.FORGE_LOG_ERROR("Couldn't create directory: " + dir.getPath());
                            }
                        }

                        copyDirectory(projectPath.resolve("build/libs"), destinationDirPath);
                        copyDirectory(assetsPath, destinationDirPath.resolve("Assets"));
                        copyDirectory(savesPath, destinationDirPath.resolve("Scenes"));

                        File[] jarFiles = destinationDirPath.toFile().listFiles((dir, name) -> name.endsWith(".jar"));
                        if (jarFiles == null || jarFiles.length == 0)
                        {
                            TinyFileDialogs.tinyfd_notifyPopup("Error in running project", "No JAR file found", "error");
                            Logger.FORGE_LOG_ERROR("No JAR files found in the directory.");
                            return;
                        }

                        File jarToExecute = Arrays.stream(jarFiles)
                                .filter(jar -> jar.getName().endsWith("-all.jar"))
                                .findFirst()
                                .orElse(null);

                        if (jarToExecute == null)
                        {
                            TinyFileDialogs.tinyfd_notifyPopup("Error in running project", "No JAR file ending with '-all' found", "error");
                            Logger.FORGE_LOG_ERROR("No JAR file ending with '-all' found.");
                            return;
                        }

                        // Delete other JAR files except '-all.jar'
                        for (File jar : jarFiles)
                        {
                            if (!jar.equals(jarToExecute) && jar.delete())
                            {
                                Logger.FORGE_LOG_TRACE("Deleted: " + jar.getName());
                            }
                        }

                        TinyFileDialogs.tinyfd_notifyPopup("Successfully compiled project", "Your game has been built", "info");
                    }
                    catch (IOException e)
                    {
                        TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "I/O error occurred", "error");
                        Logger.FORGE_LOG_FATAL("Error in compiling project: " + e.getMessage());
                    }
                }
            } catch (Exception e)
            {
                TinyFileDialogs.tinyfd_notifyPopup("Error in compiling project", "Failed to build user code: " + e.getMessage(), "error");
                Logger.FORGE_LOG_FATAL("Failed to build user code: " + e.getMessage());
            }
        }).start();

        ImGui.end();
    }

    public static void runCode()
    {
        new Thread(() ->
        {
            File destinationDir = destinationDirPath.toFile();
            if (!destinationDir.exists() || !destinationDir.isDirectory())
            {
                Logger.FORGE_LOG_ERROR("The specified destination directory does not exist or is not a directory.");
                TinyFileDialogs.tinyfd_notifyPopup("Error", "The specified destination directory does not exist or is not a directory", "error");
                return;
            }

            File[] jarFiles = destinationDir.listFiles((dir, name) -> name.endsWith(".jar"));
            if (jarFiles == null || jarFiles.length == 0)
            {
                Logger.FORGE_LOG_ERROR("No JAR files found in the directory.");
                TinyFileDialogs.tinyfd_notifyPopup("Error", "No JAR files found in your directory", "error");
                return;
            }

            try
            {
                ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", jarFiles[0].getName());
                processBuilder.directory(destinationDir);
                processBuilder.inheritIO();  // To show the output of the JAR in the console
                processBuilder.start().waitFor();  // Wait for the JAR process to finish
            }
            catch (Exception e)
            {
                TinyFileDialogs.tinyfd_notifyPopup("Failed to run the code", e.getMessage(), "error");
                Logger.FORGE_LOG_FATAL("Failed to run the code: " + e.getMessage());
            }
        }).start();
    }

    public static boolean isSuccess()
    {
        return success;
    }
}