package Just_Forge_2D.CoreSystems.EventSystem.Events;

public class Event
{
    public EventTypes type;

    public Event(EventTypes TYPE)
    {
        this.type = TYPE;
    }

    public Event()
    {
        this.type = EventTypes.UserEvent;
    }
}