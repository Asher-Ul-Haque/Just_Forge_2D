package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.justForgeLogger;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public class justForgeTexture
{
    private String filepath;
    private int textureID;

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
            justForgeLogger.FORGE_LOG_TRACE("Loaded image sucessfully: " + filepath);
            int textureType;

            switch (channels.get(0))
            {
                case 3: //RGB image
                    justForgeLogger.FORGE_LOG_TRACE("RGB image: " + filepath);
                    textureType = GL_RGB;
                    break;

                case 4: //RGBA image
                    justForgeLogger.FORGE_LOG_TRACE("RGBA image " + filepath);
                    textureType = GL_RGBA;
                    break;

                default:
                    justForgeLogger.FORGE_LOG_WARNING("Unkown picture type with " + channels.get(0) + " attributes");
                    textureType = -1;
                    break;
            }
            glTexImage2D(GL_TEXTURE_2D, 0, textureType, width.get(0), height.get(0), 0, GL_RGBA, GL_UNSIGNED_BYTE, image);
        }
        else
        {
            justForgeLogger.FORGE_LOG_WARNING("Could not load image: " + filepath);
            assert false;
        }

        stbi_image_free(image);
    }

    public void bind()
    {
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void detach()
    {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
