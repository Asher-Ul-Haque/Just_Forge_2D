package Just_Forge_2D.CoreSystems.EventSystem;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.GameObject;
import Just_Forge_2D.CoreSystems.EventSystem.Events.Event;

public interface Observer
{
    void onNotify(GameObject OBJECT, Event EVENT);
}
