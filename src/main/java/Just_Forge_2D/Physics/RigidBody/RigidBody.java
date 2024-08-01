package Just_Forge_2D.Physics.RigidBody;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Core.ECS.Components.Unattachable.TransformComponent;
import org.joml.Vector2f;

public class RigidBody extends Component
{
    private TransformComponent rawTransform;
    private Vector2f forceAccumulator = new Vector2f();
    private Vector2f position = new Vector2f();
    private float rotation = 0.0f; // degrees
    private Vector2f velocity = new Vector2f();
    private float angularVelocity = 0.0f;
    private float friction = 0.0f;
    private float angularFriction = 0.0f;
    private boolean fixedRotation;

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

    private float mass = 0.0f;
    private float inverseMass = 0.0f;

    public float getRotation()
    {
        return rotation;
    }

    public void setTransform(Vector2f POSITION, float ROTATION)
    {
        this.position.set(POSITION);
        this.rotation = ROTATION;
    }

    public void setTransform(Vector2f POSITION)
    {
        this.position.set(POSITION);
    }

    public Vector2f getPosition()
    {
        return position;
    }

    public void physicsUpdate(float DELTA_TIME)
    {
        if (this.mass == 0.0f) return;

        // - - - calculate linear velocuty
        Vector2f acceleration = new Vector2f(forceAccumulator).mul(this.inverseMass); //newtons second law
        this.velocity.add(acceleration.mul(DELTA_TIME));

        // - - - calculate position
        this.position.add(new Vector2f(velocity).mul(DELTA_TIME));

        syncCollisionTransforms();
        clearAccumulator();
    }

    public void syncCollisionTransforms()
    {
        if (this.rawTransform != null)
        {
            this.rawTransform.position.set(this.position);
        }
    }

    public void clearAccumulator()
    {
        this.forceAccumulator.zero();
    }

    public void addForce(Vector2f FORCE)
    {
        this.forceAccumulator.add(FORCE);
    }

    public void setRawTransform(TransformComponent rawTransform)
    {
        this.rawTransform = rawTransform;
        this.position.set(rawTransform.position);
    }
}
