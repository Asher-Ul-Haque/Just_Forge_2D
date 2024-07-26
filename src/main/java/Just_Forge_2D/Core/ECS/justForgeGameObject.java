package Just_Forge_2D.Core.ECS;

import Just_Forge_2D.Core.ECS.Components.justForgeComponent;
import Just_Forge_2D.Utils.justForgeTransform;
import Just_Forge_2D.Utils.justForgeLogger;

import java.util.ArrayList;
import java.util.List;

public class justForgeGameObject
{
    private String name;
    private List<justForgeComponent> components = new ArrayList<>();
    public justForgeTransform transform;
    private int layer;

    public justForgeGameObject(String NAME)
    {
        this.transform = new justForgeTransform();
        this.name = NAME;
        this.layer = 0;
    }

    public justForgeGameObject(String NAME, justForgeTransform TRANSFORM, int LAYER)
    {
        this.transform = TRANSFORM;
        this.name = NAME;
        this.layer = LAYER;
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
        justForgeLogger.FORGE_LOG_TRACE("Added component: " + COMPONENT + " to Game Object " + this);
    }

    public void update(float DELTA_TIME)
    {
        for (justForgeComponent component : components)
        {
            component.update(DELTA_TIME);
        }
    }

    public void start()
    {
        for (justForgeComponent component : components)
        {
            component.start();
        }
    }

    public int getLayer()
    {
        return this.layer;
    }
}
