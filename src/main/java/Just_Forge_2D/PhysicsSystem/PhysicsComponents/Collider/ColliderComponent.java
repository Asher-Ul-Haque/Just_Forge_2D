package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Utils.DefaultValues;
import org.joml.Vector4f;

public abstract class ColliderComponent extends Component
{
    protected boolean autoScale = true;
    protected boolean showHitboxAtRuntime = false;
    protected Vector4f baseColor = new Vector4f( 1.0f - DefaultValues.DEFAULT_CLEAR_COLOR.x, 1.0f - DefaultValues.DEFAULT_CLEAR_COLOR.y, 1.0f - DefaultValues.DEFAULT_CLEAR_COLOR.z, 1.0f - DefaultValues.DEFAULT_CLEAR_COLOR.w);
}
