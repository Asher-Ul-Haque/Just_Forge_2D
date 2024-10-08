package Just_Forge_2D.PrefabSystem;

import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;

import java.util.HashMap;
import java.util.Map;

public class PrefabManager
{
    private static final Map<String, Prefab> prefabRegistry = new HashMap<>();
    private static int defaultPrefabMask = 0;

    public static void registerPrefab(String NAME, Prefab PREFAB)
    {
        prefabRegistry.put(NAME, PREFAB);
    }

    public static GameObject createPrefab(String NAME)
    {
        Prefab prefab = prefabRegistry.get(NAME);
        if (prefab != null)
        {
            return prefab.create();
        }
        Logger.FORGE_LOG_ERROR("Prefab not found: " + NAME);
        return null;
    }

    public static GameObject generateDefaultSpriteObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        SpritePrefab spritePrefab = new SpritePrefab("Auto Generated " + ++defaultPrefabMask, SPRITE, SIZE_X, SIZE_Y);
        return  spritePrefab.create();
    }

    public static void render()
    {
        ImGui.begin("Prefabs");

        for (Map.Entry<String, Prefab> entry : prefabRegistry.entrySet())
        {
            String name = entry.getKey();
            Prefab prefab = entry.getValue();

            if (ImGui.button(name))
            {
                GameObject obj = prefab.create();
                MouseControlComponent.pickupObject(obj);
            }
        }

        ImGui.end();
    }

}
