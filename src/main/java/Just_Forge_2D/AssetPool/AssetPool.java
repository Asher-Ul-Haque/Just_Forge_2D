package Just_Forge_2D.AssetPool;

import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.EditorSystem.Forge;
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
    public static String message = "";


    // - - - Functions - - -

    // - - - shader - - -

    public static boolean hasShader(String NAME)
    {
        return nameToFileShader.containsKey(NAME);
    }

    public static boolean hasShaderFile(String FILE_PATH)
    {
        return AssetPool.shaderPool.containsKey(FILE_PATH) || AssetPool.shaderPool.containsKey(getRelativeFilePath(FILE_PATH));
    }

    private static void shaderAdder(String NAME, String FILE_PATH)
    {
        if (!hasShaderFile(FILE_PATH) && !hasShader(NAME))
        {
            Logger.FORGE_LOG_DEBUG("Shader with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            Shader shader = new Shader(FILE_PATH);
            shader.compile();
            nameToFileShader.put(NAME, FILE_PATH);
            AssetPool.shaderPool.put(FILE_PATH, shader);
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

    public static void addShader(String NAME, String FILE_PATH, boolean ABSOLUTE)
    {
        if (!checkFileExistence(FILE_PATH, ABSOLUTE))
        {
            message = "No such shader file (.glsl with both fragment and vertex shader) exists : " + FILE_PATH;
            Logger.FORGE_LOG_ERROR(message);
            return;
        }
        if (ABSOLUTE) shaderAdder(NAME, FILE_PATH);
        else shaderAdder(NAME, getRelativeFilePath(FILE_PATH));
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

    public static boolean hasTexture(String NAME)
    {
        return nameToFileTextures.containsKey(NAME);
    }

    public static boolean hasTextureFile(String FILE_PATH)
    {
        return AssetPool.texturePool.containsKey(FILE_PATH) || AssetPool.texturePool.containsKey(getRelativeFilePath(FILE_PATH));
    }

    private static void textureAdder(String NAME, String FILE_PATH)
    {
        if (!hasTextureFile(FILE_PATH) && !hasTexture(NAME))
        {
            Logger.FORGE_LOG_DEBUG("Texture with path: " + FILE_PATH + " Hashed in shader Asset Pool and loaded");
            Texture texture = new Texture();
            if (texture.init(FILE_PATH))
            {
                AssetPool.nameToFileTextures.put(NAME, FILE_PATH);
                AssetPool.texturePool.put(FILE_PATH, texture);
            }
            else
            {
                message = "Bad Texture : " + NAME + " at " + FILE_PATH;
                Logger.FORGE_LOG_ERROR(message);
            }
        }
        else
        {
            message = "Texture at: " + FILE_PATH + " already exists in the shader pool";
            Logger.FORGE_LOG_WARNING(message);
        }
    }

    public static void addTexture(String NAME, String FILE_PATH)
    {
        addTexture(NAME, FILE_PATH, false);
    }

    public static void addTexture(String NAME, String FILE_PATH, boolean ABSOLUTE)
    {
        if (!checkFileExistence(FILE_PATH, ABSOLUTE))
        {
            message = "No such texture file exists : " + FILE_PATH;
            Logger.FORGE_LOG_ERROR(message);
            return;
        }
        if (ABSOLUTE) textureAdder(NAME, FILE_PATH);
        else textureAdder(NAME, getRelativeFilePath(FILE_PATH));
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
        return makeTexture(FILE_PATH, true);
    }

    public static Texture makeTexture(String FILE_PATH, boolean ABSOLUTE)
    {
        if (AssetPool.texturePool.containsKey(FILE_PATH))
        {
            return AssetPool.texturePool.get(FILE_PATH);
        }
        if (AssetPool.texturePool.containsKey(getRelativeFilePath(FILE_PATH)))
        {
            return AssetPool.texturePool.get(getRelativeFilePath(FILE_PATH));
        }
        else
        {
            addTexture(FILE_PATH, FILE_PATH, ABSOLUTE);
            return getTexture(FILE_PATH);
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

    public static boolean hasSpriteSheet(String NAME)
    {
        return nameToFileSpriteSheet.containsKey(NAME);
    }

    public static boolean hasSpriteSheetFile(String FILE_PATH)
    {
        return AssetPool.spriteSheetPool.containsKey(FILE_PATH) || AssetPool.spriteSheetPool.containsKey(getRelativeFilePath(FILE_PATH));
    }

    private static void spriteSheetAdder(String NAME, SpriteSheet SPRITE_SHEET, String FILE_PATH)
    {
        if (!hasSpriteSheetFile(FILE_PATH) && !hasSpriteSheet(NAME))
        {
            nameToFileSpriteSheet.put(NAME, FILE_PATH);
            AssetPool.spriteSheetPool.put(FILE_PATH, SPRITE_SHEET);
            Logger.FORGE_LOG_DEBUG("Sprite sheet with path: " + FILE_PATH + " Hashed in Asset Pool and loaded");
        }
        else
        {
            message = "Sprite Sheet at: " + FILE_PATH + " already exists in Asset Pool";
            Logger.FORGE_LOG_WARNING(message);
        }
    }

    public static void addSpriteSheet(String NAME, SpriteSheet SPRITE_SHEET)
    {
        addSpriteSheet(NAME, SPRITE_SHEET, false);
    }

    public static void addSpriteSheet(String NAME, SpriteSheet SPRITE_SHEET, boolean ABSOLUTE)
    {
        if (!checkFileExistence(SPRITE_SHEET.getTexture().getFilepath(), ABSOLUTE))
        {
            message = "No such texture (sprite sheet) file exists : " + SPRITE_SHEET.getTexture().getFilepath();
            Logger.FORGE_LOG_ERROR(message);
            return;
        }
        if (ABSOLUTE) spriteSheetAdder(NAME, SPRITE_SHEET, SPRITE_SHEET.getTexture().getFilepath());
        else spriteSheetAdder(NAME, SPRITE_SHEET, getRelativeFilePath(SPRITE_SHEET.getTexture().getFilepath()));
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

    public static boolean hasSound(String NAME)
    {
        return nameToFileSounds.containsKey(NAME);
    }

    public static boolean hasSoundFile(String FILE_PATH)
    {
        return AssetPool.soundPool.containsKey(FILE_PATH) || AssetPool.soundPool.containsKey(getRelativeFilePath(FILE_PATH));
    }

    private static void soundAdder(String NAME, String FILE_PATH, boolean DOES_LOOP)
    {
        if (!hasSound(NAME) && !hasSoundFile(FILE_PATH))
        {
            Logger.FORGE_LOG_DEBUG("Sound with path: " + FILE_PATH + " Hashed in Asset Pool and loaded");
            Sound sound = new Sound(FILE_PATH, DOES_LOOP);
            if (!sound.valid)
            {
                message = "Bad Sound : " + NAME + " at " + FILE_PATH;
                Logger.FORGE_LOG_ERROR(message);
                return;
            }
            nameToFileSounds.put(NAME, FILE_PATH);
            AssetPool.soundPool.put(FILE_PATH, sound);
        }
    }

    public static void addSound(String NAME, String FILE_PATH, boolean DOES_LOOP, boolean ABSOLUTE)
    {
        if (!checkFileExistence(FILE_PATH, ABSOLUTE))
        {
            message = "No such sound file exists : " + FILE_PATH;
            Logger.FORGE_LOG_ERROR(message);
            return;
        }

        if (ABSOLUTE) soundAdder(NAME, FILE_PATH, DOES_LOOP);
        else soundAdder(NAME, getRelativeFilePath(FILE_PATH), DOES_LOOP);
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

    public static void reloadAssets()
    {
        for (Texture t : texturePool.values())
        {
            t.init(t.getFilepath());
        }
        for (SpriteSheet s : spriteSheetPool.values())
        {
            s.getTexture().init(s.getTexture().getFilepath());
        }
        for (Map.Entry<String, Sound> entry : soundPool.entrySet())
        {
            Sound sound = entry.getValue();
            entry.setValue(new Sound(sound.getFilePath(), sound.loops()));
        }
        for (Map.Entry<String, Shader> entry : shaderPool.entrySet())
        {
            Shader shader = entry.getValue();
            shader.reload();
        }
    }

    public static boolean checkFileExistence(String FILE_PATH, boolean ABSOLUTE)
    {
        if (ABSOLUTE)
        {
            return new File(FILE_PATH).exists();
        }
        else
        {
            String relativePath = getRelativeFilePath(FILE_PATH);
            return new File(System.getProperty("user.dir", relativePath)).exists();
        }
    }

    public static String getRelativeFilePath(String FILE_PATH)
    {
        String projectDir = Forge.projectDir + "/";
        return FILE_PATH.replace(projectDir, "");
    }

    public static boolean isAbsolutePath(String FILE_PATH)
    {
        return FILE_PATH.equals(new File(FILE_PATH).getAbsolutePath());
    }
}