package Just_Forge_2D.Core.EventSystem;

import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.EventSystem.Events.Event;

public interface Observer
{
    void onNotify(GameObject OBJECT, Event EVENT);
}
