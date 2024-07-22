package Just_Forge_2D.Renderer;

import Just_Forge_2D.Core.justForgeLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class justForgeShader
{
    private int shaderProgramID;
    private String vertexSource, fragmentSource;
    private String filePath;

    public justForgeShader(String FILE_PATH)
    {
        this.filePath = FILE_PATH;
        try
        {
            String source = new String(Files.readAllBytes(Paths.get(filePath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // - - - Find first word after the word #type
            int index = source.indexOf("#type") + 6; // index of the beginning of next word
            int eol = source.indexOf("\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // - - - Find second word after the word #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\n", index);
            String secondPattern = source.substring(index, eol).trim();

            switch (firstPattern.toLowerCase())
            {
                case "vertex":
                    vertexSource = splitString[1];
                    break;

                case "fragment":
                    fragmentSource = splitString[1];
                    break;

                default:
                    justForgeLogger.FORGE_LOG_ERROR("Unexpected token: " + firstPattern);
                    throw new IOException("Unexpected token: " + firstPattern);
            }

            switch (secondPattern.toLowerCase())
            {
                case "vertex":
                    vertexSource = splitString[2];
                    break;

                case "fragment":
                    fragmentSource = splitString[2];
                    break;

                default:
                    justForgeLogger.FORGE_LOG_ERROR("Unexpected token: " + firstPattern);
                    throw new IOException("Unexpected token: " + firstPattern);
            }

        }
        catch (IOException e)
        {
            justForgeLogger.FORGE_LOG_ERROR("Could not open file for shader: " + filePath);
            justForgeLogger.FORGE_LOG_ERROR(e.getMessage());
            assert false;
        }

        justForgeLogger.FORGE_LOG_DEBUG(vertexSource);
        justForgeLogger.FORGE_LOG_DEBUG(fragmentSource);

    }

    public void compile()
    {
        // - - - | Compile and link shaders | - - -

        int vertexID, fragmentID;

        justForgeLogger.FORGE_LOG_INFO("Settting up shaders");

        // First load and compile the vertex shader
        justForgeLogger.FORGE_LOG_DEBUG("Compiling Default Vertex Shader");
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            justForgeLogger.FORGE_LOG_ERROR("defaultShader.glsl'\n\tVertex shader compilation failed");
            justForgeLogger.FORGE_LOG_ERROR(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }


        justForgeLogger.FORGE_LOG_DEBUG("Compiling Default Fragment Shader");
        // First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);


        // Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            justForgeLogger.FORGE_LOG_ERROR(filePath + "\tFragment shader compilation failed");
            justForgeLogger.FORGE_LOG_ERROR(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }

        // - - - Link Shaders

        justForgeLogger.FORGE_LOG_DEBUG("Linking shaders");
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            justForgeLogger.FORGE_LOG_ERROR(filePath + "\tLinking of shaders failed.");
            justForgeLogger.FORGE_LOG_ERROR(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    public void detach()
    {
        glUseProgram(0);
    }

    public void use()
    {
        glUseProgram(shaderProgramID);
    }
}
