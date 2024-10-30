package Just_Forge_2D.RenderingSystem;

import static org.lwjgl.opengl.GL14.*;

public enum TextureWrapping
{
    REPEAT(GL_REPEAT),
    MIRRORED_REPEAT(GL_MIRRORED_REPEAT),
    CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE),
    CLAMP_TO_BORDER(GL_CLAMP_TO_BORDER);

    protected int OPEN_GL_FILTER;

    TextureWrapping(int A)
    {
        this.OPEN_GL_FILTER = A;
    }
}
