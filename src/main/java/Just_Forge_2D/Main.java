package Just_Forge_2D;

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
        Window window = Window.get();
        window.run();
    }
}