package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.SceneSystem.Camera;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SceneHierarchyWindow
{

    private static final Map<Class<? extends Component>, Boolean> componentFilters = new HashMap<>();
    private static String nameFilter = "";
    private static String newObjectName = "";
    private static boolean showDeathPopup = false;
    private static boolean showAddPopup = false;
    private static boolean showSingleDeathPopup = false;

    private static List<GameObject> gameObjectList;

    public static void editorGUI()
    {
        ImGui.begin(Icons.Video + "  Scene Hierarchy Window");

        ImGui.columns(2);
        if (Widgets.button(Icons.Trash + " Delete All")) {showDeathPopup = !showDeathPopup;}

        if (showDeathPopup)
        {
            if (!Widgets.popUp(Icons.Trash, "Confirm Delete", "Are you sure you want to delete all game objects?").equals(Widgets.PopupReturn.NO_INPUT))
            {
                EventManager.notify(null, new Event(EventTypes.ForgeStop));
                GameWindow.getCurrentScene().clearGameObjects();
                showDeathPopup = false;
            }
        }

        ImGui.nextColumn();
        if (ImGui.button(Icons.UserPlus + " Create New"))
        {showAddPopup = !showAddPopup;}
        ImGui.columns(1);

        if (showAddPopup)
        {
            newObjectName = Widgets.inputText(Icons.User + "  Name", newObjectName);
            ImGui.columns(2);
            if (Widgets.button(Icons.UserPlus + "  Create"))
            {
               GameObject obj = GameWindow.getCurrentScene().createGameObject(newObjectName);
               obj.addComponent(new SpriteComponent());
               GameWindow.getCurrentScene().addGameObject(obj);
               showAddPopup = false;
            }
            ImGui.nextColumn();
            if (Widgets.button(Icons.Ban + "  Cancel")) showAddPopup = false;
            ImGui.columns(1);
        }


        gameObjectList = GameWindow.getCurrentScene().getGameObjects();

        if (ImGui.collapsingHeader(Icons.Filter + "  Filters")) {
            nameFilter = Widgets.inputText("Filter by name", nameFilter);


            Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
            ImGui.text("Filter by components:");
            ImGui.beginChild("##ComponentFilterChild", ImGui.getContentRegionAvailX(), 200, true);
            Theme.resetDefaultTextColor();
            Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
            for (Class<? extends Component> componentClass : ComponentList.types) {
                boolean isSelected = componentFilters.getOrDefault(componentClass, false);
                if (ImGui.checkbox(componentClass.getSimpleName(), isSelected)) {
                    componentFilters.put(componentClass, !isSelected);
                }
            }

            ImGui.endChild();

            List<Class<? extends Component>> selectedComponents = new ArrayList<>();
            for (Map.Entry<Class<? extends Component>, Boolean> entry : componentFilters.entrySet()) {
                if (entry.getValue()) {
                    selectedComponents.add(entry.getKey());
                }
            }

            if (selectedComponents.isEmpty() && nameFilter.isEmpty()) {
                gameObjectList = GameWindow.getCurrentScene().getGameObjects();
            } else if (selectedComponents.isEmpty()) {
                gameObjectList = GameWindow.getCurrentScene().getGameObjects(nameFilter);
            } else if (nameFilter.isEmpty()) {
                gameObjectList = GameWindow.getCurrentScene().getGameObjects(selectedComponents);
            } else {
                gameObjectList = GameWindow.getCurrentScene().getGameObjects(selectedComponents, nameFilter);
            }

            Theme.resetDefaultTextColor();
        }

        if (showSingleDeathPopup)
        {
            GameObject obj = ComponentsWindow.getActiveGameObject();
            if (obj == null)
            {
                showSingleDeathPopup = false;
            }
            else
            {
                switch (Widgets.popUp(Icons.ExclamationTriangle, "Confirm Death", "Are you sure you want to delete\n" + obj, new Vector2f(300, 128)))
                {
                    case OK:
                        obj.destroy();
                        showSingleDeathPopup = false;
                        break;

                    case CANCEL:
                        showSingleDeathPopup = false;
                        break;
                }
            }
        }


        for (int i = 0; i < gameObjectList.size(); ++i)
        {
            GameObject obj = gameObjectList.get(i);
            if (!obj.getSerializationStatus())
            {
                continue;
            }
            if (ImGui.button("  " + Icons.Trash+"  ##" + i))
            {
                showSingleDeathPopup = !showSingleDeathPopup;
                ComponentsWindow.setActiveGameObject(obj);
            }
            ImGui.sameLine();
            if (ImGui.button("  " + Icons.Search+"  ##" + i))
            {
                Camera camera = GameWindow.getCurrentScene().getCamera();
                if (camera != null)
                {
                    camera.setPosition(new Vector2f(obj.transform.position).sub(new Vector2f(camera.getProjectionSize()).div(2)));
                    ComponentsWindow.clearSelection();
                    ComponentsWindow.setActiveGameObject(obj);
                }
            }
            ImGui.sameLine();
            if (Widgets.button(obj.name + "##" + i))
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