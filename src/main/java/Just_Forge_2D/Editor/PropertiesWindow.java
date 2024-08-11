package Just_Forge_2D.Editor;

import Just_Forge_2D.Core.ECS.Components.EditorComponents.NonPickableComponent;
import Just_Forge_2D.Core.ECS.Components.PhysicsComponents.Collider.BoxColliderComponent;
import Just_Forge_2D.Core.ECS.Components.PhysicsComponents.Collider.CircleColliderComponent;
import Just_Forge_2D.Core.ECS.Components.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.Input.Mouse;
import Just_Forge_2D.Core.Scene.Scene;
import Just_Forge_2D.Core.ForgeDynamo;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;


// - - - Properties Window
public class PropertiesWindow
{
    // - - - private variables
    private List<GameObject> activeGameObjects;
    private GameObject activeGameObject = null;
    private final ObjectSelector selector;
    private float debounce = 0.2f;


    // - - - Functions - - -

    // - - - Constructor
    public PropertiesWindow(ObjectSelector SELECTOR)
    {
        this.activeGameObjects = new ArrayList<>();
        this.selector = SELECTOR;
    }

    // - - - usage
    public void update(float DELTA_TIME, Scene CURRENT_SCENE)
    {
        debounce -= DELTA_TIME;
        if (!Mouse.isDragging() && Mouse.isMouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0)
        {
            int x = (int)Mouse.getScreenX();
            int y = (int)Mouse.getScreenY();
            int gameObjectID = this.selector.readPixel(x, y);
            GameObject picked = ForgeDynamo.getCurrentScene().getGameObject(gameObjectID);
            if (picked != null && picked.getCompoent(NonPickableComponent.class) == null)
            {
                Logger.FORGE_LOG_DEBUG("Currently active object: " + picked.toString());
                setActiveGameObject(picked);
            }
            else if (picked == null && !Mouse.isDragging())
            {
                Logger.FORGE_LOG_DEBUG("Currently active object: Null");
                activeGameObject = null;
            }
            this.debounce = 0.2f;
        }
    }

    // - - - getter
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
        this.activeGameObjects.clear();
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
        this.activeGameObjects.add(GO);
    }

    // - - - ignore this, this is for the editor
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

                if (ImGui.menuItem("Add Circle Collider"))
                {
                    if (activeGameObject.getCompoent(CircleColliderComponent.class) == null && activeGameObject.getCompoent(BoxColliderComponent.class) == null)
                    {
                        activeGameObject.addComponent(new CircleColliderComponent());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.editorGUI();
            ImGui.end();
        }
    }

//    public
}
