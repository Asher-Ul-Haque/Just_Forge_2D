package Just_Forge_2D;

import Just_Forge_2D.EditorSystem.EditorWindow;
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
        /*
        Window test = new Window(null);
        int i = 0;
        while (!test.shouldClose())
        {
            test.loop();
            ++i;
        }
        test.close();
         */
        EditorWindow plzWork = new EditorWindow();
        plzWork.run();
    }
}