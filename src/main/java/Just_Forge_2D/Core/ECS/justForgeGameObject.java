package Just_Forge_2D.Core.ECS;

import Just_Forge_2D.Core.ECS.Components.justForgeComponent;
import Just_Forge_2D.Core.justForgeLogger;

import java.util.ArrayList;
import java.util.List;

public class justForgeGameObject
{
    private String name;
    private List<justForgeComponent> components = new ArrayList<>();

    public justForgeGameObject(String NAME)
    {
        this.name = NAME;
    }

    public <T extends justForgeComponent> T getCompoent(Class<T> COMPONENT_CLASS)
    {
        for (justForgeComponent c : components)
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
        return null;
    }

    public <T extends justForgeComponent> void removeComponent(Class<T> COMPONENT_CLASS)
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
    }

    public void addComponent(justForgeComponent COMPONENT)
    {
        this.components.add(COMPONENT);
        COMPONENT.gameObject = this;
        justForgeLogger.FORGE_LOG_TRACE("Added component: " + COMPONENT.toString() + "to Game Object " + this.toString());
    }

    public void update(float DELTA_TIME)
    {
        for (int i = 0; i < components.size(); ++i)
        {
            components.get(i).update(DELTA_TIME);
        }
    }

    public void start()
    {
        for (int i = 0; i < components.size(); ++i)
        {
            components.get(i).start();
        }
    }
}
