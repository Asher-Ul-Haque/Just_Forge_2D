package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.GameManager;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class MenuBar
{
    public static void render()
    {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu(Icons.Film + " Scene"))
        {
            if (ImGui.menuItem(Icons.Save + " Save", ""))
            {
                EventManager.notify(null, new Event(EventTypes.SaveLevel));
            }
            if (ImGui.menuItem(Icons.Save+ " Save As", ""))
            {
                String savePath = TinyFileDialogs.tinyfd_saveFileDialog("Choose Save Location", EditorSystemManager.projectDir + DefaultValues.DEFAULT_SAVE_DIR,null,null);
                if (savePath != null)
                {
                    if (!savePath.endsWith(".justForgeFile")) savePath += ".justForgeFile";
                    GameWindow.getCurrentScene().setSavePath(savePath);
                    EventManager.notify(null, new Event(EventTypes.SaveLevel));
                }
            }
            if (ImGui.menuItem(Icons.FileImport + " Load", ""))
            {
                EventManager.notify(null, new Event(EventTypes.LoadLevel));
            }
            if (ImGui.menuItem(Icons.FileImport + " Load From", ""))
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
                        GameWindow.getCurrentScene().setSavePath(savePath);
                        EventManager.notify(null, new Event(EventTypes.LoadLevel));
                    }
                }
            }
            ImGui.endMenu();
        }
        ImGui.separator();

        if (ImGui.beginMenu(Icons.Bug + "  Run"))
        {
            if (ImGui.menuItem(Icons.MugHot + "  Build JAR"))
            {
                GameManager.compileCode();
            }
            if (ImGui.menuItem(Icons.Terminal + "  Run JAR"))
            {
                GameManager.runCode();
            }
            ImGui.endMenu();
        }
        ImGui.separator();

        if (ImGui.beginMenu(Icons.Eye + "  View"))
        {
            for (int i = 0; i < ImGUIManager.getRenderable().size(); ++i)
            {
                ImGUIManager.getRenderable().set(i, Widgets.drawBoolControl(ImGUIManager.getRenderableNames().get(i), ImGUIManager.getRenderable().get(i)));
                Boolean b = ImGUIManager.getRenderable().get(i);
                if (ImGui.checkbox(ImGUIManager.getRenderableNames().get(i), b)) b = !b;
                ImGUIManager.getRenderable().set(i, b);
            }
            ImGui.endMenu();
        }
        ImGui.separator();

        if (ImGui.beginMenu(Icons.Cogs + "  Settings"))
        {
            DefaultValues.render();
            ImGui.endMenu();
        }
        ImGui.separator();

        ImGui.endMainMenuBar();
    }
}