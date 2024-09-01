package Just_Forge_2D.Utils;

import java.io.InputStream;

public class Resource
{
    private static final boolean USE_FULL_JAR;

    static
    {
        String mode = System.getenv("RESOURCE_MODE");
        USE_FULL_JAR = "FULL_JAR".equals(mode);
    }

    public static String getResourcePath(String PATH)
    {
        if (USE_FULL_JAR)
        {
            if (!PATH.startsWith("/"))
            {
                PATH = "/" + PATH;
            }
        }
        return PATH;
    }

    public static InputStream getResource(String PATH)
    {
        PATH = getResourcePath(PATH);
        InputStream stream = Resource.class.getResourceAsStream(PATH);
        if (stream == null)
        {
            Logger.FORGE_LOG_FATAL("Could find file : " + PATH);
        }
        return stream;
    }
}
