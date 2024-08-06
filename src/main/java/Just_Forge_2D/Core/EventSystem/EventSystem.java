package Just_Forge_2D.Core.EventSystem;

import Just_Forge_2D.Core.ECS.GameObject;
import Just_Forge_2D.Core.EventSystem.Events.Event;

import java.util.ArrayList;
import java.util.List;

public class EventSystem
{
    private static List<Observer> observers = new ArrayList<>();

    public static void addObserver(Observer OBSERVER)
    {
        observers.add(OBSERVER);
    }

    public static void notify(GameObject OBJECT, Event EVENT)
    {
        for (Observer obs : observers)
        {
            obs.onNotify(OBJECT, EVENT);
        }
    }
}
