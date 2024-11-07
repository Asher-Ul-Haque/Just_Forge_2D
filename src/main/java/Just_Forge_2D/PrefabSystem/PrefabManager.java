package Just_Forge_2D.PrefabSystem;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.InputControls.MouseControlComponent;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.Sprite;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;

import java.util.HashMap;
import java.util.Map;

public class PrefabManager
{
    protected static final Map<String, Prefab> prefabRegistry = new HashMap<>();
    private static int defaultPrefabMask = 0;
    private static boolean showPrefabDeletePopup = false;
    private static boolean showPrefabClearPopup = false;

    public static void registerPrefab(String NAME, Prefab PREFAB)
    {
        prefabRegistry.put(NAME, PREFAB);
    }

    public static void unregisterPrefab(String NAME) {prefabRegistry.remove(NAME);}

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

    public static GameObject generateObject(Sprite SPRITE, float SIZE_X, float SIZE_Y)
    {
        SpritePrefab spritePrefab = new SpritePrefab("Auto Generated " + ++defaultPrefabMask, SPRITE, SIZE_X, SIZE_Y);
        return spritePrefab.create();
    }

    public static GameObject generateObject(float SIZE_X, float SIZE_Y)
    {
        NonSpritePrefab nonSpritePrefab = new NonSpritePrefab("Auto Generated " + ++defaultPrefabMask, SIZE_X, SIZE_Y);
        return nonSpritePrefab.create();
    }

    public static void render()
    {
        ImGui.begin(Icons.Copy + "  Prefabs");
        if (Widgets.button(Icons.Trash + " Delete All"))
        {
            showPrefabClearPopup = !showPrefabClearPopup;
        }
        Widgets.text("");
        if (showPrefabClearPopup)
        {
            switch(Widgets.popUp(Icons.ExclamationTriangle, "Delete Prefab", "Are you sure you want to delete all prefabs"))
            {
                case OK:
                    prefabRegistry.clear();
                    showPrefabClearPopup = false;
                    break;

                case CANCEL:
                    showPrefabClearPopup = false;
                    break;
            }
        }

        for (Map.Entry<String, Prefab> entry : prefabRegistry.entrySet())
        {
            String name = entry.getKey();
            Prefab prefab = entry.getValue();


            if (Widgets.button(Icons.Trash))
            {
                showPrefabDeletePopup = !showPrefabDeletePopup;
            }
            ImGui.sameLine();
            if (Widgets.button(name))
            {
                GameObject obj = prefab.create();
                MouseControlComponent.pickupObject(obj);
            }
            if (showPrefabDeletePopup)
            {
                switch(Widgets.popUp(Icons.ExclamationTriangle, "Delete Prefab", "Are you sure you want to delete prefab\n" + name))
                {
                    case OK:
                        unregisterPrefab(name);
                        showPrefabDeletePopup = false;
                        ImGui.end();
                        return;

                    case CANCEL:
                        showPrefabDeletePopup = false;
                        break;
                }
            }
        }
        ImGui.end();
    }
}