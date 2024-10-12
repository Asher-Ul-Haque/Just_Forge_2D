package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHierarchyWindow
{

    private static final Map<Class<? extends Component>, Boolean> componentFilters = new HashMap<>();
    private static String nameFilter = "";
    private static List<GameObject> gameObjectList;

//    private static boolean selectedAll = false;
//    private static boolean deselectedAll = false;

    public static void editorGUI()
    {
        ImGui.begin("Scene Hierarchy");

        nameFilter = Widgets.inputText("Filter by name", nameFilter);


        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        ImGui.text("Filter by components:");
        ImGui.beginChild("##ComponentFilterChild", ImGui.getContentRegionAvailX(), 200, true);
        Theme.resetDefaultTextColor();

        /*if (ImGui.button("Select All"))
        {
            for (Class<? extends Component> componentClass : ComponentList.types)
            {
                componentFilters.put(componentClass, true);
            }
            selectedAll = true;
            deselectedAll = false;
        }
        ImGui.sameLine();
        if (ImGui.button("Deselect All"))
        {
            for (Class<? extends Component> componentClass : ComponentList.types)
            {
                componentFilters.put(componentClass, false);
            }
            selectedAll = false;
            deselectedAll = true;
        }*/
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        for (Class<? extends Component> componentClass : ComponentList.types)
        {
            boolean isSelected = componentFilters.getOrDefault(componentClass, false);
            if (ImGui.checkbox(componentClass.getSimpleName(), isSelected))
            {
                componentFilters.put(componentClass, !isSelected);
            }
        }

        ImGui.endChild();

        List<Class<? extends Component>> selectedComponents = new ArrayList<>();
        for (Map.Entry<Class<? extends Component>, Boolean> entry : componentFilters.entrySet())
        {
            if (entry.getValue())
            {
                selectedComponents.add(entry.getKey());
            }
        }

        if (selectedComponents.isEmpty() && nameFilter.isEmpty())
        {
            gameObjectList = GameWindow.getCurrentScene().getGameObjects();
        }
        else if (selectedComponents.isEmpty())
        {
            gameObjectList = GameWindow.getCurrentScene().getGameObjects(nameFilter);
        }
        else if (nameFilter.isEmpty())
        {
            gameObjectList = GameWindow.getCurrentScene().getGameObjects(selectedComponents);
        }
        else
        {
            gameObjectList = GameWindow.getCurrentScene().getGameObjects(selectedComponents, nameFilter);
        }

        Theme.resetDefaultTextColor();


        for (int i = 0; i < gameObjectList.size(); ++i)
        {
            GameObject obj = gameObjectList.get(i);
            if (!obj.getSerializationStatus())
            {
                continue;
            }
            ImGui.setCursorPosX(EditorSystemManager.getCurrentTheme().framePadding.x);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
//            boolean isSelected = false;
//            Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
//            ImGui.checkbox("##" + obj.name, isSelected);
//            Theme.resetDefaultTextColor();
//            ImGui.sameLine();
            if (ImGui.button(obj.name + "##" + i))
            {
                Logger.FORGE_LOG_TRACE("Selected Object: " + obj.name);
                ComponentsWindow.clearSelection();
                ComponentsWindow.setActiveGameObject(obj);
            }
        }
        ImGui.end();
    }

    public static List<GameObject> gameObjectList()
    {
        return gameObjectList;
    }
}