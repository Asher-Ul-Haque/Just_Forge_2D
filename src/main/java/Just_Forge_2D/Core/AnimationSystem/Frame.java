package Just_Forge_2D.Core.AnimationSystem;

import Just_Forge_2D.Core.EntityComponentSystem.Components.Sprite.Sprite;

public class Frame
{
    public Sprite sprite;
    public float frameTime;

    public Frame()
    {

    }

    public Frame(Sprite SPRITE, float TIME)
    {
        this.sprite = SPRITE;
        this.frameTime = TIME;
    }
}
