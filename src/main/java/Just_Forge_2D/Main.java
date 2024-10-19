package Just_Forge_2D;

import Just_Forge_2D.EditorSystem.EditorSystemManager;


public class Main
{
    private static boolean restart = false;

    public static void main(String[] args)
    {
        EditorSystemManager.run();
        if (restart)
        {
            restart = false;
            EditorSystemManager.run();
        }
    }

    public static void triggerRestart()
    {
        restart = true;
    }
}