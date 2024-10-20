package Just_Forge_2D.PhysicsSystem;

import Just_Forge_2D.PhysicsSystem.PhysicsManagers.CollisionManager;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.Utils.Settings;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;

public class PhysicsWorld
{
    protected final World world;
    private float physicsTime = 0.0f;
    private float physicsDeltaTime = Settings.PHYSICS_DELTA_TIME;
    private int velocityIterations = Settings.VELOCITY_ITERATIONS;
    private int positionIterations = Settings.POSITION_ITERATIONS;


    // - - - | Functions | - - -


    // - - - Constructors - - -

    public PhysicsWorld(Vector2f GRAVITY, float DELTA_TIME, int VELOCITY_ITERATIONS, int POSITION_ITERATIONS)
    {
        this.world = new World(new Vec2(GRAVITY.x, GRAVITY.y));
        this.world.setContactListener(new CollisionManager());
        this.physicsDeltaTime = DELTA_TIME;
        this.velocityIterations = VELOCITY_ITERATIONS;
        this.positionIterations = POSITION_ITERATIONS;
        Logger.FORGE_LOG_INFO("Created new Physics World");
    }

    public PhysicsWorld()
    {
        this.world = new World(Settings.GRAVITY);
        this.world.setContactListener(new CollisionManager());
        Logger.FORGE_LOG_INFO("Created new Physics World");
    }


    // - - - Other
    protected void step(float DELTA_TIME)
    {
        this.physicsTime += DELTA_TIME;
        if (physicsTime > 0.0f)
        {
            physicsTime -= physicsDeltaTime;
            world.step(physicsDeltaTime, velocityIterations, positionIterations);
        }
    }

    public World getWorld()
    {
        return this.world;
    }
}