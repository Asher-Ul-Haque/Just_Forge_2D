package Just_Forge_2D.RenderingSystems;

import Just_Forge_2D.Utils.Logger;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.*;

// - - - Texture Loading Class
public class Texture
{
    // - - - Private variables
    private String filepath;
    private transient int textureID;
    private int width, height;

    // - - - Functions - - -

    // - - - Constructor
    public Texture()
    {
        textureID = -1;
        width = -1;
        height = -1;
    }

    public Texture(int WIDTH, int HEIGHT)
    {
        this.filepath = "Assets/Textures/Generated";

        // - - - Generate the texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // - - - sorry GPU, I cant give data because I dont have any.
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    public Texture(int WIDTH, int HEIGHT, String FILE_PATH)
    {
        this.filepath = FILE_PATH;

        // - - - Generate the texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        // - - - sorry GPU, I cant give data because I dont have any.
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, WIDTH, HEIGHT, 0, GL_RGBA, GL_UNSIGNED_BYTE, 0);
    }

    // - - - init
    public void init(String FILEPATH)
    {
        this.filepath = FILEPATH;

        // - - - Generate the texture on GPU
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        // - - - Set texture parameters
        // Repeat texture in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // - - - When shrinking or stretching pixelate it
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // - - - gET RGB data
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

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

            glTexImage2D(GL_TEXTURE_2D, 0, textureType, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Could not load image: " + filepath);
            assert false;
        }
        Logger.FORGE_LOG_DEBUG("Loaded image sucessfully: " + filepath);
        stbi_image_free(image);
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
}
