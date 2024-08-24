package Just_Forge_2D.ForgeEditor;
import org.lwjgl.util.tinyfd.TinyFileDialogs;


public class ProjectManager
{
    public static void createNewProject()
    {
        String projectName = TinyFileDialogs.tinyfd_inputBox("Project Name", "Enter the project name:", "");
        if (projectName != null && !projectName.isEmpty())
        {
            String directory = TinyFileDialogs.tinyfd_selectFolderDialog("Select Project Directory", null);
            if (directory != null)
            {

            }
        }
    }

    public static void openExistingProject()
    {
        String filePath = TinyFileDialogs.tinyfd_openFileDialog("Open Project", null, null, null, false);
        if (filePath != null)
        {
            System.out.println("Opening Project from: " + filePath);
        }
    }
}
