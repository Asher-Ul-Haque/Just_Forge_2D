package Just_Forge_2D.EditorSystem.Windows;

import Just_Forge_2D.EntityComponentSystem.Components.BreakableBrick;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import imgui.ImGui;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;


// - - - Properties Window
public class PropertiesWindow
{
    // - - - private variables
    private static final List<GameObject> activeGameObjects = new ArrayList<>();
    private static final List<Vector4f> activeGameObjectsColors = new ArrayList<>();


    // - - - Functions - - -

    // - - - Update - - -

    public static void render()
    {
        ImGui.begin("Properties");
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null)
        {
            GameObject activeGameObject = activeGameObjects.get(0);

            if (ImGui.beginPopupContextWindow("Component Adder"))
            {
                if (ImGui.menuItem("Add Rigid Body"))
                {
                    if (activeGameObject.getCompoent(RigidBodyComponent.class) == null)
                    {
                        activeGameObject.addComponent(new RigidBodyComponent());
                    }
                }

                if (ImGui.menuItem("Add Box Collider"))
                {
                    if (activeGameObject.getCompoent(BoxColliderComponent.class) == null && activeGameObject.getCompoent(CircleColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new BoxColliderComponent());
                    }
                }

                if (ImGui.menuItem("Add Breakable Brick"))
                {
                    if (activeGameObject.getCompoent(BreakableBrick.class) == null)
                    {
                        activeGameObject.addComponent(new BreakableBrick());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider"))
                {
                    if (activeGameObject.getCompoent(CircleColliderComponent.class) == null && activeGameObject.getCompoent(BoxColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new CircleColliderComponent());
                    }
                }

                if (ImGui.menuItem("Add Breakable Brick"))
                {
                    if (activeGameObject.getCompoent(CircleColliderComponent.class) == null && activeGameObject.getCompoent(BoxColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new CircleColliderComponent());
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
