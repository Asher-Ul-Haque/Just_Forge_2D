package Just_Forge_2D.RenderingSystems;

import Just_Forge_2D.Utils.Logger;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
    // private variables
    private final int fboID;
    private final Texture texture;


    // - - - | Functions | - - -


    // - - - constructor
    public Framebuffer(int WIDTH, int HEIGHT)
    {
        // - - - generate frame buffer
        fboID = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);

        // - - - create the texture
        this.texture = new Texture(WIDTH, HEIGHT);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.texture.getID(), 0); //not sure why 0

        // - - - create the render buffer to store the depth info
        int rboID = glGenRenderbuffers();
        glBindRenderbuffer(GL_RENDERBUFFER, rboID);
        glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT32, WIDTH, HEIGHT);
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, rboID);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Logger.FORGE_LOG_ERROR("Frame buffer not complete");
            Logger.FORGE_LOG_ERROR(glGetError());
            assert false;
        }
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        Logger.FORGE_LOG_INFO("Frame buffer created");
    }


    // - - - usage - - -

    public void bind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID);
    }

    public void unbind()
    {
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    // - - - getters - - -

    public int getFboID()
    {
        return fboID;
    }

    public int getTextureID()
    {
        return texture.getID();
    }
}
