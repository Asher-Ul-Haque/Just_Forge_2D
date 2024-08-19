package Just_Forge_2D.EditorSystem;

import Just_Forge_2D.EntityComponentSystem.Components.BreakableBrick;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import imgui.ImGui;
import org.joml.Vector4f;

import java.util.ArrayList;
import java.util.List;


// - - - Properties Window
public class PropertiesWindow
{
    // - - - private variables
    private final List<GameObject> activeGameObjects;
    private final List<Vector4f> activeGameObjectsColors;
    private GameObject activeGameObject = null;
    private final ObjectSelector selector;


    // - - - Functions - - -


    // - - - Constructor - - -
    public PropertiesWindow(ObjectSelector SELECTOR)
    {
        this.activeGameObjects = new ArrayList<>();
        this.selector = SELECTOR;
        this.activeGameObjectsColors = new ArrayList<>();
    }


    // - - - Update - - -

    public void editorGUI()
    {
        if (activeGameObjects.size() == 1 && activeGameObjects.get(0) != null)
        {
            activeGameObject = activeGameObjects.get(0);
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("Component Adder"))
            {
                if (ImGui.menuItem("Add Rigid Body"))
                {
                    if (activeGameObject.getComponent(RigidBodyComponent.class) == null)
                    {
                        activeGameObject.addComponent(new RigidBodyComponent());
                    }
                }

                if (ImGui.menuItem("Add Box Collider"))
                {
                    if (activeGameObject.getComponent(BoxColliderComponent.class) == null && activeGameObject.getComponent(CircleColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new BoxColliderComponent());
                    }
                }

                if (ImGui.menuItem("Add Breakable Brick"))
                {
                    if (activeGameObject.getComponent(BreakableBrick.class) == null)
                    {
                        activeGameObject.addComponent(new BreakableBrick());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider"))
                {
                    if (activeGameObject.getComponent(CircleColliderComponent.class) == null && activeGameObject.getComponent(BoxColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new CircleColliderComponent());
                    }
                }

                if (ImGui.menuItem("Add Breakable Brick"))
                {
                    if (activeGameObject.getComponent(CircleColliderComponent.class) == null && activeGameObject.getComponent(BoxColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new CircleColliderComponent());
                    }
                }

                ImGui.endPopup();
            }

//            activeGameObject.editorGUI();
            ImGui.end();
        }
    }


    // - - - Active Game Objects - - -

    public GameObject getActiveGameObject()
    {
        return activeGameObjects.size() == 1 ? this.activeGameObjects.get(0) : null;
    }

    public List<GameObject> getActiveGameObjects()
    {
        return this.activeGameObjects;
    }

    public void clearSelection()
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
        this.activeGameObjects.clear();
        this.activeGameObjectsColors.clear();
    }

    public void setActiveGameObject(GameObject GO)
    {
        if (GO != null)
        {
            clearSelection();
            this.activeGameObjects.add(GO);
        }
    }

    public void addActiveGameObject(GameObject GO)
    {
        SpriteComponent spr = GO.getComponent(SpriteComponent.class);
        if (spr != null)
        {
            this.activeGameObjectsColors.add(new Vector4f(spr.getColor()));
            spr.setColor(new Vector4f(0.8f, 0.8f, 0.0f, 0.8f));
        }
        else
        {
            this.activeGameObjectsColors.add(new Vector4f());
        }
        this.activeGameObjects.add(GO);
    }


    // - - - selector
    public ObjectSelector getSelector()
    {
        return this.selector;
    }
}
