package Just_Forge_2D.EditorSystem.Windows;

import Utils.Logger;
import org.joml.Vector2i;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;

// - - - Object Selector
public class ObjectSelector
{
    private static int fbo;


    // - - - | Functions | - - -


    // - - - Constructors and initialization - - -


    public static boolean init(int WIDTH, int HEIGHT)
    {
        // - - - generate framebuffer
        fbo = glGenFramebuffers();
        glBindFramebuffer(GL_FRAMEBUFFER, fbo);

        // - - - create the texture
        // - - - private variables
        int objectTextureId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, objectTextureId);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB32F, WIDTH, HEIGHT, 0, GL_RGB, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, objectTextureId, 0); //not sure why 0

        // - - - create a texture object for the depth buffer
        glEnable(GL_TEXTURE_2D);
        int depthTexture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, depthTexture);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, WIDTH, HEIGHT, 0, GL_DEPTH_COMPONENT, GL_FLOAT, 0);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthTexture, 0);

        // - - - disable the reading
        glReadBuffer(GL_NONE);
        glDrawBuffer(GL_COLOR_ATTACHMENT0);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Logger.FORGE_LOG_ERROR("Framebuffer not complete");
            Logger.FORGE_LOG_ERROR(glGetError());
            assert false;
            return false;
        }

        // - - - unbind texture and framebuffer
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        return true;
    }


    // - - - Writing - - -

    public static void enableWriting()
    {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo);
    }

    public static void disableWriting()
    {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0);
    }


    // - - - Reading - - -

    public static int readPixel(int X, int Y)
    {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        float[] pixels = new float[3];
        glReadPixels(X, Y, 1, 1, GL_RGB, GL_FLOAT, pixels);

        return (int)(pixels[0]) - 1;
    }

    public static float[] readPixels(Vector2i START, Vector2i END)
    {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fbo);
        glReadBuffer(GL_COLOR_ATTACHMENT0);

        Vector2i size = new Vector2i(END).sub(START).absolute();
        float[] pixels = new float[3 * size.x * size.y];
        glReadPixels(START.x, START.y, size.x, size.y, GL_RGB, GL_FLOAT, pixels);
        for (int i = 0; i < pixels.length; ++i)
        {
            pixels[i] -= 1;
        }

        return pixels;
    }
}
