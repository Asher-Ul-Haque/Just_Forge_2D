package Just_Forge_2D.Utils;

public class justForgeTime
{
    public static double timeStarted = System.nanoTime();

    public static double getTime()
    {
        return (System.nanoTime() - timeStarted) * 1E-9;
    }
}
