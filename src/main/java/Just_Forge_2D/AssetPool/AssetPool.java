package Just_Forge_2D.AssetPool;

import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.Logger;

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
        File file = new File(EditorSystemManager.projectDir + "/Assets/Shaders/" + FILE_PATH);
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

    public static void removeShader(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (nameToFile.containsKey(NAME) && AssetPool.shaderPool.containsKey(path))
        {
            shaderPool.remove(path);
            nameToFile.remove(NAME);
            Logger.FORGE_LOG_DEBUG("Shader: " + NAME + " removed from AssetPool");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Shader: " + NAME + " doesn't exist in AssetPool");
        }
    }

    public static void clearShaderPool()
    {
        Logger.FORGE_LOG_WARNING("Clearing Shader Pool");
        shaderPool.clear();
    }


    // - - - texture - - -

    public static void addTexture(String NAME, String FILE_PATH)
    {
        File file = new File(EditorSystemManager.projectDir + "/Assets/Textures/" + FILE_PATH);
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

    public static void addSpriteSheet(String NAME, SpriteSheet SPRITE_SHEET)
    {
        File file = new File(EditorSystemManager.projectDir + "/Assets/Textures/" + SPRITE_SHEET.getTexture().getFilepath());
        if (!AssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            nameToFile.put(NAME, file.getAbsolutePath());
            AssetPool.spriteSheetPool.put(file.getAbsolutePath(), SPRITE_SHEET);
            Logger.FORGE_LOG_DEBUG("Sprite sheet with path: " + file.getAbsolutePath() + " Hashed in Asset Pool and loaded");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Sprite Sheet at: " + file.getAbsolutePath() + " already exists in Asset Pool");
        }
    }

    public static SpriteSheet getSpriteSheet(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (!nameToFile.containsKey(NAME) || !AssetPool.spriteSheetPool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Sprite sheet : " + NAME + " does not exist");
            return null;
        }
        return AssetPool.spriteSheetPool.get(path);
    }

    public static void removeSpriteSheet(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (nameToFile.containsKey(NAME) && AssetPool.spriteSheetPool.containsKey(path))
        {
            spriteSheetPool.remove(path);
            nameToFile.remove(NAME);
            Logger.FORGE_LOG_DEBUG("Sprite Sheet: " + NAME + " removed from AssetPool");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Sprite Sheet: " + NAME + " doesn't exist in AssetPool");
        }
    }

    public static void clearSpriteSheetPool()
    {
        Logger.FORGE_LOG_WARNING("Clearing Sprite Sheet Pool");
        spriteSheetPool.clear();
    }

    public static Collection<SpriteSheet> getAllSpriteSheets()
    {
        return spriteSheetPool.values();
    }


    // - - - Sounds - - -

    public static void addSound(String NAME, String FILE_PATH, boolean DOES_LOOP)
    {
        File file = new File(EditorSystemManager.projectDir + "/Assets/Sounds/" + FILE_PATH);
        if (!AssetPool.soundPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_DEBUG("Sound with path: " + FILE_PATH + " Hashed in Asset Pool and loaded");
            Sound sound = new Sound(file.getAbsolutePath(), DOES_LOOP);
            nameToFile.put(NAME, file.getAbsolutePath());
            AssetPool.soundPool.put(file.getAbsolutePath(), sound);
        }
    }

    public static Sound getSound(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (!nameToFile.containsKey(NAME) || !AssetPool.soundPool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Sound: " + NAME + " does not exist");
            return null;
        }
        return AssetPool.soundPool.get(path);
    }

    public static void removeSound(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (nameToFile.containsKey(NAME) && AssetPool.soundPool.containsKey(path))
        {
            soundPool.get(path).delete();
            soundPool.remove(path);
            nameToFile.remove(NAME);
            Logger.FORGE_LOG_DEBUG("Sound: " + NAME + " removed from AssetPool");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Sound: " + NAME + " doesn't exist in AssetPool");
        }
    }

    public static void clearSoundPool()
    {
        Logger.FORGE_LOG_WARNING("Clearing Sound Pool");
        soundPool.clear();
    }

    public static Collection<Sound> getAllSounds()
    {
        return soundPool.values();
    }

    public static Map<String, Sound> getSoundMap() { return soundPool; };
}