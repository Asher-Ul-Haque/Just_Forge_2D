package Just_Forge_2D.PhysicsSystem.Raycasts;

import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;

public class Raycast
{
    // - - - Ray cast

    public RayCastInfo rayCast(GameObject REQUESTEE, Vector2f POINT_1, Vector2f POINT_2)
    {
        RayCastInfo callback = new RayCastInfo(REQUESTEE);
        MainWindow.getPhysicsSystem().rawWorld.getWorld().raycast(callback, new Vec2(POINT_1.x, POINT_1.y), new Vec2(POINT_2.x, POINT_2.y));
        return callback;
    }

}
