package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.Sprite.SpriteComponent;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import imgui.type.ImString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AnimationComponent extends Component
{
    // - - - static class for triggering
    private static class StateTrigger
    {
        public String state;
        public String trigger;

        public StateTrigger(String STATE, String TRIGGER)
        {
            this.state = STATE;
            this.trigger = TRIGGER;
        }

        @Override
        public boolean equals(Object OTHER)
        {
            if (!(OTHER instanceof StateTrigger t2)) return false;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(state, trigger);
        }
    }

    // - - - private variables
    private final HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private final List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";
    private boolean revertToDefault = false;


    // - - - | Functions | - - -


    // - - - add states - - -

    public void addState(AnimationState STATE)
    {
        this.states.add(STATE);
    }

    public void addStateTrigger(String FROM, String TO, String ON_TRIGGER)
    {
        this.stateTransfers.put(new StateTrigger(FROM, ON_TRIGGER), TO);
    }

    public void setDefaultState(String TITLE)
    {
        for (AnimationState state : states)
        {
            if (state.title.equals(TITLE))
            {
                setDefaultState(state);
                return;
            }
        }
        Logger.FORGE_LOG_ERROR("Unable to find state : " + TITLE + " to set to default");
    }

    public void setDefaultState(AnimationState STATE)
    {
        if (states.contains(STATE))
        {
            defaultStateTitle = STATE.title;
            if (currentState == null)
            {
                currentState = STATE;
            }
        }
    }


    // - - - trigger and use - - -

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

    public void setRevertToDefault(boolean REALLY)
    {
        if (this.revertToDefault == REALLY)
        {
            Logger.FORGE_LOG_WARNING(this + " already has revertToDefaultStatus : " + REALLY);
            return;
        }
        this.revertToDefault = REALLY;
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

            if (!currentState.isLooping() && currentState.isFinished() && revertToDefault)
            {
                currentState = getStateByName(defaultStateTitle);
            }

            SpriteComponent sprite = gameObject.getComponent(SpriteComponent.class);
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


    // - - - helper functions - - -

    public void refreshTextures()
    {
        for (AnimationState state: states)
        {
            state.refreshTextures();
        }
    }

    @Override
    public void editorGUI()
    {
        super.deleteButton();
        if (ImGui.collapsingHeader(Icons.List + "  Animations"))
        {
            if (Widgets.button(Icons.PlusSquare + "  Add Animation"))
            {
                this.addState(new AnimationState("New Animation", false));
            }

            for (int i = 0; i < this.states.size(); i++)
            {
                AnimationState animation = states.get(i);
                if (Widgets.button(Icons.Trash + "  Delete"))
                {
                    //this.removeState(animation);
                    continue;
                }

                Widgets.text("Animation: " + animation.title);
                animation.editorGUI(GameWindow.get().getDeltaTime());

                //  - - - Set default animation
                if (Widgets.button(Icons.Star + "  Set as Default"))
                {
                    setDefaultState(animation.title);
                }

                // - - - Add trigger controls
            }
        }
        for (AnimationState state : states)
        {
            ImString title = new ImString(state.title);
            ImGui.inputText("State: ", title);
            state.title = title.get();

            int index = 0;
            for (Frame frame: state.animationFrames)
            {
                float[] tmp = new float[]{frame.frameTime};
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

    public AnimationState getStateByName(String TITLE)
    {
        for (AnimationState state : states)
        {
            if (state.title.equals(TITLE))
            {
                return state;
            }
        }
        return null;
    }

}