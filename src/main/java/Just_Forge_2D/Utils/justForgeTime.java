package Just_Forge_2D.Utils;

public class justForgeTime
{
    // - - - Private Variables
    public static double timeStarted = System.nanoTime();

    // - - - Get the time
    public static double getTime()
    {
        return (System.nanoTime() - timeStarted) * 1E-9;
    }
}
