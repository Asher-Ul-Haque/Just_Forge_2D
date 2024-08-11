package Just_Forge_2D.Utils;

import Just_Forge_2D.Core.ECS.Components.Sprite.SpriteSheet;
import Just_Forge_2D.Renderer.Shader;
import Just_Forge_2D.Renderer.Texture;
import Just_Forge_2D.Sound.Sound;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


// - - - Class to store references to all our assets to protect them from the garbage collection
public class AssetPool
{
    // - - - private variable maps for all types of assets
    private static final Map<String, String> nameToFile = new HashMap<>();
    private static final Map<String, Shader> shaderPool = new HashMap<>();
    private static final Map<String, Texture> texturePool = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheetPool = new HashMap<>();
    private static final Map<String, Sound> soundPool = new HashMap<>();


    // - - - Functions - - -

    // - - - shader - - -

    public static void addShader(String NAME, String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.shaderPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_DEBUG("Shader with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            Shader shader = new Shader(FILE_PATH);
            shader.compile();
            nameToFile.put(NAME, file.getAbsolutePath());
            AssetPool.shaderPool.put(file.getAbsolutePath(), shader);
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Shader at: " + FILE_PATH + " already exists in the asset pool");
        }
    }

    public static Shader getShader(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (!nameToFile.containsKey(NAME) || !AssetPool.shaderPool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Shader : " + NAME + " does not exist");
            return null;
        }
        Logger.FORGE_LOG_TRACE("Loaded shader: " + NAME);
        return AssetPool.shaderPool.get(path);
    }


    // - - - texture - - -

    public static void addTexture(String NAME, String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.texturePool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_DEBUG("Texture with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            Texture texture = new Texture();
            texture.init(FILE_PATH);
            nameToFile.put(NAME, file.getAbsolutePath());
            AssetPool.texturePool.put(file.getAbsolutePath(), texture);
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Texture at: " + FILE_PATH + " already exists in the shader pool");
        }
    }

    public static Texture getTexture(String FILE_PATH)
    {
        /*
        if (!nameToFile.containsKey(NAME))
        {
            Logger.FORGE_LOG_ERROR("Texture : " + NAME + " does not exist");
            return null;
        }
        String path = nameToFile.get(NAME);
        if (AssetPool.texturePool.containsKey(path))
        {
            Logger.FORGE_LOG_TRACE("Loaded texture: " + NAME);
            return AssetPool.texturePool.get(path);
        }
        else
        {
            Logger.FORGE_LOG_DEBUG("Texture with path: " + path + " Hashed in shader Asset Pool and loaded");
            Shader shader = new Shader(path);
            shader.compile();
            AssetPool.shaderPool.put(NAME, shader);
            return shader;
        }
         */
        File file = new File(FILE_PATH);


        if (AssetPool.texturePool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_TRACE("Loaded texture: " + FILE_PATH);
            return AssetPool.texturePool.get(file.getAbsolutePath());
        }
        else
        {
            Logger.FORGE_LOG_DEBUG("Texture with path: " + FILE_PATH + " Hashed in Texture Asset Pool and loaded");
            Texture texture = new Texture();
            texture.init(FILE_PATH);
            AssetPool.texturePool.put(file.getAbsolutePath(), texture);
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
        else
        {
            Logger.FORGE_LOG_WARNING("Sprite Sheet: " + FILE_PATH + " is already present");
        }
    }

    public static SpriteSheet getSpriteSheet(String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_ERROR("Tried to access a sprite sheet : " + FILE_PATH + " before adding to the resource pool");
            assert false;
        }
        return AssetPool.spriteSheetPool.get(file.getAbsolutePath());
    }


    // - - - Sounds - - -

    public static Sound getSound(String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (soundPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_TRACE("Loaded sound: " + FILE_PATH);
            return soundPool.get(file.getAbsolutePath());
        }
        else
        {
            Logger.FORGE_LOG_ERROR("Sound file not added: " + FILE_PATH);
            assert false;
        }
        return null;
    }

    public static void addSound(String FILE_PATH, boolean DOES_LOOP)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.soundPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_TRACE();
            Sound sound = new Sound(file.getAbsolutePath(), DOES_LOOP);
            AssetPool.soundPool.put(file.getAbsolutePath(), sound);
        }
    }

    public static Collection<Sound> getAllSounds()
    {
        return soundPool.values();
    }
}