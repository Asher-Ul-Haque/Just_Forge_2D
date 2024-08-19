package Just_Forge_2D.PhysicsSystem;

import Just_Forge_2D.Utils.Configurations;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class PhysicsWorld
{
    protected final World world;
    private float physicsTime = 0.0f;
    private float physicsDeltaTime = Configurations.PHYSICS_DELTA_TIME;
    private int velocityIterations = Configurations.VELOCITY_ITERATIONS;
    private int positionIterations = Configurations.POSITION_ITERATIONS;

    public PhysicsWorld(Vector2f GRAVITY, float DELTA_TIME, int VELOCITY_ITERATIONS, int POSITION_ITERATIONS)
    {
        this.world = new World(new Vec2(GRAVITY.x, GRAVITY.y));
        this.world.setContactListener(new CollisionDetector());
        this.physicsDeltaTime = DELTA_TIME;
        this.velocityIterations = VELOCITY_ITERATIONS;
        this.positionIterations = POSITION_ITERATIONS;
    }

    public PhysicsWorld()
    {
        this.world = new World(Configurations.GRAVITY);
        this.world.setContactListener(new CollisionDetector());
    }

    protected void step(float DELTA_TIME)
    {
        this.physicsTime += DELTA_TIME;
        if (physicsTime > 0.0f)
        {
            physicsTime -= physicsDeltaTime;
            world.step(physicsDeltaTime, velocityIterations, positionIterations);
        }
    }

    protected World getWorld()
    {
        return this.world;
    }
}