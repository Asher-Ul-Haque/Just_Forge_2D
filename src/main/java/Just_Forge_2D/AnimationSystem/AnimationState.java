package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.Utils.AssetPool;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class AnimationState
{
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static final Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop = false;
    public boolean isFinished = false;


    // - - - Create animation state - - -


    public AnimationState() {}

    public AnimationState(String TITLE, boolean LOOP)
    {
        Logger.FORGE_LOG_DEBUG("Adding new animation state : " + TITLE);
        this.title = TITLE;
        this.doesLoop = LOOP;
        this.animationFrames = new ArrayList<>();
        this.isFinished = false;
    }


    // - - - refresh textures
    public void refreshTextures()
    {
        Logger.FORGE_LOG_DEBUG("Refreshing Textures");
        for (Frame frame : animationFrames)
        {
            frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilepath()));
        }
    }


    // - - - Add frames - - -

    public void addFrame(Sprite SPRITE, float FRAME_TIME)
    {
        animationFrames.add(new Frame(SPRITE, FRAME_TIME));
    }

    public void addFrame(Sprite SPRITE)
    {
        animationFrames.add(new Frame(SPRITE, Configurations.DEFAULT_FRAME_TIME));
    }

    public void addFrames(List<Sprite> SPRITES, float FRAME_TIME)
    {
        for (Sprite sprite : SPRITES)
        {
            this.animationFrames.add(new Frame(sprite, FRAME_TIME));
        }
    }


    // - - - Helper Functions - - -

    public void setLoop(boolean REALLY)
    {
        this.doesLoop = REALLY;
    }

    public void update(float DELTA_TIME)
    {
        if (currentSprite < animationFrames.size())
        {
            timeTracker -= DELTA_TIME;
            if (timeTracker <= 0)
            {
                if (!(currentSprite == animationFrames.size() - 1 && !doesLoop))
                {
                    currentSprite = (currentSprite + 1) % animationFrames.size();
                }
                else if (!doesLoop)
                {
                    this.isFinished = true;
                }
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
        }
        else
        {
            this.isFinished = true;
        }
    }

    public Sprite getCurrentSprite()
    {
        if (currentSprite < animationFrames.size())
        {
            return animationFrames.get(currentSprite).sprite;
        }
        return defaultSprite;
    }

    public boolean isFinished()
    {
        return this.isFinished;
    }

    public void reset()
    {
        this.currentSprite = 0;
        this.timeTracker = animationFrames.isEmpty() ? 0.0f : animationFrames.get(0).frameTime;
        this.isFinished = false;
    }
}
