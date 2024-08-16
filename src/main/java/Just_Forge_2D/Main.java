package Just_Forge_2D;

import Just_Forge_2D.WindowSystem.Window;
import Just_Forge_2D.WindowSystem.WindowConfig;
import imgui.app.Application;
import imgui.app.Configuration;

public class Main extends Application
{
    @Override
    protected void configure(Configuration config)
    {
    }

    @Override
    public void process()
    {
    }

    public static void main(String[] args)
    {
        Window test = new Window(null);
        while (true)
        {
            test.loop();
        }
        //Forge window = Forge.get();
        //window.run();
    }
}