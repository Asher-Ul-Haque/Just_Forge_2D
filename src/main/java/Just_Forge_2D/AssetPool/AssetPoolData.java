package Just_Forge_2D.AssetPool;

import Just_Forge_2D.AudioSystem.Sound;
import Just_Forge_2D.RenderingSystem.Shader;
import Just_Forge_2D.RenderingSystem.SpriteSheet;
import Just_Forge_2D.RenderingSystem.Texture;

import java.util.Map;

class AssetPoolData
{
    Map<String, String> nameToFileTextures;
    Map<String, String> nameToFileSpriteSheet;
    Map<String, String> nameToFileShader;
    Map<String, String> nameToFileSounds;
    Map<String, Shader> shaderPool;
    Map<String, Texture> texturePool;
    Map<String, SpriteSheet> spriteSheetPool;
    Map<String, Sound> soundPool;

    // Constructor
    public AssetPoolData(Map<String, String> nameToFileTextures, Map<String, String> nameToFileSpriteSheet,
                         Map<String, String> nameToFileShader, Map<String, String> nameToFileSounds,
                         Map<String, Shader> shaderPool, Map<String, Texture> texturePool,
                         Map<String, SpriteSheet> spriteSheetPool, Map<String, Sound> soundPool) {
        this.nameToFileTextures = nameToFileTextures;
        this.nameToFileSpriteSheet = nameToFileSpriteSheet;
        this.nameToFileShader = nameToFileShader;
        this.nameToFileSounds = nameToFileSounds;
        this.shaderPool = shaderPool;
        this.texturePool = texturePool;
        this.spriteSheetPool = spriteSheetPool;
        this.soundPool = soundPool;
    }
}
