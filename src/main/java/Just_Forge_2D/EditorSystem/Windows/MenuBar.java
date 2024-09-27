package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.GameSystem.GameManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Utils.DefaultValues;
import Utils.Logger;
import imgui.ImGui;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class MenuBar
{
    public static void render()
    {
        ImGui.beginMainMenuBar();
        if (ImGui.beginMenu("File"))
        {
            if (ImGui.menuItem("Save", ""))
            {
                EventManager.notify(null, new Event(EventTypes.SaveLevel));
            }
            if (ImGui.menuItem("Load", ""))
            {
                EventManager.notify(null, new Event(EventTypes.LoadLevel));
            }
            if (ImGui.menuItem("Save as", ""))
            {
                String savePath = TinyFileDialogs.tinyfd_saveFileDialog("Choose Save Location", EditorSystemManager.projectDir + DefaultValues.DEFAULT_SAVE_DIR,null,null);
                if (savePath != null)
                {
                    if (!savePath.endsWith(".justForgeFile")) savePath += ".justForgeFile";
                    MainWindow.getCurrentScene().setSavePath(savePath);
                    EventManager.notify(null, new Event(EventTypes.SaveLevel));
                }
            }
            if (ImGui.menuItem("Load from", ""))
            {
                String savePath = TinyFileDialogs.tinyfd_openFileDialog("Choose Save Location", EditorSystemManager.projectDir + DefaultValues.DEFAULT_SAVE_DIR,null,null, false);
                if (savePath != null)
                {
                    if (!savePath.endsWith(".justForgeFile"))
                    {
                        Logger.FORGE_LOG_ERROR("Couldn't open save file. Must be a .justForgeFile");
                    }
                    else
                    {
                        MainWindow.getCurrentScene().setSavePath(savePath);
                        EventManager.notify(null, new Event(EventTypes.LoadLevel));
                    }
                }
            }
            ImGui.endMenu();
        }
        if (ImGui.beginMenu("Run"))
        {
            if (ImGui.menuItem("Build JAR"))
            {
                GameManager.compileCode();
            }
            if (ImGui.menuItem("Run JAR"))
            {
                GameManager.runCode();
            }
            ImGui.endMenu();
        }
        ImGui.endMainMenuBar();
    }
}