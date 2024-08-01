package Just_Forge_2D.Physics.RigidBody;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import org.joml.Vector2f;

public class RigidBody extends Component
{
    // - - - Private variables - - -

    private final Vector2f forceAccumulator = new Vector2f();
    private boolean fixedRotation;

    private float mass = 0.0f;
    private float inverseMass = 0.0f;

    private TransformComponent rawTransform;
    private final Vector2f position = new Vector2f();
    private final Vector2f velocity = new Vector2f();

    private float rotation = 0.0f; // radians
    private float angularVelocity = 0.0f;

    private float friction = 0.0f;
    private float angularFriction = 0.0f;


    // - - - | Functions | - - -


    // - - - default constructor
    public RigidBody(){}


    // - - - Mass - - -

    public float getMass()
    {
        return mass;
    }

    public void setMass(float mass)
    {
        this.mass = mass;
        if (this.mass != 0.0f)
        {
            this.inverseMass = 1.0f / this.mass;
        }
    }


    // - - - Transform - - -

    public void setTransform(Vector2f POSITION, float ROTATION)
    {
        this.position.set(POSITION);
        this.rotation = ROTATION;
    }

    public void setTransform(Vector2f POSITION)
    {
        this.position.set(POSITION);
    }

    public void setRawTransform(TransformComponent rawTransform)
    {
        this.rawTransform = rawTransform;
        this.position.set(rawTransform.position);
    }

    public void syncCollisionTransforms()
    {
        if (this.rawTransform != null)
        {
            this.rawTransform.position.set(this.position);
        }
    }


    // - - - Stuff related to rotation - - -

    public float getRotation()
    {
        return rotation;
    }


    // - - - Stuff related to position - - -

    public Vector2f getPosition()
    {
        return position;
    }


    // - - - Stuff related to forces - - -

    public void addForce(Vector2f FORCE)
    {
        this.forceAccumulator.add(FORCE);
    }

    public void clearAccumulator()
    {
        this.forceAccumulator.zero();
    }


    // - - - update
    public void physicsUpdate(float DELTA_TIME)
    {
        if (this.mass == 0.0f) return;

        // - - - calculate linear velocity
        Vector2f acceleration = new Vector2f(forceAccumulator).mul(this.inverseMass); //newtons second law
        this.velocity.add(acceleration.mul(DELTA_TIME));

        // - - - calculate position
        this.position.add(new Vector2f(velocity).mul(DELTA_TIME));

        syncCollisionTransforms();
        clearAccumulator();
    }
}
