package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.Utils.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AnimationComponent extends Component
{
    public AnimationState defaultState;

    // - - - embedded class to add trigger
    private static class StateTrigger
    {
        public final String state;
        public final String trigger;

        public StateTrigger(String STATE, String TRIGGER)
        {
            this.state = STATE;
            this.trigger = TRIGGER;
        }

        @Override
        public boolean equals(Object OTHER)
        {
            if (OTHER.getClass() != StateTrigger.class) return false;
            StateTrigger t2 = (StateTrigger) OTHER;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(state, trigger);
        }
    }

    private final HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private final List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;

    public void refreshTextures()
    {
        for (AnimationState state: states)
        {
            state.refreshTextures();
        }
    }

    public void addStateTrigger(String FROM, String TO, String ON_TRIGGER)
    {
        this.stateTransfers.put(new StateTrigger(FROM, ON_TRIGGER), TO);
    }

    public void addState(AnimationState STATE)
    {
        this.states.add(STATE);
    }

    public void trigger(String TRIGGER)
    {
        for (StateTrigger state : stateTransfers.keySet())
        {
            if (state.state.equals(currentState.title) && state.trigger.equals(TRIGGER))
            {
                if (stateTransfers.get(state) != null)
                {
                    int newStateIndex = statesIndexOf(stateTransfers.get(state));
                    if (newStateIndex > -1)
                    {
                        currentState = states.get(newStateIndex);
                        currentState.reset();
                    }
                }
                return;
            }
        }
        Logger.FORGE_LOG_ERROR("Trigger failed : " + TRIGGER);
    }

    @Override
    public void start()
    {
        currentState = defaultState;
        if (currentState != null)
        {
            currentState.reset();
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (currentState != null)
        {
            currentState.update(DELTA_TIME);
            SpriteComponent sprite = gameObject.getComponent(SpriteComponent.class);
            if (sprite != null)
            {
                sprite.setSprite(currentState.getCurrentSprite());
            }
            if (currentState.isFinished)
            {
                currentState = defaultState;
                currentState.reset();
            }
        }
    }

    public void setDefaultState(AnimationState STATE)
    {
        Logger.FORGE_LOG_DEBUG("Setting default animation of : " + this.gameObject + " to : " + STATE.title);
        for (AnimationState state : states)
        {
            if (state.title.equals(STATE.title))
            {
                defaultState = STATE;
                break;
            }
        }
        Logger.FORGE_LOG_ERROR("Unable to find state: " + STATE.title + " to set as default");
    }

    private int statesIndexOf(String TITLE)
    {
        int index = 0;
        for (AnimationState state : states)
        {
            if (state.title.equals(TITLE))
            {
                return index;
            }
            ++index;
        }
        return -1;
    }
}
