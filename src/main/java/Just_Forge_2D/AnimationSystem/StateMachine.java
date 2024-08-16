package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class StateMachine extends Component
{
    private class StateTrigger
    {
        public String state;
        public String trigger;

        public StateTrigger(){}
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

    public HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void refreshTextures()
    {
        for (AnimationState state: states)
        {
            state.refreshTextures();
        }
    }

    public void addState(String FROM, String TO, String ON_TRIGGER)
    {
        this.stateTransfers.put(new StateTrigger(FROM, ON_TRIGGER), TO);
    }

    public void addState(AnimationState STATE)
    {
        this.states.add(STATE);
    }

    public void addStateTrigger(String FROM, String TO, String ON_TRIGGER)
    {
        this.stateTransfers.put(new StateTrigger(FROM, ON_TRIGGER), TO);
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
                    }
                }
                return;
            }
        }
    }

    @Override
    public void start()
    {
        for (AnimationState state: states)
        {
            if (state.title.equals(defaultStateTitle))
            {
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (currentState != null)
        {
            currentState.update(DELTA_TIME);
            SpriteComponent sprite = gameObject.getCompoent(SpriteComponent.class);
            if (sprite != null)
            {
                sprite.setSprite(currentState.getCurrentSprite());
            }
        }
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        update(DELTA_TIME);
    }

    public void setDefaultState(String TITLE)
    {
        for (AnimationState state : states)
        {
            if (state.title.equals(TITLE))
            {
                defaultStateTitle = TITLE;
                if (currentState == null)
                {
                    currentState = state;
                    return;
                }
            }
        }
        Logger.FORGE_LOG_ERROR("UNable to find state : " + TITLE + " to set to defualt");
    }

    @Override
    public void editorGUI()
    {
        for (AnimationState state : states)
        {
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);
            state.title = title.get();

            int index = 0;
            for (Frame frame: state.animationFrames)
            {
                float[] tmp = new float[1];
                ImGui.dragFloat("Frame(" + index + ") Time: ", tmp, 0.01f);
                frame.frameTime = tmp[0];
                index++;
            }
        }
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
