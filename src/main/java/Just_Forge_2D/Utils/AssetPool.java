package Just_Forge_2D.Utils;

import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Renderer.Shader;
import Just_Forge_2D.Renderer.Texture;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


// - - - Class to store references to all our assets to protect them from the garbage collection
public class AssetPool
{
    // - - - private variable maps for all types of assets
    private static final Map<String, Shader> shaderPool = new HashMap<>();
    private static final Map<String, Texture> texturePool = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheetPool = new HashMap<>();


    // - - - Functions - - -

    // - - - shader
    public static Shader getShader(String FILE_PATH)
    {
        File file = new File(FILE_PATH);

        if (AssetPool.shaderPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_TRACE("Loaded shader: " + FILE_PATH);
            return AssetPool.shaderPool.get(file.getAbsolutePath());
        }
        else
        {
            Shader shader = new Shader(FILE_PATH);
            shader.compile();
            AssetPool.shaderPool.put(file.getAbsolutePath(), shader);
            Logger.FORGE_LOG_DEBUG("Shader with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            return shader;
        }
    }

    // - - - texture
    public static Texture getTexture(String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (AssetPool.texturePool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_TRACE("Loaded texture: " + FILE_PATH);
            return AssetPool.texturePool.get(file.getAbsolutePath());
        }
        else
        {
            Texture texture = new Texture();
            texture.init(FILE_PATH);
            AssetPool.texturePool.put(file.getAbsolutePath(), texture);
            Logger.FORGE_LOG_DEBUG("Texture with path: " + FILE_PATH + " Hashed in Texture Asset Pool and loaded");
            return texture;
        }
    }


    // - - - sprite sheets - - -

    public static void addSpriteSheet(String FILE_PATH, SpriteSheet SPRITE_SHEET)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            AssetPool.spriteSheetPool.put(file.getAbsolutePath(), SPRITE_SHEET);
        }
    }

    public static SpriteSheet getSpriteSheet(String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_ERROR("Tried to access a spritesheet : " + FILE_PATH + " before adding to the resource pool");
            assert false;
        }
        return AssetPool.spriteSheetPool.get(file.getAbsolutePath());
    }
}
