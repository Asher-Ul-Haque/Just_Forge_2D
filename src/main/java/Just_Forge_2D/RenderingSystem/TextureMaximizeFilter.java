package Just_Forge_2D.RenderingSystem;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;

public enum TextureMaximizeFilter
{
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR);

    protected int OPEN_GL_FILTER;

    TextureMaximizeFilter(int A)
    {
        this.OPEN_GL_FILTER = A;
    }
}
