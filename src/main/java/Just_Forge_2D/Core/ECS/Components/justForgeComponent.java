package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.ECS.justForgeGameObject;

public abstract class justForgeComponent
{
    public justForgeGameObject gameObject = null;

    public abstract void update(float DELTA_TIME);

    public void start(){}
}
