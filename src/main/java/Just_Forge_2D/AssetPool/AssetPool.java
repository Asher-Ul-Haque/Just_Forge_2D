package Just_Forge_2D.AssetPool;

import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// - - - Class to store references to all our assets to protect them from the garbage collection
public class AssetPool
{
    // - - - private variable maps for all types of assets
    protected static final Map<String, String> nameToFileTextures = new HashMap<>();
    protected static final Map<String, String> nameToFileSpriteSheet = new HashMap<>();
    protected static final Map<String, String> nameToFileShader = new HashMap<>();
    protected static final Map<String, String> nameToFileSounds = new HashMap<>();
    protected static final Map<String, Shader> shaderPool = new HashMap<>();
    protected static final Map<String, Texture> texturePool = new HashMap<>();
    protected static final Map<String, SpriteSheet> spriteSheetPool = new HashMap<>();
    protected static final Map<String, Sound> soundPool = new HashMap<>();


    // - - - Functions - - -

    // - - - shader - - -

    private static void shaderAdder(String NAME, String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.shaderPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_DEBUG("Shader with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            Shader shader = new Shader(FILE_PATH);
            shader.compile();
            nameToFileShader.put(NAME, file.getAbsolutePath());
            AssetPool.shaderPool.put(file.getAbsolutePath(), shader);
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Shader at: " + FILE_PATH + " already exists in the asset pool");
        }
    }

    public static void addShader(String NAME, String FILE_PATH)
    {
        addShader(NAME, FILE_PATH, false);
    }

    public static void addShader(String NAME, String FILE_PATH, boolean ABSOLUTE_PATH)
    {
        if (ABSOLUTE_PATH) shaderAdder(NAME, FILE_PATH);
        else shaderAdder(NAME, EditorSystemManager.projectDir + "/Assets/Shaders/" + FILE_PATH);
    }

    public static Shader getShader(String NAME)
    {
        String path = nameToFileShader.get(NAME);
        if (!nameToFileShader.containsKey(NAME) || !AssetPool.shaderPool.containsKey(path))
        {

            Logger.FORGE_LOG_ERROR("Shader : " + NAME + " does not exist");
            return null;
        }
        Logger.FORGE_LOG_TRACE("Loaded shader: " + NAME);
        return AssetPool.shaderPool.get(path);
    }

    public static void removeShader(String NAME)
    {
        String path = nameToFileShader.get(NAME);
        if (nameToFileShader.containsKey(NAME) && AssetPool.shaderPool.containsKey(path))
        {
            shaderPool.remove(path);
            nameToFileShader.remove(NAME);
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
        nameToFileShader.clear();
        shaderPool.clear();
    }


    // - - - texture - - -

    private static void textureAdder(String NAME, String FILE_PATH)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.texturePool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_DEBUG("Texture with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            Texture texture = new Texture();
            if (texture.init(FILE_PATH))
            {
                nameToFileTextures.put(NAME, file.getAbsolutePath());
                AssetPool.texturePool.put(file.getAbsolutePath(), texture);
            }
            else
            {
                Logger.FORGE_LOG_ERROR("Bad Texture : " + NAME);
            }
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Texture at: " + FILE_PATH + " already exists in the shader pool");
        }
    }

    public static void addTexture(String NAME, String FILE_PATH)
    {
        addTexture(NAME, FILE_PATH, false);
    }

    public static void addTexture(String NAME, String FILE_PATH, boolean ABSOLUTE)
    {
        if (ABSOLUTE) textureAdder(NAME, FILE_PATH);
        else textureAdder(NAME, EditorSystemManager.projectDir + "/Assets/Textures/" + FILE_PATH);
    }

    public static Texture getTexture(String NAME)
    {
        String path = nameToFileTextures.get(NAME);
        if (!nameToFileTextures.containsKey(NAME) || !AssetPool.texturePool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Texture : " + NAME + " does not exist");
            return null;
        }
        return AssetPool.texturePool.get(path);
    }

