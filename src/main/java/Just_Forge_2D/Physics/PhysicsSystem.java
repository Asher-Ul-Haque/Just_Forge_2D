package Just_Forge_2D.Physics;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class PhysicsSystem
{
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);
    private float physicsTime = 0.0f;
    private float physicsTimeDelta = 1.0f / 60.0f;
    private int vecolictyIterations = 8;
    private int positionIterations = 3;

    public void update(float DELTA_TIME)
    {
        // - - - ensure that update happens only every 16 milliseconds. We increase only when we pass 16ms
        physicsTime += DELTA_TIME;
        if (physicsTime >= 0.0f)
        {
            physicsTime -= physicsTimeDelta;
            world.step(physicsTimeDelta, vecolictyIterations, positionIterations);
        }
    }
}
