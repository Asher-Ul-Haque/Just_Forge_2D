package Just_Forge_2D.AssetPool;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Just_Forge_2D.AssetPool.AssetPool.*;

public class AssetPoolSerializer
{
    private static String serialize()
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create();
        AssetPoolData data = new AssetPoolData(nameToFileTextures, nameToFileSpriteSheet, nameToFileShader,
                nameToFileSounds, shaderPool, texturePool, spriteSheetPool, soundPool);
        return gson.toJson(data);
    }

    private static void deserialize(String json)
    {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        AssetPoolData data = gson.fromJson(json, AssetPoolData.class);

        // - - - Populate from deserialized data
        data.nameToFileTextures.forEach((name, filePath) ->
        {
            if (new File(filePath).exists())
            {
                addTexture(name, filePath, true);
            }
            else
            {
                Logger.FORGE_LOG_WARNING("Texture file not found: " + filePath);
            }
        });

        data.nameToFileSpriteSheet.forEach((name, filePath) ->
        {
            SpriteSheet sheet = data.spriteSheetPool.get(filePath);

            Texture t = AssetPool.makeTexture(filePath);
            t.init(filePath);
            SpriteSheet newSheet = new SpriteSheet(t, sheet.getWidth(), sheet.getHeight(), sheet.size(), sheet.getSpacing());
            addSpriteSheet(name, newSheet, true);
        });

        data.nameToFileShader.forEach((name, filePath) ->
        {
            if (new File(filePath).exists())
            {
                addShader(name, filePath, true);
            }
            else
            {
                Logger.FORGE_LOG_WARNING("Shader file not found: " + filePath);
            }
        });

        data.nameToFileSounds.forEach((name, filePath) ->
        {
            if (new File(filePath).exists())
            {
                addSound(name, filePath, true, true);
            }
            else
            {
                Logger.FORGE_LOG_WARNING("LowLevelSound file not found: " + filePath);
            }
        });
    }


    public static void saveAssetPool(String FILE_PATH)
    {
        String json = serialize();
        try
        {
            FileWriter writer = new FileWriter(FILE_PATH);
            writer.write(json);
            writer.close();
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_WARNING(e.getMessage());
        }
    }

    public static void loadAssetPool(String FILE_PATH)
    {
        try
        {
            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
            deserialize(json);
        }
        catch (Exception e)
        {
            Logger.FORGE_LOG_FATAL(e.getMessage());
        }
    }

    public static void loadAssetPool()
    {
        loadAssetPool(EditorSystemManager.projectDir + "/.forge/Pool.justForgeFile");
    }

    public static void saveAssetPool()
    {
        saveAssetPool(EditorSystemManager.projectDir + "/.forge/Pool.justForgeFile");
    }
}