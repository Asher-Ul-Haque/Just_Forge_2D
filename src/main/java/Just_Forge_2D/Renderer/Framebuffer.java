package Just_Forge_2D.Renderer;

import Just_Forge_2D.Utils.justForgeLogger;

import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL44.glBufferStorage;

public class Framebuffer
{
    private int fboID = 0;
    private Texture texture = null;

    public int getFboID()
    {
        return fboID;
    }

    public int getTextureID()
    {
        return texture.getID();
    }


    public Framebuffer(int WIDTH, int HEIGHT)
    {
        // - - - generate framebuffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // - - - create the texture
        this.texture = new Texture(WIDTH, HEIGHT);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getID(), 0); //not sure why 0

        // - - - create the renderbuffer to store the depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, WIDTH, HEIGHT);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            justForgeLogger.FORGE_LOG_ERROR("Framebuffer not complete");
            assert false;
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
}
