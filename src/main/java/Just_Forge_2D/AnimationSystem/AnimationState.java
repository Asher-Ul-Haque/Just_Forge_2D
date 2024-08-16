package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;
import Just_Forge_2D.Utils.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class AnimationState
{
    public String title;
    public List<Frame> animationFrames = new ArrayList<>();

    private static Sprite defaultSprite = new Sprite();
    private transient float timeTracker = 0.0f;
    private transient int currentSprite = 0;
    public boolean doesLoop = false;

    public void refreshTextures()
    {
        for (Frame frame : animationFrames)
        {
            frame.sprite.setTexture(AssetPool.getTexture(frame.sprite.getTexture().getFilepath()));
        }
    }

    public void addFrame(Sprite SPRITE, float FRAME_TIME)
    {
        animationFrames.add(new Frame(SPRITE, FRAME_TIME));
    }

    public void setLoop(boolean REALLY)
    {
        this.doesLoop = REALLY;
    }

    public void addFrames(List<Sprite> SPRITES, float FRAME_TIME)
    {
        for (Sprite sprite : SPRITES)
        {
            this.animationFrames.add(new Frame(sprite, FRAME_TIME));
        }
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
                timeTracker = animationFrames.get(currentSprite).frameTime;
            }
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
}
