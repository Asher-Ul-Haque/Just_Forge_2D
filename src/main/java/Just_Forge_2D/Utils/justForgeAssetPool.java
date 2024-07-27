package Just_Forge_2D.Utils;

import Just_Forge_2D.Core.ECS.Components.Sprite.justForgeSpriteSheet;
import Just_Forge_2D.Renderer.justForgeShader;
import Just_Forge_2D.Renderer.justForgeTexture;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


// - - - Class to store references to all our assets to protect them from the garbage collection
public class justForgeAssetPool
{
    // - - - private variable maps for all types of assets
    private static Map<String, justForgeShader> shaderPool = new HashMap<>();
    private static Map<String, justForgeTexture> texturePool = new HashMap<>();
    private static Map<String, justForgeSpriteSheet> spriteSheetPool = new HashMap<>();


    // - - - Functions - - -

    // - - - shader
    public static justForgeShader getShader(String FILE_PATH)
    {
        File file = new File(FILE_PATH);

        if (justForgeAssetPool.shaderPool.containsKey(file.getAbsolutePath()))
        {
            justForgeLogger.FORGE_LOG_TRACE("Loaded shader: " + FILE_PATH);
            return justForgeAssetPool.shaderPool.get(file.getAbsolutePath());
        }
        else
        {
            justForgeShader shader = new justForgeShader(FILE_PATH);
            shader.compile();
            justForgeAssetPool.shaderPool.put(file.getAbsolutePath(), shader);
            justForgeLogger.FORGE_LOG_DEBUG("Shader with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            return shader;
        }
    }

    // - - - texture
    public static justForgeTexture getTexture(String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (justForgeAssetPool.texturePool.containsKey(file.getAbsolutePath()))
        {
            justForgeLogger.FORGE_LOG_TRACE("Loaded texture: " + FILE_PATH);
            return justForgeAssetPool.texturePool.get(file.getAbsolutePath());
        }
        else
        {
            justForgeTexture texture = new justForgeTexture();
            texture.init(FILE_PATH);
            justForgeAssetPool.texturePool.put(file.getAbsolutePath(), texture);
            justForgeLogger.FORGE_LOG_DEBUG("Texture with path: " + FILE_PATH + " Hashed in Texture Asset Pool and loaded");
            return texture;
        }
    }


    // - - - sprite sheets - - -

    public static void addSpriteSheet(String FILE_PATH, justForgeSpriteSheet SPRITE_SHEET)
    {
        File file = new File(FILE_PATH);
        if (!justForgeAssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            justForgeAssetPool.spriteSheetPool.put(file.getAbsolutePath(), SPRITE_SHEET);
        }
    }

    public static justForgeSpriteSheet getSpriteSheet(String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!justForgeAssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            justForgeLogger.FORGE_LOG_ERROR("Tried to access a spritesheet : " + FILE_PATH + " before adding to the resource pool");
            assert false;
        }
        return justForgeAssetPool.spriteSheetPool.get(file.getAbsolutePath());
    }
}
