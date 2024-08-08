package Just_Forge_2D.Renderer;

import Just_Forge_2D.Utils.Logger;
import org.joml.*;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;


// - - - SHADER Management
public class Shader
{
    // - - - Private variables for compiling
    private int shaderProgramID;
    private String vertexSource, fragmentSource;
    private final String filePath;
    private boolean beingUsed = false;

    // - - - Constructor to get a usable shader
    public Shader(String FILE_PATH)
    {
        this.filePath = FILE_PATH;

        // - - - Get the file contents to compile it
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
                    Logger.FORGE_LOG_ERROR("Unexpected token: " + firstPattern);
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
                    Logger.FORGE_LOG_ERROR("Unexpected token: " + firstPattern);
                    throw new IOException("Unexpected token: " + firstPattern);
            }

        }
        catch (IOException e)
        {
            Logger.FORGE_LOG_ERROR("Could not open file for shader: " + filePath);
            Logger.FORGE_LOG_ERROR(e.getMessage());
            assert false;
        }
    }

    // - - - Actually compile and link
    public void compile()
    {
        // - - - | Compile and link shaders | - - -

        int vertexID, fragmentID;
        Logger.FORGE_LOG_INFO("Setting up shader: " + this.filePath);

        // - - - First load and compile the vertex shader
        Logger.FORGE_LOG_DEBUG("Compiling Vertex Shader");
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        // - - - Pass the shader source to the GPU
        glShaderSource(vertexID, vertexSource);
        glCompileShader(vertexID);

        // - - - Check for errors in compilation
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            Logger.FORGE_LOG_ERROR("defaultShader.glsl'\n\tVertex shader compilation failed");
            Logger.FORGE_LOG_ERROR(glGetShaderInfoLog(vertexID, len));
            assert false : "";
        }

        Logger.FORGE_LOG_DEBUG("Compiling Fragment Shader");

        // - - - First load and compile the vertex shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        // - - - Pass the shader source to the GPU
        glShaderSource(fragmentID, fragmentSource);
        glCompileShader(fragmentID);

        // - - - Check for errors in compilation
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            Logger.FORGE_LOG_ERROR(filePath + "\tFragment shader compilation failed");
            Logger.FORGE_LOG_ERROR(glGetShaderInfoLog(fragmentID, len));
            assert false : "";
        }


        // - - - Link Shaders - - -

        Logger.FORGE_LOG_DEBUG("Linking shaders");
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // - - - Check for linking errors
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE)
        {
            int len = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            Logger.FORGE_LOG_ERROR(filePath + "\tLinking of shaders failed.");
            Logger.FORGE_LOG_ERROR(glGetProgramInfoLog(shaderProgramID, len));
            assert false : "";
        }
    }

    // - - - Use and fuck - - -

    public void detach()
    {
        beingUsed = false;
        glUseProgram(0);
    }

    public void use()
    {
        if (!beingUsed)
        {
            glUseProgram(shaderProgramID);
            beingUsed = true;
        }
    }


    // - - - Upload to shader - - -

    // - - - Matrix4
    public void uploadMatrix4f(String VARIABLE_NAME, Matrix4f MAT_4)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
        MAT_4.get(matrixBuffer);
        glUniformMatrix4fv(varLocation, false, matrixBuffer);
    }

    // - - - Matrix3
    public void uploadMatrix3f(String VARIABLE_NAME, Matrix3f MAT_3)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(9);
        MAT_3.get(matrixBuffer);
        glUniformMatrix3fv(varLocation, false, matrixBuffer);
    }

    // - - - Vector4
    public void uploadVec4f(String VARIABLE_NAME, Vector4f VEC_4)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform4f(varLocation, VEC_4.x, VEC_4.y, VEC_4.z, VEC_4.w);
    }

    // - - - Vector3
    public void uploadVec3f(String VARIABLE_NAME, Vector3f VEC_3)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform3f(varLocation, VEC_3.x, VEC_3.y, VEC_3.z);
    }

    // - - - Vector2
    public void uploadVec2f(String VARIABLE_NAME, Vector2f VEC_2)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform2f(varLocation, VEC_2.x, VEC_2.y);
    }

    // - - - Float
    public void uploadFloat(String VARIABLE_NAME, float VALUE)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform1f(varLocation, VALUE);
    }

    // - - - Int
    public void uploadInt(String VARIABLE_NAME, int VALUE)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform1i(varLocation, VALUE);
    }

    // - - - Texture
    public void uploadTexture(String VARIABLE_NAME, int SLOT)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform1i(varLocation, SLOT);
    }

    // - - - Many textures
    public void uploadIntArray(String VARIABLE_NAME, int[] ARRAY)
    {
        int varLocation = glGetUniformLocation(shaderProgramID, VARIABLE_NAME);
        use();
        glUniform1iv(varLocation, ARRAY);
    }
}
