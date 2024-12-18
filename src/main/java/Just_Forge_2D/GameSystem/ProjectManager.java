package Just_Forge_2D.GameSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.SceneSystem.SceneSystemManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ProjectManager
{
    // - - - project management defaults
    private static final String DEFAULT_PROJECTS_DIR = System.getProperty("user.home") + "/Documents/ForgeProjects";
    private static final String PROJECT_TEMPLATE_DIR = "ProjectTemplate";
    @NotNull public static String PROJECT_NAME = new File(System.getProperty("user.dir")).getName();


    // - - - Get the last project path:
    public static void saveLastProjectPath()
    {
        Path path = Paths.get("Configurations/lastProject.justForgeFile");
        try
        {
            Files.write(path, Paths.get(EditorSystemManager.projectDir).toAbsolutePath().toString().getBytes());
            System.out.println("File written to: " + path);
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_WARNING(e.getMessage());
        }
    }

    public static File getLastProjectPath()
    {
        Path path = Paths.get("Configurations/lastProject.justForgeFile");
        try
        {
            if (Files.exists(path))
            {
                String projectPath = new String(Files.readAllBytes(path)).trim();
                return new File(projectPath);
            }
            else
            {
                Logger.FORGE_LOG_WARNING("Last project path file does not exist.");
            }
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_WARNING(e.getMessage());
        }
        return null;
    }


    // - - - Creating new Project - - -

    public static boolean createNewProject()
    {
        String projectName = getProjectName();
        Logger.FORGE_LOG_INFO("Creating new Project: " + projectName);
        if (projectName == null || projectName.trim().isEmpty())
        {
            Logger.FORGE_LOG_ERROR("Project name is invalid or not provided.");
            return false;
        }

        createProjectsDir();
        String selectedDir = selectProjectDirectory();
        if (selectedDir == null || selectedDir.trim().isEmpty())
        {
            Logger.FORGE_LOG_ERROR("No project directory selected.");
            return false;
        }

        Path destinationDir = Paths.get(selectedDir).resolve(projectName.trim());
        if (Files.exists(destinationDir))
        {
            TinyFileDialogs.tinyfd_notifyPopup("Error", "Project already exists.", "error");
            return false;
        }

        try
        {
            copyDirectory(Paths.get(PROJECT_TEMPLATE_DIR), destinationDir);
            Logger.FORGE_LOG_INFO("Project created successfully at: " + destinationDir.toAbsolutePath());
            EditorSystemManager.projectDir = destinationDir.toString();
            PROJECT_NAME = projectName;
            return true;
        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_ERROR("Error creating project: " + e.getMessage());
            TinyFileDialogs.tinyfd_notifyPopup("Fatal Error", e.getMessage(), "error");
            return false;
        }
    }

    private static void createProjectsDir()
    {
        Path projectsDir = Paths.get(DEFAULT_PROJECTS_DIR);
        Logger.FORGE_LOG_INFO("Creating Project directory: " + projectsDir);
        if (Files.notExists(projectsDir))
        {
            try
            {
                Files.createDirectories(projectsDir);
                Logger.FORGE_LOG_INFO("Created the Forge Projects directory at: " + projectsDir);
            }
            catch (IOException | SecurityException e)
            {
                Logger.FORGE_LOG_FATAL("Failed to create Forge Projects directory: " + e.getMessage());
                TinyFileDialogs.tinyfd_notifyPopup("Fatal Error", "Failed to create Forge Projects directory: " + e.getMessage(), "error");
            }
        }
    }


    // - - - Opening Project - - -

    public static boolean openExistingProject()
    {
        String selectedDir = selectProjectDirectory();
        if (selectedDir != null && !selectedDir.trim().isEmpty())
        {
            EditorSystemManager.projectDir = selectedDir;
            File dir = new File(selectedDir);
            PROJECT_NAME = dir.getName();
            try
            {
                File[] files = dir.listFiles((directory, name) -> name.endsWith(".justForgeFile"));
                for (File script : files)
                {
                    SceneSystemManager.sceneScripts.add(script.getAbsolutePath());
                }
            }
            catch (Exception e)
            {
                Logger.FORGE_LOG_ERROR("Failed to read scripts");
                Logger.FORGE_LOG_ERROR(e.getMessage());
            }
            return true;
        }
        return false;
    }

    private static String selectProjectDirectory()
    {
        return TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", Paths.get(DEFAULT_PROJECTS_DIR + Settings.DEFAULT_SAVE_DIR()).toAbsolutePath().toString());
    }


    // - - - Helper Function - - -

    private static String getProjectName()
    {
        return TinyFileDialogs.tinyfd_inputBox("Project Name:", null, "newProject");
    }

    public static void copyDirectory(Path source, Path destination) throws IOException
    {
        if (Files.notExists(source))
        {
            Logger.FORGE_LOG_FATAL("Source directory does not exist: " + source);
            return;
        }

        Files.walk(source).forEach(sourcePath ->
        {
            try
            {
                Path destinationPath = destination.resolve(source.relativize(sourcePath));
                if (Files.isDirectory(sourcePath))
                {
                    Files.createDirectories(destinationPath);
                }
                else
                {
                    Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
            catch (IOException e)
            {
                Logger.FORGE_LOG_FATAL("Error copying: " + sourcePath + " to " + destination + " - " + e.getMessage());
            }
        });
    }

    public static boolean copyFile(String SRC, String DST)
    {
        try
        {
            Path source = Paths.get(SRC);
            Path destination = Paths.get(DST);
            Files.copy(source, destination);
            return true;
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_ERROR("Failed to copy file: " + SRC + " "+ e.getMessage());
            return false;
        }
    }

    public static void openProjectInBrowser()
    {
        try
        {
            File projectDirFile = new File(EditorSystemManager.projectDir);
            if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN))
            {
                new Thread(() ->
                {
                    try
                    {
                        Desktop.getDesktop().open(projectDirFile);
                    }
                    catch (IOException e)
                    {
                        Logger.FORGE_LOG_ERROR(e.getMessage());
                    }
                }).start();
            }
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_ERROR(e.getMessage());
        }
    }
}