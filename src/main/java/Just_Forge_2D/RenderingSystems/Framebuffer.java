package Just_Forge_2D.RenderingSystems;

import Just_Forge_2D.Utils.Logger;

import static org.lwjgl.opengl.GL30.*;

public class Framebuffer
{
    // private variables
    private final int fboID;
    private final Texture texture;
    private final int width, height;


    // - - - | Functions | - - -


    // - - - constructor
    public Framebuffer(int WIDTH, int HEIGHT)
    {
        this.width = WIDTH;
        this.height = HEIGHT;
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
            int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
            Logger.FORGE_LOG_ERROR("Frame buffer not complete, status: " + status);
            switch (status)
            {
                case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                    Logger.FORGE_LOG_ERROR("Framebuffer incomplete attachment.");
                    break;

                case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                    Logger.FORGE_LOG_ERROR("Framebuffer missing attachment.");
                    break;

                case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER:
                    Logger.FORGE_LOG_ERROR("Framebuffer incomplete draw buffer.");
                    break;

                case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER:
                    Logger.FORGE_LOG_ERROR("Framebuffer incomplete read buffer.");
                    break;

                case GL_FRAMEBUFFER_UNSUPPORTED:
                    Logger.FORGE_LOG_ERROR("Framebuffer unsupported format.");
                    break;

                default:
                    Logger.FORGE_LOG_ERROR("Framebuffer error.");
                    break;
            }
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


    // - - - width and height - - -

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }
}
