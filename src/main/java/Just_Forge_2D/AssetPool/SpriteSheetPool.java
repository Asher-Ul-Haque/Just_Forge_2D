package Just_Forge_2D.AssetPool;

import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.Utils.Logger;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpriteSheetPool
{
    private static final Map<String, String> nameToFile = new HashMap<>();
    private static final Map<String, SpriteSheet> spriteSheetPool = new HashMap<>();


    // - - - add spritesheet
    public static void addSpriteSheet(String NAME, SpriteSheet SHEET)
    {
        File file = new File(SHEET.getTexture().getFilepath());
        if (!spriteSheetPool.containsKey(file.getAbsolutePath()))
        {
            nameToFile.put(NAME, file.getAbsolutePath());
            spriteSheetPool.put(file.getAbsolutePath(), SHEET);
            Logger.FORGE_LOG_DEBUG("Sprite Sheet with Path: " + file.getAbsolutePath() + " Hashed in Sprite Sheet Pool");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Sprite Sheet at: " + file.getAbsolutePath() + " already exists in the Sprite Sheet Pool");
        }
    }

    // - - - get spritesheet
    public static SpriteSheet getSpriteSheet(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (!nameToFile.containsKey(NAME) || !spriteSheetPool.containsKey(path))
        {
            Logger.FORGE_LOG_ERROR("Sprite sheet : " + NAME + " does not exist");
            return null;
        }
        return spriteSheetPool.get(path);
    }

    // - - - remove spritesheet
    public static void removeSpriteSheet(String NAME)
    {
        String path = nameToFile.get(NAME);
        if (nameToFile.containsKey(NAME) && spriteSheetPool.containsKey(path))
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

    public static void removeSpriteSheet(SpriteSheet SHEET)
    {
        if (spriteSheetPool.containsValue(SHEET))
        {
            spriteSheetPool.remove(SHEET.getTexture().getFilepath());
            Logger.FORGE_LOG_DEBUG("Sprite Sheet: " + SHEET + " removed from AssetPool");
        }
        else
        {
            Logger.FORGE_LOG_WARNING("Sprite Sheet: " + SHEET + " doesn't exist in AssetPool");
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
}
