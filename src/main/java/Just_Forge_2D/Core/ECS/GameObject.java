package Just_Forge_2D.Core.ECS;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Utils.justForgeLogger;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.List;


// - - - THe E of ECS
public class GameObject
{
    // - - - private variables
    private String name;
    private List<Component> components = new ArrayList<>();
    public transient TransformComponent transform; // transform is a mandatory component
    private static int ID_COUNTER = 0;
    private int uniqueID = -1;
    private boolean doSerialization = true;


    // - - -  | Functions | - - -

    // - - - Constructors - - -

    public GameObject(String NAME)
    {
        this.name = NAME;
        this.uniqueID = ID_COUNTER++;
        justForgeLogger.FORGE_LOG_DEBUG("Created new Game Object : " + NAME);
    }


    // - - - component management - - -

    public <T extends Component> T getCompoent(Class<T> COMPONENT_CLASS)
    {
        for (Component c : components)
        {
            if (COMPONENT_CLASS.isAssignableFrom(c.getClass()))
            {
                try
                {
                    return COMPONENT_CLASS.cast(c);
                }
                catch (ClassCastException e)
                {
                    justForgeLogger.FORGE_LOG_ERROR("Failed component casting \n" + e.getMessage());
                    assert false;
                }
            }
        }
        justForgeLogger.FORGE_LOG_WARNING("Returning null on get Component on Game Object " + this.name + " for component of type" + COMPONENT_CLASS);
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> COMPONENT_CLASS)
    {
        for (int i = 0; i < components.size(); ++i)
        {
            if (COMPONENT_CLASS.isAssignableFrom(components.getClass()))
            {
                justForgeLogger.FORGE_LOG_TRACE("Removed Component at index " + i + ": " + components.get(i).toString());
                components.remove(i);
                return;
            }
        }
        justForgeLogger.FORGE_LOG_WARNING("No component of type: " + COMPONENT_CLASS + " To remove from game object " + this.name);
    }

    public void addComponent(Component COMPONENT)
    {
        COMPONENT.generateID();
        this.components.add(COMPONENT);
        COMPONENT.gameObject = this;
        justForgeLogger.FORGE_LOG_TRACE("Added component: " + COMPONENT + " to Game Object " + this);
    }

    public List<Component> getComponents()
    {
        return this.components;
    }


    // - - - Usage - - -

    public void update(float DELTA_TIME)
    {
        for (Component component : components)
        {
            component.update(DELTA_TIME);
        }
    }

    public void start()
    {
        // WARNING: DO not change to enhanced for loop
        for (int i = 0; i < components.size(); ++i)
        {
            components.get(i).start();
        }
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public void editorGUI()
    {
        for (Component component : components)
        {
            if (ImGui.collapsingHeader(component.getClass().getSimpleName()))
            component.editorGUI();
        }
    }


    // - - - Unique IDS - - -

    public static void init(int MAX_ID)
    {
        ID_COUNTER = MAX_ID;
    }

    public int getUniqueID()
    {
        return this.uniqueID;
    }


    // - - - Saving - - -
    public void noSerialize()
    {
        this.doSerialization = false;
    }

    public boolean getSerializationStatus()
    {
        return this.doSerialization;
    }
}
