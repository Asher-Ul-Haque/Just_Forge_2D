package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.EventSystem.Observer;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import imgui.ImGui;
import imgui.ImVec4;
import imgui.flag.ImGuiCol;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


// - - - Properties Window
public class ComponentsWindow
{
    // - - - private variables
    private static final List<GameObject> activeGameObjects = new ArrayList<>();
    private static final List<Vector4f> activeGameObjectsColors = new ArrayList<>();


    // - - - Functions - - -

    // - - - Update - - -

    public static void render()
    {
        ImGui.begin("Components");
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null)
        {
            GameObject activeGameObject = activeGameObjects.get(0);

            if (ImGui.beginPopupContextWindow("Component Adder"))
            {
                String message = "Add Component";
                ImGui.setCursorPosX((ImGui.getContentRegionAvailX() - ImGui.calcTextSize(message).x)/2f);
                ImGui.text(message);
                for (Class<? extends Component> type : ComponentList.types)
                {
                    if (activeGameObject.getCompoent(type) != null) continue;
                    if (ImGui.menuItem(type.getSimpleName()))
                    {
                        try
                        {
                            Component component = type.getDeclaredConstructor().newInstance();
                            activeGameObject.addComponent(component);
                        }
                        catch (Exception e)
                        {
                            Logger.FORGE_LOG_ERROR("Cant add component : " + type.getSimpleName());
                        }
                    }
                }
                ImGui.endPopup();
            }
            activeGameObject.editorGUI();
        }
        else
        {
            ImGui.text("No Game Object selected");
        }
        ImGui.end();
    }


    // - - - Active Game Objects - - -

    public static GameObject getActiveGameObject()
    {
        return activeGameObjects.size() == 1 ? activeGameObjects.get(0) : null;
    }

    public static List<GameObject> getActiveGameObjects()
    {
        return activeGameObjects;
    }

    public static void clearSelection()
    {
        if (!activeGameObjectsColors.isEmpty())
        {
            int i = 0;
            for (GameObject go: activeGameObjects)
            {
                SpriteComponent spr = go.getCompoent(SpriteComponent.class);
                if (spr != null)
                {
                    spr.setColor(activeGameObjectsColors.get(i));
                }
                ++i;
            }
        }
        activeGameObjects.clear();
        activeGameObjectsColors.clear();
    }

    public static void setActiveGameObject(GameObject GO)
    {
        if (GO != null)
        {
            clearSelection();
            activeGameObjects.add(GO);
        }
    }

    public static void addActiveGameObject(GameObject GO)
    {
        SpriteComponent spr = GO.getCompoent(SpriteComponent.class);
        if (spr != null)
        {
            activeGameObjectsColors.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        }
        else
        {
            activeGameObjectsColors.add(new Vector4f());
        }
        activeGameObjects.add(GO);
    }
}
