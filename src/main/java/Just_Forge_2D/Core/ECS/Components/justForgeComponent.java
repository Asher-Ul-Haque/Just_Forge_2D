package Just_Forge_2D.Core.ECS.Components;

import Just_Forge_2D.Core.ECS.justForgeGameObject;

// - - - Abstrasct class for components
public abstract class justForgeComponent
{
    public transient justForgeGameObject gameObject = null;

    public void update(float DELTA_TIME){}
    public void start(){}

    public void editorGUI(){}
}
