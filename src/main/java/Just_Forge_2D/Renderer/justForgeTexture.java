package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.justForgeLogger;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

// - - - Texture Loading Class
public class justForgeTexture
{
    // - - - Private variables
    private String filepath;
    private int textureID;


    // - - - Functions - - -

    // - - - Constructor to set up a use able texture
    public justForgeTexture(String FILEPATH)
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
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if (image != null)
        {
            int textureType = switch (channels.get(0))
            {
                case 3 ->
                {
                    justForgeLogger.FORGE_LOG_TRACE("RGB image: " + filepath);
                    yield GL_RGB; //RGB image
                }
                case 4 ->
                {
                    justForgeLogger.FORGE_LOG_TRACE("RGBA image " + filepath);
                    yield GL_RGBA; //RGBA image
                }
                default ->
                {
                    justForgeLogger.FORGE_LOG_WARNING("Unkown picture type with " + channels.get(0) + " attributes");
                    yield -1;
                }
            };

            glTexImage2D(GL_TEXTURE_2D, 0, textureType, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }
        else
        {
            justForgeLogger.FORGE_LOG_WARNING("Could not load image: " + filepath);
            assert false;
        }
        justForgeLogger.FORGE_LOG_DEBUG("Loaded image sucessfully: " + filepath);
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
}
