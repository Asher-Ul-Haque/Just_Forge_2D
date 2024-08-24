package Just_Forge_2D.ForgeEditor.Windows;

import Just_Forge_2D.Forge;
import Just_Forge_2D.ForgeEditor.ImGuiManager;
import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;

public class EditorWindow extends Window
{
    public EditorWindow(WindowConfig CONFIG)
    {
        super(CONFIG);
    }

    @Override
    public void loop()
    {
        warnFPSspike();
        render();
        ImGuiManager.update(dt);
        manageInput();
        Forge.update(dt);
        finishInputFrames();
        keepTime();
    }
}
