package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.SpriteComponent;
import Just_Forge_2D.Utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AnimationComponent extends Component {
    private static class StateTrigger {
        public final String state;
        public final String trigger;

        public StateTrigger(String STATE, String TRIGGER) {
            this.state = STATE;
            this.trigger = TRIGGER;
        }

        @Override
        public boolean equals(Object OTHER) {
            if (OTHER.getClass() != StateTrigger.class) return false;
            StateTrigger t2 = (StateTrigger) OTHER;
            return t2.trigger.equals(this.trigger) && t2.state.equals(this.state);
        }

        @Override
        public int hashCode() {
            return Objects.hash(state, trigger);
        }
    }

    private HashMap<StateTrigger, String> stateTransfers = new HashMap<>();
    private List<AnimationState> states = new ArrayList<>();
    private transient AnimationState currentState = null;
    private String defaultStateTitle = "";

    public void refreshTextures() {
        for (AnimationState state: states) {
            state.refreshTextures();
        }
    }

    public void addStateTrigger(String FROM, String TO, String ON_TRIGGER) {
        this.stateTransfers.put(new StateTrigger(FROM, ON_TRIGGER), TO);
    }

    public void addState(AnimationState STATE) {
        this.states.add(STATE);
    }

    public void trigger(String TRIGGER) {
        for (StateTrigger state : stateTransfers.keySet()) {
            if (state.state.equals(currentState.title) && state.trigger.equals(TRIGGER)) {
                if (stateTransfers.get(state) != null) {
                    int newStateIndex = statesIndexOf(stateTransfers.get(state));
                    if (newStateIndex > -1) {
                        currentState = states.get(newStateIndex);
                    }
                }
                return;
            }
        }
    }

    @Override
    public void start() {
        for (AnimationState state: states) {
            if (state.title.equals(defaultStateTitle)) {
                currentState = state;
                break;
            }
        }
    }

    @Override
    public void update(float DELTA_TIME) {
        if (currentState != null) {
            currentState.update(DELTA_TIME);
            SpriteComponent sprite = gameObject.getComponent(SpriteComponent.class);
            if (sprite != null) {
                sprite.setSprite(currentState.getCurrentSprite());
            }
            // Check if the animation has finished and trigger the default state if needed
            if (currentState.isAnimationFinished()) {
                trigger("animationFinished");
            }
        }
    }

    public void setDefaultState(String TITLE) {
        for (AnimationState state : states) {
            if (state.title.equals(TITLE)) {
                defaultStateTitle = TITLE;
                if (currentState == null) {
                    currentState = state;
                    return;
                }
            }
        }
        Logger.FORGE_LOG_ERROR("Unable to find state: " + TITLE + " to set as default");
    }

    private int statesIndexOf(String TITLE) {
        int index = 0;
        for (AnimationState state : states) {
            if (state.title.equals(TITLE)) {
                return index;
            }
            ++index;
        }
        return -1;
    }
}
