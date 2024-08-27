package Just_Forge_2D.EditorSystem;
import Just_Forge_2D.Utils.Logger;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.io.File;
import java.io.IOException;


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
            dirPath = TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", dirPath);
            if (dirPath != null)
            {
                dirPath += "/" + NAME;
                File hostDir = new File(dirPath);
                if (!hostDir.exists())
                {
                    if (!hostDir.mkdir())
                    {
                        Logger.FORGE_LOG_ERROR("Could create host directory : " + hostDir.getAbsolutePath());
                        return false;
                    }
                }
                else
                {
                    TinyFileDialogs.tinyfd_notifyPopup("Error in creating project", hostDir.getName() + " already exists", "error");
                    return false;
                }
                for (String dir : projectDirs)
                {
                    File file = new File(dirPath, dir);
                    if (file.exists()) continue;
                    if (file.mkdir())
                    {
                        Logger.FORGE_LOG_INFO("Creating directory : " + dir);
                    }
                    else
                    {
                        Logger.FORGE_LOG_ERROR("Couldn't create directory : " + dir);
                        return false;
                    }
                }
                for (String files : projectFiles)
                {
                    File file = new File(dirPath, files);
                    if (file.exists()) continue;
                    try
                    {
                        if (file.createNewFile())
                        {
                            Logger.FORGE_LOG_INFO("Creating file : " + files);
                        }
                        else
                        {
                            Logger.FORGE_LOG_ERROR("Couldn't create file : " + files);
                            return false;
                        }
                    }
                    catch (IOException e)
                    {
                        Logger.FORGE_LOG_FATAL("Couldn't create file : " + files);
                        return false;
                    }
                }
            }
            EditorSystemManager.projectDir = dirPath;
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
}