    public static Texture makeTexture(String FILE_PATH)
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
            if (texture.init(FILE_PATH)) return texture;
            else
            {
                Logger.FORGE_LOG_WARNING("Bad Texture at : " + FILE_PATH);
                return null;
            }
        }
    }

    public static void removeTexture(String NAME)
    {
        String path = nameToFileTextures.get(NAME);
        if (nameToFileTextures.containsKey(NAME) && AssetPool.texturePool.containsKey(path))
        {
            texturePool.remove(path);
            nameToFileTextures.remove(NAME);
            Logger.FORGE_LOG_DEBUG("Texture: " + NAME + " removed from AssetPool");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Texture: " + NAME + " doesn't exist in AssetPool");
        }
    }

    public static void clearTexturePool()
    {
        Logger.FORGE_LOG_WARNING("Clearing Texture Pool");
        nameToFileTextures.clear();
        texturePool.clear();
    }

    public static List<Texture> getAllTextures()
    {
        return new ArrayList<>(texturePool.values());
    }

    public static List<String> getTextureNames()
    {
        return new ArrayList<>(nameToFileTextures.keySet());
    }


    // - - - sprite sheets - - -

    private static void spriteSheetAdder(String NAME, SpriteSheet SPRITE_SHEET, String PATH)
    {
        File file = new File(PATH);
        if (!AssetPool.spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            nameToFileSpriteSheet.put(NAME, file.getAbsolutePath());
            AssetPool.spriteSheetPool.put(file.getAbsolutePath(), SPRITE_SHEET);
            Logger.FORGE_LOG_DEBUG("Sprite sheet with path: " + file.getAbsolutePath() + " Hashed in Asset Pool and loaded");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Sprite Sheet at: " + file.getAbsolutePath() + " already exists in Asset Pool");
        }
    }

    public static void addSpriteSheet(String NAME, SpriteSheet SPRITE_SHEET)
    {
        addSpriteSheet(NAME, SPRITE_SHEET, false);
    }

    public static void addSpriteSheet(String NAME, SpriteSheet SPRITE_SHEET, boolean ABSOLUTE)
    {
        if (ABSOLUTE) spriteSheetAdder(NAME, SPRITE_SHEET, SPRITE_SHEET.getTexture().getFilepath());
        else spriteSheetAdder(NAME, SPRITE_SHEET, EditorSystemManager.projectDir + "/Assets/Textures/" + SPRITE_SHEET.getTexture().getFilepath());
    }

    public static SpriteSheet getSpriteSheet(String NAME)
    {
        String path = nameToFileSpriteSheet.get(NAME);
        if (!nameToFileSpriteSheet.containsKey(NAME) || !AssetPool.spriteSheetPool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Sprite sheet : " + NAME + " does not exist");
            return null;
        }
        return AssetPool.spriteSheetPool.get(path);
    }

    public static void removeSpriteSheet(String NAME)
    {
        String path = nameToFileSpriteSheet.get(NAME);
        if (nameToFileSpriteSheet.containsKey(NAME) && AssetPool.spriteSheetPool.containsKey(path))
        {
            spriteSheetPool.remove(path);
            nameToFileSpriteSheet.remove(NAME);
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
        nameToFileSpriteSheet.clear();
        spriteSheetPool.clear();
    }

    public static List<SpriteSheet> getAllSpriteSheets()
    {
        return new ArrayList<>(spriteSheetPool.values());
    }

    public static List<String> getSpriteSheetNames() {return new ArrayList<>(nameToFileSpriteSheet.keySet());}

    public static List<String> getSpriteSheetPaths() {return new ArrayList<>(nameToFileSpriteSheet.values());}


    // - - - Sounds - - -

    private static void soundAdder(String NAME, String FILE_PATH, boolean DOES_LOOP)
    {
        File file = new File(FILE_PATH);
        if (!AssetPool.soundPool.containsKey(file.getAbsolutePath()))
        {
            Logger.FORGE_LOG_DEBUG("Sound with path: " + FILE_PATH + " Hashed in Asset Pool and loaded");
            Sound sound = new Sound(file.getAbsolutePath(), DOES_LOOP);
            if (!sound.valid)
            {
                Logger.FORGE_LOG_ERROR("Bad Sound : " + NAME);
                return;
            }
            nameToFileSounds.put(NAME, file.getAbsolutePath());
            AssetPool.soundPool.put(file.getAbsolutePath(), sound);
        }
    }

    public static void addSound(String NAME, String FILE_PATH, boolean DOES_LOOP, boolean ABSOLUTE)
    {
        if (ABSOLUTE) soundAdder(NAME, FILE_PATH, DOES_LOOP);
        else soundAdder(NAME, EditorSystemManager.projectDir + "/Assets/Sounds/" + FILE_PATH, DOES_LOOP);
    }

    public static void addSound(String NAME, String FILE_PATH, boolean DOES_LOOP)
    {
        addSound(NAME, FILE_PATH, DOES_LOOP, false);
    }

    public static Sound getSound(String NAME)
    {
        String path = nameToFileSounds.get(NAME);
        if (!nameToFileSounds.containsKey(NAME) || !AssetPool.soundPool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Sound: " + NAME + " does not exist");
            return null;
        }
        return AssetPool.soundPool.get(path);
    }

    public static void removeSound(String NAME)
    {
        String path = nameToFileSounds.get(NAME);
        if (nameToFileSounds.containsKey(NAME) && AssetPool.soundPool.containsKey(path))
        {
            soundPool.get(path).delete();
            soundPool.remove(path);
            nameToFileSounds.remove(NAME);
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
        nameToFileSounds.clear();
        soundPool.clear();
    }

    public static List<Sound> getAllSounds()
    {
        return new ArrayList<>(soundPool.values());
    }

    public static List<String> getAllSoundNames()
    {
        return new ArrayList<>(nameToFileSounds.keySet());
    }
}