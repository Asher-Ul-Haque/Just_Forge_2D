package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite;
import Just_Forge_2D.Utils.Configurations;
import Just_Forge_2D.Utils.Logger;

public class Frame
{
    public final Sprite sprite;
    public final float frameTime;

    public Frame(Sprite SPRITE, float TIME)
    {
        Logger.FORGE_LOG_TRACE("Creating new trace");
        this.sprite = SPRITE;
        this.frameTime = TIME;
    }
}