package Just_Forge_2D.EntityComponentSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.JsonHandlers.ComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.GameObjectJsonHandler;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


// - - - THe E of ECS
public class GameObject
{
    // - - - private variables
    public String name;
    private final Map<Class<? extends Component>, Component> components = new HashMap<>();
    public transient TransformComponent transform; // transform is a mandatory component
    private static int ID_COUNTER = 0;
    private int uniqueID = -1;
    private boolean doSerialization = true;
    private boolean isDead = false;


    // - - -  | Functions | - - -

    // - - - Constructors - - -

    public GameObject(String NAME)
    {
        Logger.FORGE_LOG_DEBUG("Creating new Game Object : " + NAME);
        this.name = NAME;
        this.uniqueID = ID_COUNTER++;
        this.transform = new TransformComponent();
    }

    public GameObject(String NAME, TransformComponent TRANSFORM)
    {
        Logger.FORGE_LOG_DEBUG("Creating new Game Object : " + NAME);
        this.name = NAME;
        this.uniqueID = ID_COUNTER++;
        this.transform = TRANSFORM;
    }


    // - - - component management - - -

    public <T extends Component> T getComponent(Class<T> COMPONENT_CLASS)
    {
        T component = COMPONENT_CLASS.cast(this.components.get(COMPONENT_CLASS));
        if (component == null)
        {
            Logger.FORGE_LOG_ERROR("No such component in : " + this);
        }
        return component;
    }

    public <T extends Component> void removeComponent(Class<T> COMPONENT_CLASS)
    {
        Logger.FORGE_LOG_DEBUG("Removing Component : " + COMPONENT_CLASS + " from : " + this);
        if (!this.components.containsKey(COMPONENT_CLASS))
        {
            Logger.FORGE_LOG_ERROR("No such component in : " + this);
        }
        this.components.remove(COMPONENT_CLASS);
    }

    public void addComponent(Component COMPONENT)
    {
        Logger.FORGE_LOG_DEBUG("Adding component: " + COMPONENT.getClass() + " to : " + this);
        COMPONENT.generateID();
        this.components.put(COMPONENT.getClass(), COMPONENT);
        COMPONENT.gameObject = this;
    }

    public List<Component> getComponents()
    {
        return new ArrayList<>(this.components.values());
    }


    // - - - Usage - - -

    public void update(float DELTA_TIME)
    {
        for (Component component : this.components.values())
        {
            component.update(DELTA_TIME);
        }
    }

    public void start()
    {
        List<Component> componentsToStart = new ArrayList<>(components.values());

        for (int i = 0; i < componentsToStart.size(); ++i)
        {
            Component component = componentsToStart.get(i);
            component.start();

            if (components.size() > componentsToStart.size())
            {
                Logger.FORGE_LOG_WARNING("Bro, why adding components at run time. You are making a headache for the game engine developer. Thats ME");
                componentsToStart = new ArrayList<>(components.values());
            }
        }
    }

    public void destroy()
    {
        this.isDead = true;
        List<Component> list = getComponents();
        for (int i = 0; i < components.size(); ++i)
        {
            list.get(i).destroy();
        }
    }

    // - - - Unique IDS - - -

    @Override
    public String toString()
    {
        return this.name;
    }

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

    public boolean isDead()
    {
        return this.isDead;
    }

    public GameObject copy()
    {
       return GameObjectJsonHandler.copy(this);
    }

    public void generateUniqueID()
    {
        this.uniqueID = ID_COUNTER++;
    }
}