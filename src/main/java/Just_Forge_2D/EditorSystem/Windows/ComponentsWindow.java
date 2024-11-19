package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.ImGUIManager;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.ComponentList;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PrefabSystem.CopyCatPrefab;
import Just_Forge_2D.PrefabSystem.PrefabManager;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;


// - - - Properties Window
public class ComponentsWindow
{
    // - - - private variables
    private static final List<GameObject> activeGameObjects = new ArrayList<>();
    private static final List<Vector4f> activeGameObjectsColors = new ArrayList<>();
    private static boolean deletePopup = false;


    // - - - Functions - - -

    // - - - Update - - -

    public static void render()
    {
        ImGui.begin(Icons.CodeBranch + "  Components Window");
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null)
        {
            GameObject activeGameObject = activeGameObjects.get(0);
            ImGui.columns(2);
            if (Widgets.button(Icons.Trash + " Delete"))
            {
                deletePopup = !deletePopup;
            }
            ImGui.nextColumn();
            if (Widgets.button(Icons.Copy + "  Make Prefab"))
            {
                CopyCatPrefab prefab = new CopyCatPrefab(activeGameObject.name, activeGameObject);
                PrefabManager.registerPrefab(activeGameObject.name,  prefab);
            }
            ImGui.columns(1);
            Widgets.text("");
            if (deletePopup)
            {
                Widgets.PopupReturn returnVal = Widgets.popUp(Icons.ExclamationTriangle, "Delete Confirmation", "Are you sure you want to delete \n" +activeGameObject);
                switch (returnVal)
                {
                    case OK:
                        activeGameObject.destroy();
                        clearSelection();
                        deletePopup = false;
                        break;

                    case CANCEL:
                        deletePopup = false;
                        break;
                }
            }
            Widgets.text("");
            if (activeGameObject != null)
            {
                if (ImGui.beginPopupContextWindow(Icons.UserCog + "  Component Adder"))
                {
                    String message = "Add Component";
                    ImGui.setCursorPosX((ImGui.getContentRegionAvailX() - ImGui.calcTextSize(message).x) / 2f);
                    ImGui.pushFont(ImGUIManager.interExtraBold);
                    ImGui.text(message);
                    ImGui.popFont();
                    Widgets.text("");

                    int delay = 2;
                    for (Class<? extends Component> type : ComponentList.getTypes())
                    {
                        // - - - Skip components already added to the active game object
                        if (activeGameObject.getComponent(type) != null) continue;

                        // - - - Get component registry info for dependencies
                        ComponentList.ComponentRegistry registry = ComponentList.getComponentInfo(type);

                        // - - - Check if all required components are present
                        List<Class<? extends Component>> requiredTypes = registry.requiredComponents();
                        if (requiredTypes != null)
                        {
                            boolean allRequirementsMet = requiredTypes.stream()
                                    .allMatch(requiredType -> activeGameObject.getComponent(requiredType) != null);

                            // - - - If not all requirements are met, skip this component
                            if (!allRequirementsMet) continue;
                        }

                        // - - - Add a separator between groups of components
                        if (delay-- == 0)
                        {
                            Widgets.text("");
                            delay = 2;
                        }

                        // - - - Display the menu item and handle the component addition on click
                        if (ImGui.menuItem(registry.name()))
                        {
                            try
                            {
                                Component component = type.getDeclaredConstructor().newInstance();
                                activeGameObject.addComponent(component);
                            }
                            catch (Exception e)
                            {
                                Logger.FORGE_LOG_ERROR("Can't add component: " + registry.name());
                                Logger.FORGE_LOG_ERROR(e.getCause());
                            }
                        }
                    }
                    ImGui.endPopup();
                }

                List<Component> components = activeGameObject.getComponents();
                for (int i = 0; i < components.size(); ++i)
                {
                    Component component = components.get(i);
                    String name = component.getClass().getSimpleName();
                    ComponentList.ComponentRegistry componentRegistry = ComponentList.getComponentInfo(component.getClass());
                    if (componentRegistry != null)
                    {
                        name = componentRegistry.name();
                    }
                    if (ImGui.collapsingHeader(name))
                    {
                        component.editorGUI();
                    }
                }
            }
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
                SpriteComponent spr = go.getComponent(SpriteComponent.class);
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
            SpriteComponent spr = GO.getComponent(SpriteComponent.class);
            if (spr != null)
            {
                activeGameObjects.add(GO);
            }
        }
    }

    public static void addActiveGameObject(GameObject GO)
    {
        SpriteComponent spr = GO.getComponent(SpriteComponent.class);
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