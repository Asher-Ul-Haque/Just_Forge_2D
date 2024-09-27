package Just_Forge_2D.Utils;

// import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TimeKeeper
{
    // - - - Private Variables
    public static double timeStarted = System.nanoTime();

    // - - - Get the time
    public static double getTime()
    {
        //return glfwGetTime();
        return (System.nanoTime() - timeStarted) * 1E-9;
    }
}
