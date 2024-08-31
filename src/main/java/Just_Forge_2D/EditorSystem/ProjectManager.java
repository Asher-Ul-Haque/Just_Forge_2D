package Just_Forge_2D.EditorSystem;
import Just_Forge_2D.Utils.Logger;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ProjectManager
{
    static String dirPath = System.getProperty("user.home") + "/Documents/ForgeProjects";
    static String[] projectDirs = {"Assets", "Assets/Textures", "Assets/Sounds", "Assets/Shaders", "src", "src/main", "src/main/java", "build"};
    static String[] projectFiles = {"src/main/java/Just_Forge_2D.Main.java"};

    public static boolean createNewProject()
    {
        String NAME = TinyFileDialogs.tinyfd_inputBox("Project name : ", null, "newProject");
        createProjectsDir();
        if (NAME != null && !NAME.isEmpty())
        {
            String destinationDirPath = TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", dirPath);
            if (destinationDirPath != null)
            {
                destinationDirPath += "/" + NAME;
                File destinationDir = new File(destinationDirPath);
                if (!destinationDir.exists())
                {
                    try
                    {
                        copyDirectory("ProjectTemplate", destinationDirPath);
                        Logger.FORGE_LOG_INFO("Project created successfully at: " + destinationDir.getAbsolutePath());
                    }
                    catch (IOException e)
                    {
                        Logger.FORGE_LOG_ERROR("Could not create project: " + e.getMessage());
                        return false;
                    }
                }
                else
                {
                    TinyFileDialogs.tinyfd_notifyPopup("Error in creating project", destinationDir.getName() + " already exists", "error");
                    return false;
                }
            }
            EditorSystemManager.projectDir = destinationDirPath;
            return true;
        }
        return false;
    }

    public static boolean openExistingProject()
    {
        String filePath = TinyFileDialogs.tinyfd_selectFolderDialog("Select Project", dirPath + "/");
        if (filePath != null)
        {
            EditorSystemManager.projectDir = filePath;
            return true;
        }
        return false;
    }

    private static void createProjectsDir()
    {
        File directory = new File(dirPath);
        if (!directory.exists())
        {
            try
            {
                if (directory.mkdir()) Logger.FORGE_LOG_INFO("Created the Forge Projects Directory");
                else Logger.FORGE_LOG_ERROR("Couldnt create the Forge Projects Directory");
            }
            catch (SecurityException e)
            {
                Logger.FORGE_LOG_FATAL("Security exception : " + e.getMessage());
            }
        }
    }

    public static void copyDirectory(String sourceDirectoryLocation, String destinationDirectoryLocation)
            throws IOException
    {
        Files.walk(Paths.get(sourceDirectoryLocation))
                .forEach(source -> {
                    Path destination = Paths.get(destinationDirectoryLocation, source.toString()
                            .substring(sourceDirectoryLocation.length()));
                    try {
                        Files.copy(source, destination);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
