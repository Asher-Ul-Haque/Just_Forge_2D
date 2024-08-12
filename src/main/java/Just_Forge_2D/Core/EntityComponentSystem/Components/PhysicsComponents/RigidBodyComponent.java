package Just_Forge_2D.Core.EntityComponentSystem.Components.PhysicsComponents;

import Just_Forge_2D.Core.EntityComponentSystem.Components.Component;
import Just_Forge_2D.Physics.Enums.BodyType;
import Just_Forge_2D.Utils.Configurations;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

// - - - Rigid Body
public class RigidBodyComponent extends Component
{
    // - - - private variables
    private Vector2f velocity = new Vector2f();
    private float angularDamping = Configurations.ANGULAR_DAMPING;
    private float linearDamping = Configurations.LINEAR_DAMPING;
    private float mass = Configurations.DEFAULT_MASS;
    private BodyType bodyType = BodyType.Dynamic;
    private boolean fixedRotation = Configurations.ROTATION_FIXED;
    private boolean continuousCollision = Configurations.CONTINUOUS_COLLISION;
    private transient Body rawBody = null;


    // - - - | Functions | - - -


    // - - - Velocity - - -

    public Vector2f getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2f VELOCITY)
    {
        this.velocity = VELOCITY;
    }


    // - - - Angular Damping - - -

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float ANGULAR_DAMPING)
    {
        this.angularDamping = ANGULAR_DAMPING;
    }


    // - - - Linear Damping - - -

    public float getLinearDamping()
    {
        return linearDamping;
    }

    public void setLinearDamping(float LINEAR_DAMPING)
    {
        this.linearDamping = LINEAR_DAMPING;
    }


    // - - - Mass - - -

    public float getMass()
    {
        return mass;
    }

    public void setMass(float MASS)
    {
        this.mass = MASS;
    }


    // - - - Body Type - - -

    public BodyType getBodyType()
    {
        return bodyType;
    }

    public void setBodyType(BodyType TYPE)
    {
        this.bodyType = TYPE;
    }


    // - - - Fixed Rotation - - -

    public boolean isFixedRotation()
    {
        return fixedRotation;
    }

    public void setFixedRotation(boolean REALLY)
    {
        this.fixedRotation = REALLY;
    }


    // - - - Continuous Collision - - -

    public boolean isContinuousCollision()
    {
        return continuousCollision;
    }

    public void setContinuousCollision(boolean REALLY)
    {
        this.continuousCollision = REALLY;
    }


    // - - - Raw Body - - -

    public Body getRawBody()
    {
        return rawBody;
    }

    public void setRawBody(Body BODY)
    {
        this.rawBody = BODY;
    }


    // - - - update
    @Override
    public void update(float DELTA_TIME)
    {
        if (rawBody != null)
        {
            this.gameObject.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
            this.gameObject.transform.rotation = rawBody.getAngle();
        }
    }
}
