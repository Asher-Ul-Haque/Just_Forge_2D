package Just_Forge_2D.Core.ECS.Components.PhysicsComponents;

import Just_Forge_2D.Core.ECS.Components.Component;
import Just_Forge_2D.Physics.Enums.BodyType;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

public class RigidBodyComponent extends Component
{
    private Vector2f velocity = new Vector2f();
    private float angularDamping = 0.0f;
    private float linearDamping = 0.9f;
    private float mass = 0;
    private BodyType bodyType = BodyType.Dynamic;

    private boolean fixedRotation = false;
    private boolean continousCollission = true;

    private Body rawBody = null;

    public Vector2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2f velocity) {
        this.velocity = velocity;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isContinousCollission() {
        return continousCollission;
    }

    public void setContinousCollission(boolean continousCollission) {
        this.continousCollission = continousCollission;
    }

    public Body getRawBody() {
        return rawBody;
    }

    public void setRawBody(Body rawBody) {
        this.rawBody = rawBody;
    }

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
