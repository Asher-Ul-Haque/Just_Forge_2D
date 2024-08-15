package Just_Forge_2D.CoreSystems.EventSystem;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.GameObject;
import Just_Forge_2D.CoreSystems.EventSystem.Events.Event;
import java.util.ArrayList;
import java.util.List;


// - - - Event System
public class EventManager
{
    // - - - private list of observers
    private static final List<Observer> observers = new ArrayList<>();

    // - - - add an observer
    public static void addObserver(Observer OBSERVER)
    {
        observers.add(OBSERVER);
    }

    // - - - trigger an event
    public static void notify(GameObject OBJECT, Event EVENT)
    {
        for (Observer obs : observers)
        {
            obs.onNotify(OBJECT, EVENT);
        }
    }
}
