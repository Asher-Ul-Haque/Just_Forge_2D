package Just_Forge_2D.RenderingSystem;

import static org.lwjgl.opengl.GL11.*;

public enum TextureMinimizeFilter
{
    NEAREST(GL_NEAREST),
    LINEAR(GL_LINEAR),
    NEAREST_MIPMAP_NEAREST(GL_NEAREST_MIPMAP_NEAREST),
    NEAREST_MIPMAP_LINEAR(GL_NEAREST_MIPMAP_LINEAR),
    LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR);

    protected int OPEN_GL_FILTER;

    TextureMinimizeFilter(int A)
    {
        this.OPEN_GL_FILTER = A;
    }
}
