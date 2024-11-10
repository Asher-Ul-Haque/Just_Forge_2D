package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
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
    private boolean editorUpdate = false;
    private transient String to = "";
    private transient String trigger = "";
    private transient String temp = "";

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
        if (TO.equals(FROM)) return;
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
        if (editorUpdate) update(DELTA_TIME);
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
        editorUpdate = Widgets.drawBoolControl(Icons.PenAlt + "  Editor Update", editorUpdate);

        Widgets.text("");
        if (Widgets.button(Icons.PlusSquare + "  Add Animation"))
        {
            this.addState(new AnimationState("New Animation", false, gameObject.getComponent(SpriteComponent.class).getSpriteCopy()));
        }

        for (int i = 0; i < this.states.size(); i++)
        {
            AnimationState animation = states.get(i);
            if (ImGui.collapsingHeader(states.get(i).title))
            {
                ImGui.separator();
                if (Widgets.button(Icons.Trash + "  Delete" + " ##" + i))
                {
                    this.removeState(animation);
                    continue;
                }
                Widgets.text("");

                if (Widgets.button(Icons.Check)) animation.title = temp;
                ImGui.sameLine();
                temp = Widgets.inputText("Title: ", temp);

                if (Widgets.button(Icons.Star + "  Set as Default" + " ##" + i))
                {
                    setDefaultState(animation.title);
                }

                ImGui.sameLine();
                animation.previewControls(GameWindow.get().getDeltaTime());

                ImGui.sameLine();
                animation.newFrameControls();

                Widgets.text("");
                animation.editorGUI();
                Widgets.text("");
                ImGui.separator();
                Widgets.text("");

                // - - - Trigger management
                for (StateTrigger trigger : stateTransfers.keySet())
                {
                    if (trigger.state.equals(animation.title))
                    {
                        String toState = stateTransfers.get(trigger);

                        if (Widgets.button(Icons.Trash + "  ##" + i))
                        {
                            stateTransfers.remove(trigger);
                        }
                        ImGui.sameLine();

                        if (ImGui.beginCombo("##toStateCombo" + i, toState))
                        {
                            for (AnimationState state : states)
                            {
                                if (ImGui.selectable(state.title, state.title.equals(toState)))
                                {
                                    stateTransfers.put(trigger, state.title);
                                    addStateTrigger(animation.title, state.title, trigger.trigger);
                                }
                            }
                            ImGui.endCombo();
                        }
                        ImGui.sameLine();

                        ImGui.inputText(trigger.trigger, new ImString(trigger.trigger));
                    }
                }
                // - - - Add New Trigger UI
                Widgets.text("");
                Widgets.text("Add a Trigger");
                to = Widgets.inputText("To State", to);
                trigger = Widgets.inputText("Trigger", trigger);
                if (Widgets.button(Icons.PlusSquare + " Add Trigger"))
                {
                    addStateTrigger(animation.title, to, trigger);
                }
                Widgets.text("");
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

    public void removeState(AnimationState STATE)
    {
        if (!states.contains(STATE))
        {
            Logger.FORGE_LOG_WARNING("State not found: " + STATE.title);
            return;
        }

        // - - -Remove the state from the list
        states.remove(STATE);

        // - - - Remove any state triggers associated with the state
        stateTransfers.keySet().removeIf(trigger -> trigger.state.equals(STATE.title));

        // - - - Reset default state if it's the one being removed
        if (Objects.equals(defaultStateTitle, STATE.title))
        {
            defaultStateTitle = "";
            currentState = null;
        }

        Logger.FORGE_LOG_INFO("Removed state: " + STATE.title);
    }
}