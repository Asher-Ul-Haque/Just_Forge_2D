package Just_Forge_2D.GameSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

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
            return true;
        }
        return false;
    }

    private static String selectProjectDirectory()
    {
        return TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", Paths.get(DEFAULT_PROJECTS_DIR + DefaultValues.DEFAULT_SAVE_DIR).toAbsolutePath().toString());
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
}