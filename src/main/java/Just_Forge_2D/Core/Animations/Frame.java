package Just_Forge_2D.Core.Animations;

import Just_Forge_2D.Core.ECS.Components.Sprite.Sprite;

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
