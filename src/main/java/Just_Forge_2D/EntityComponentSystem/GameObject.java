package Just_Forge_2D.EntityComponentSystem;

import Just_Forge_2D.AssetPool.AssetPool;
import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.EventSystem.EventManager;
import Just_Forge_2D.EventSystem.Events.Event;
import Just_Forge_2D.EventSystem.Events.EventTypes;
import Just_Forge_2D.Utils.JsonHandlers.ComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.GameObjectJsonHandler;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import java.util.ArrayList;
import java.util.List;


// - - - THe E of ECS
public class GameObject
{
    // - - - private variables
    public String name;
    private final List<Component> components = new ArrayList<>();
    public transient TransformComponent transform; // transform is a mandatory component
    private static int ID_COUNTER = 0;
    private int uniqueID = -1;
    private boolean doSerialization = true;
    private boolean isDead = false;


    // - - -  | Functions | - - -


    // - - - Constructors - - -

    public GameObject(String NAME)
    {
        this.name = NAME;
        this.uniqueID = ID_COUNTER++;
        Logger.FORGE_LOG_INFO("Created new Game Object : " + NAME);
    }


    // - - - component management - - -

    public <T extends Component> T getComponent(Class<T> COMPONENT_CLASS)
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
                    Logger.FORGE_LOG_ERROR("Failed component casting \n" + e.getMessage());
                    return null;
                }
            }
        }
        Logger.FORGE_LOG_WARNING("No component of type : " + COMPONENT_CLASS + " in : " + this);
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> COMPONENT_CLASS)
    {
        Logger.FORGE_LOG_DEBUG("Removing Component of type : " + COMPONENT_CLASS + " from: " + this);
        for (int i = 0; i < components.size(); ++i)
        {
            if (COMPONENT_CLASS.isAssignableFrom(components.get(i).getClass()))
            {
                Logger.FORGE_LOG_INFO("Removed Component of type : " + COMPONENT_CLASS + " from: " + this);
                try
                {
                    components.get(i).destroy();
                }
                catch (Exception e)
                {
                    handleComponentException(components.get(i), e);
                }
                components.remove(i);
                return;
            }
        }
        Logger.FORGE_LOG_WARNING("No component of type: " + COMPONENT_CLASS + " To remove from game object " + this.name);
    }

    public void addComponent(Component COMPONENT)
    {
        if (COMPONENT == null)
        {
            Logger.FORGE_LOG_ERROR("Cannot add a null component to : " + this);
            return;
        }
        COMPONENT.generateID();
        this.components.add(COMPONENT);
        COMPONENT.gameObject = this;
        Logger.FORGE_LOG_INFO("Added component: " + COMPONENT + " to : " + this);
    }

    public boolean hasComponent(Class<? extends Component> COMPONENT_CLASS)
    {
        if (COMPONENT_CLASS == null)
        {
            Logger.FORGE_LOG_ERROR(COMPONENT_CLASS + " is null for searching in : " + this);
            return false;
        }
        for (Component c : this.components)
        {
            if (COMPONENT_CLASS.isAssignableFrom(c.getClass())) return true;
        }
        return false;
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
            try
            {
                component.update(DELTA_TIME);
            }
            catch (Exception e)
            {

            }
        }
    }

    public void start()
    {
        // WARNING: DO not change to enhanced for loop
        for (int i = 0; i < components.size(); ++i)
        {
            try
            {
                components.get(i).start();
            }
            catch (Exception e)
            {
                handleComponentException(components.get(i), e);
            }
        }
    }

    public void destroy()
    {
        this.isDead = true;
        for (int i = 0; i < components.size(); ++i)
        {
            try
            {
                components.get(i).destroy();
            }
            catch (Exception e)
            {
                handleComponentException(components.get(i), e);
            }
        }
    }


    // - - - Editor Stuff - - -

    public void editorGUI()
    {
        for (int i = 0; i < components.size(); ++i)
        {
            Component component = components.get(i);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
            if (ImGui.collapsingHeader(component.getClass().getSimpleName()))
            {
                try
                {
                    component.editorGUI();
                }
                catch (Exception e)
                {
                    handleComponentException(component, e);
                }
            }
        }
    }

    public void editorUpdate(float DELTA_TIME)
    {
        for (int i = 0 ; i < components.size(); ++i)
        {
            try
            {
                components.get(i).editorUpdate(DELTA_TIME);
            }
            catch (Exception e)
            {
                handleComponentException(components.get(i), e);
            }
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

    private void generateUniqueID()
    {
        this.uniqueID = ID_COUNTER++;
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
        // TODO: refactor
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Component.class, new ComponentJsonHandler())
                .registerTypeAdapter(GameObject.class, new GameObjectJsonHandler())
                .enableComplexMapKeySerialization()
                .create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);
        obj.generateUniqueID();
        obj.name += " Copy: " + obj.getUniqueID();

        for (Component c : obj.components)
        {
            c.generateID();
        }

        SpriteComponent sprite = obj.getComponent(SpriteComponent.class);
        if (sprite != null && sprite.getTexture() != null)
        {
            sprite.setTexture(AssetPool.makeTexture(sprite.getTexture().getFilepath()));
        }

        return obj;
    }


    // - - - exception handling - - -

    private void handleComponentException(Component COMPONENT,  Exception e)
    {
        TinyFileDialogs.tinyfd_notifyPopup(e.getClass().getSimpleName(), "Component : " + COMPONENT + "\nGame Object : " + this.name, "error");
        Logger.FORGE_LOG_FATAL(COMPONENT + " caused Exception : " + e.getClass());
        Logger.FORGE_LOG_ERROR(e.getMessage());
        Logger.FORGE_LOG_ERROR("Reason: " + e.getCause());
        EventManager.notify(null, new Event(EventTypes.ForgeStop));
    }
}