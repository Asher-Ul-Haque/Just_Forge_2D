package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.AssetPool.AssetPoolSerializer;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.GameSystem.GameCodeLoader;
import Just_Forge_2D.GameSystem.GameManager;
import Just_Forge_2D.GameSystem.ProjectManager;
import Just_Forge_2D.PrefabSystem.PrefabSerializer;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

public class MenuBar
{
    public static void render()
    {
        ImGui.beginMainMenuBar();

        if (ImGui.beginMenu(Icons.Gamepad + "  Project"))
        {
            if (ImGui.menuItem(Icons.Code + "  Recompile Game"))
            {
                GameManager.buildUserCode();
            }
            if (ImGui.menuItem(Icons.FolderOpen + "  Open in Browser"))
            {
                ProjectManager.openProjectInBrowser();
            }
            if (ImGui.menuItem(Icons.WindowClose + "  Close"))
            {
                AssetPoolSerializer.saveAssetPool(EditorSystemManager.projectDir + "/.forge/Pool.justForgeFile");
                PrefabSerializer.savePrefabs(EditorSystemManager.projectDir + "/.forge/Assets/Prefabs.justForgeFile");
                GameCodeLoader.terminate();
                GameCodeLoader.closeEye();
                EventManager.notify(null, new Event(EventTypes.ForgeStop));
                EditorSystemManager.setCurrentState(EditorSystemManager.state.isSplashScreen);
                GameWindow.get().restore();
                GameWindow.get().resetTitleBar();
                SplashScreen.restart();
                SplashScreen.initialize();
            }
            ImGui.endMenu();
        }
        ImGui.separator();

        if (ImGui.beginMenu(Icons.Film + " Scene"))
        {
            if (ImGui.menuItem(Icons.Save + " Save", ""))
            {
                EventManager.notify(null, new Event(EventTypes.SaveLevel));
            }
            if (ImGui.menuItem(Icons.Save+ " Save As", ""))
            {
                String savePath = TinyFileDialogs.tinyfd_saveFileDialog("Choose Save Location", EditorSystemManager.projectDir + Settings.DEFAULT_SAVE_DIR,null,null);
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
                String savePath = TinyFileDialogs.tinyfd_openFileDialog("Choose Save Location", EditorSystemManager.projectDir + Settings.DEFAULT_SAVE_DIR,null,null, false);
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
                if (ImGUIManager.getRenderableNames().get(i).startsWith("Keyboard")) continue;
                Boolean b = ImGUIManager.getRenderable().get(i);
                if (ImGui.checkbox(ImGUIManager.getRenderableNames().get(i), b)) b = !b;
                ImGUIManager.getRenderable().set(i, b);
            }
            ImGui.endMenu();
        }
        ImGui.separator();

        if (ImGui.beginMenu(Icons.Cog + "  Settings"))
        {
            if (ImGui.menuItem(Icons.Save + "  Save"))
            {
                Settings.save();
            }
            if (ImGui.menuItem(Icons.FileUpload + "  Load"))
            {
                Settings.load();
            }
            ImGui.endMenu();
        }
        ImGui.separator();

        ImGui.endMainMenuBar();
    }
}