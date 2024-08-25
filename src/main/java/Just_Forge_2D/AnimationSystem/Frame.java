package Just_Forge_2D.AnimationSystem;

import Just_Forge_2D.EntityComponentSystem.Components.Sprite.Sprite;

public class Frame
{
    public final Sprite sprite;
    public float frameTime;

    public Frame(Sprite SPRITE, float TIME)
    {
        this.sprite = SPRITE;
        this.frameTime = TIME;
    }
}