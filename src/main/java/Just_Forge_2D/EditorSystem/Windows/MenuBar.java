package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.GameManager;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.MainWindow;
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
            if (ImGui.menuItem("Save As", ""))
            {
                String savePath = TinyFileDialogs.tinyfd_saveFileDialog("Choose Save Location", EditorSystemManager.projectDir + DefaultValues.DEFAULT_SAVE_DIR,null,null);
                if (savePath != null)
                {
                    if (!savePath.endsWith(".justForgeFile")) savePath += ".justForgeFile";
                    MainWindow.getCurrentScene().setSavePath(savePath);
                    EventManager.notify(null, new Event(EventTypes.SaveLevel));
                }
            }
            if (ImGui.menuItem("Load From", ""))
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
            ImGui.endMenu();  // End the "File" menu
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

        if (ImGui.beginMenu("View"))
        {
            for (int i = 0; i < ImGUIManager.getRenderable().size(); ++i)
            {
                Boolean b = ImGUIManager.getRenderable().get(i);
                if (ImGui.menuItem((b ? "HIDE" : "SHOW") + "\t\t" + ImGUIManager.getRenderableNames().get(i)))
                {
                    ImGUIManager.getRenderable().set(i, !b);
                }
            }
            ImGui.endMenu();
        }

        if (ImGui.beginMenu("Settings"))
        {
            DefaultValues.render();
            ImGui.endMenu();
        }

        ImGui.endMainMenuBar();
    }
}