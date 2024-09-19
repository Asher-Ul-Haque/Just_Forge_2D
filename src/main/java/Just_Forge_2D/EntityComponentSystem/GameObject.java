package Just_Forge_2D.EntityComponentSystem;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.JsonHandlers.ComponentJsonHandler;
import Just_Forge_2D.Utils.JsonHandlers.GameObjectJsonHandler;
import Just_Forge_2D.Utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import imgui.ImGui;

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
        Logger.FORGE_LOG_DEBUG("Created new Game Object : " + NAME);
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
                    Logger.FORGE_LOG_ERROR("Failed component casting \n" + e.getMessage());
                    assert false;
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> COMPONENT_CLASS)
    {
        for (int i = 0; i < components.size(); ++i)
        {
            if (COMPONENT_CLASS.isAssignableFrom(components.get(i).getClass()))
            {
                Logger.FORGE_LOG_TRACE("Removed Component at index " + i + ": " + components.get(i).toString());
                components.get(i).destroy();
                components.remove(i);
                return;
            }
        }
        Logger.FORGE_LOG_WARNING("No component of type: " + COMPONENT_CLASS + " To remove from game object " + this.name);
    }

    public void addComponent(Component COMPONENT)
    {
        COMPONENT.generateID();
        this.components.add(COMPONENT);
        COMPONENT.gameObject = this;
        Logger.FORGE_LOG_TRACE("Added component: " + COMPONENT + " to Game Object " + this);
    }

    public boolean hasComponent(Class<? extends Component> COMPONENT_CLASS)
    {
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

    // - - - Editor Stuff
    public void editorGUI()
    {
        for (int i = 0; i < components.size(); ++i)
        {
            Component component = components.get(i);
            ImGui.setCursorPosY(ImGui.getCursorPosY() + EditorSystemManager.getCurrentTheme().framePadding.y);
            if (ImGui.collapsingHeader(component.getClass().getSimpleName()))
            {
                component.editorGUI();
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

    public void destroy()
    {
        this.isDead = true;
        for (int i = 0; i < components.size(); ++i)
        {
            components.get(i).destroy();
        }
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

    public void editorUpdate(float DELTA_TIME)
    {
        for (int i = 0 ; i < components.size(); ++i)
        {
            components.get(i).editorUpdate(DELTA_TIME);
        }
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

        SpriteComponent sprite = obj.getCompoent(SpriteComponent.class);
        if (sprite != null && sprite.getTexture() != null)
        {
            sprite.setTexture(AssetPool.getTexture(sprite.getTexture().getFilepath()));
        }

        return obj;
    }

    private void generateUniqueID()
    {
        this.uniqueID = ID_COUNTER++;
    }
}
