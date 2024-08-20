package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Configurations;

import java.util.ArrayList;
import java.util.List;

public class AnimationState {
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static final Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop = false;
    public boolean isAnimationFinished = false;

    public AnimationState() {}

    public AnimationState(String TITLE, boolean LOOP) {
        this.title = TITLE;
        this.doesLoop = LOOP;
        this.animationFrames = new ArrayList<>();
    }

    public void refreshTextures() {
        for (Frame frame : animationFrames) {
            frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilepath()));
        }
    }

    public void addFrame(Sprite SPRITE, float FRAME_TIME) {
        animationFrames.add(new Frame(SPRITE, FRAME_TIME));
    }

    public void addFrame(Sprite SPRITE) {
        animationFrames.add(new Frame(SPRITE, Configurations.DEFAULT_FRAME_TIME));
    }

    public void setLoop(boolean REALLY) {
        this.doesLoop = REALLY;
    }

    public void addFrames(List<Sprite> SPRITES, float FRAME_TIME) {
        for (Sprite sprite : SPRITES) {
            this.animationFrames.add(new Frame(sprite, FRAME_TIME));
        }
    }

    public void update(float DELTA_TIME) {
        if (currentSprite < animationFrames.size()) {
            timeTracker -= DELTA_TIME;
            if (timeTracker <= 0) {
                boolean wasLastFrame = currentSprite == animationFrames.size() - 1;
                currentSprite = (currentSprite + 1) % animationFrames.size();
                timeTracker = animationFrames.get(currentSprite).frameTime;

                if (wasLastFrame && !doesLoop)
                {
                    this.isAnimationFinished = true;
                }
            }
        }
    }

    public Sprite getCurrentSprite() {
        if (currentSprite < animationFrames.size()) {
            return animationFrames.get(currentSprite).sprite;
        }
        return defaultSprite;
    }

    protected boolean isAnimationFinished() {
        // Notify AnimationComponent that this animation has finished
        return isAnimationFinished;
    }
}
