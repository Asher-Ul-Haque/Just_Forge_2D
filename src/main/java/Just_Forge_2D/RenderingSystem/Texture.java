package Just_Forge_2D.RenderingSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import org.joml.Vector2i;
import org.lwjgl.BufferUtils;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.*;

// - - - Texture Loading Class
public class Texture
{
    // - - - Private variables
    private String filepath;
    private int textureID;
    private int width, height;
    private TextureMinimizeFilter MIN_FILTER = Settings.DEFAULT_TEXTURE_MIN_FILTER();
    private TextureMaximizeFilter MAX_FILTER = Settings.DEFAULT_TEXTURE_MAX_FILTER();
    private TextureWrapping WARP_S = Settings.DEFAULT_TEXTURE_WRAP_S();
    private TextureWrapping WARP_T = Settings.DEFAULT_TEXTURE_WRAP_T();

    // - - - Functions - - -

    // - - - Constructor
    public Texture()
    {}

    public Texture(int WIDTH, int HEIGHT)
    {
        this.filepath = "Assets/Textures/logo.png";

        // - - - Generate the texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, MIN_FILTER.OPEN_GL_FILTER);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, MAX_FILTER.OPEN_GL_FILTER);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    public Texture(int WIDTH, int HEIGHT, String FILE_PATH)
    {
        try
        {
            // - - - Generate the texture on GPU
            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, MIN_FILTER.OPEN_GL_FILTER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, MAX_FILTER.OPEN_GL_FILTER);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);

            init(FILE_PATH);
        }
        catch (Throwable e)
        {
            Logger.FORGE_LOG_FATAL(e.getMessage());
        }
    }

    // - - - init
    public boolean init(String FILEPATH)
    {
        this.filepath = FILEPATH;

        // - - - Generate the texture on GPU
        try
        {
            textureID = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, textureID);

            // - - - Set texture parameters
            // Repeat texture in both directions
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, WARP_S.OPEN_GL_FILTER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, WARP_T.OPEN_GL_FILTER);

            // - - - When shrinking or stretching pixelate it
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, MIN_FILTER.OPEN_GL_FILTER);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, MAX_FILTER.OPEN_GL_FILTER);

            // - - - gET RGB data
            IntBuffer width = BufferUtils.createIntBuffer(1);
            IntBuffer height = BufferUtils.createIntBuffer(1);
            IntBuffer channels = BufferUtils.createIntBuffer(1);
            stbi_set_flip_vertically_on_load(true);
            String correctPath;
            if (AssetPool.isAbsolutePath(FILEPATH)) correctPath = FILEPATH;
            else correctPath = new File(System.getProperty("user.dir"), FILEPATH).getAbsolutePath();
            ByteBuffer image = stbi_load(correctPath, width, height, channels, 0);

            if (image != null)
            {
                this.width = width.get(0);
                this.height = height.get(0);
                int textureType = switch (channels.get(0))
                {
                    case 3 ->
                    {
                        Logger.FORGE_LOG_TRACE("RGB image: " + filepath);
                        yield GL_RGB; //RGB image
                    }
                    case 4 ->
                    {
                        Logger.FORGE_LOG_TRACE("RGBA image " + filepath);
                        yield GL_RGBA; //RGBA image
                    }
                    default ->
                    {
                        Logger.FORGE_LOG_ERROR("Unknown picture type with " + channels.get(0) + " attributes");
                        yield -1;
                    }
                };

                glTexImage2D(GL_TEXTURE_2D, 0, textureType, width.get(0), height.get(0), 0, textureType, GL_UNSIGNED_BYTE, image);
                stbi_image_free(image);
            }
            Logger.FORGE_LOG_DEBUG("Loaded image sucessfully: " + filepath);
            return true;
        }
        catch (Throwable e)
        {
            Logger.FORGE_LOG_ERROR("Could not load image: " + filepath);
            Logger.FORGE_LOG_ERROR(e.getMessage());
            return false;
        }
    }

    // - - - Bind texture to an object
    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    // - - - Remove texture from an object
    public void detach()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }

    public int getID()
    {
        return this.textureID;
    }

    // - - - get the filepath
    public String getFilepath()
    {
        return this.filepath;
    }

    // - - - compare two textures
    @Override
    public boolean equals(Object OBJECT)
    {
        if (OBJECT == null) return false;
        if (!(OBJECT instanceof Texture oTexture)) return false;
        return oTexture.getWidth() == this.width && oTexture.getHeight() == this.height && oTexture.getID() == this.textureID && oTexture.getFilepath().equals(this.filepath);
    }

    public void setFilters(TextureMaximizeFilter MAX, TextureMinimizeFilter MIN, TextureWrapping S, TextureWrapping T)
    {
        this.MAX_FILTER = MAX;
        this.MIN_FILTER = MIN;
        this.WARP_S = S;
        this.WARP_T = T;
        init(this.getFilepath());
    }

    public TextureWrapping getWrap_tFilter()
    {
        return WARP_T;
    }

    public TextureWrapping getWrap_sFilter()
    {
        return WARP_S;
    }

    public TextureMaximizeFilter getMaximizeFilter()
    {
        return MAX_FILTER;
    }

    public TextureMinimizeFilter getMinimizeFilter()
    {
        return MIN_FILTER;
    }

    public void setMIN_FILTER(TextureMinimizeFilter MIN_FILTER)
    {
        this.MIN_FILTER = MIN_FILTER;
    }

    public void setMAX_FILTER(TextureMaximizeFilter MAX_FILTER)
    {
        this.MAX_FILTER = MAX_FILTER;
    }

    public void setWARP_S(TextureWrapping WARP_S)
    {
        this.WARP_S = WARP_S;
    }

    public void setWARP_T(TextureWrapping WARP_T)
    {
        this.WARP_T = WARP_T;
    }

    public float[] readPixel(int X, int Y)
    {
        float[] pixels = new float[4];
        int framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer);

        // - - - Attach the texture to the framebuffer
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureID, 0);

        // - - - Check if the framebuffer is complete
        if (glCheckFramebufferStatus(GL_READ_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Logger.FORGE_LOG_FATAL("Framebuffer for texture : " + this.filepath + " is not complete");
            return pixels;
        }

        // - - - Prepare the buffer to store pixel data

        // - - - Read the pixel
        glReadPixels(X, Y, 1, 1, GL_RGBA, GL_FLOAT, pixels);

        // - - - Clean up
        glBindFramebuffer(GL_READ_FRAMEBUFFER, 0);
        glDeleteFramebuffers(framebuffer);

        // - - - Return the pixel data
        return pixels;
    }

    public float[] readPixels(Vector2i START, Vector2i END)
    {
        Vector2i size = new Vector2i(END).sub(START).absolute();
        float[] pixels = new float[4 * size.x * size.y];
        int framebuffer = glGenFramebuffers();
        glBindFramebuffer(GL_READ_FRAMEBUFFER, framebuffer);

        // - - - Attach the texture to the framebuffer
        glFramebufferTexture2D(GL_READ_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, textureID, 0);

        // - - - Check if the framebuffer is complete
        if (glCheckFramebufferStatus(GL_READ_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE)
        {
            Logger.FORGE_LOG_FATAL("Framebuffer for texture : " + this.filepath + " is not complete");
            return pixels;
        }

        glReadPixels(START.x, START.y, size.x, size.y, GL_RGBA, GL_FLOAT, pixels);
        for (int i = 0; i < pixels.length; ++i)
        {
            pixels[i] -= 1;
        }

        return pixels;
    }
}