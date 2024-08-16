package Just_Forge_2D.EventSystem;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.EventSystem.Events.Event;

public interface Observer
{
    void onNotify(GameObject OBJECT, Event EVENT);
}
