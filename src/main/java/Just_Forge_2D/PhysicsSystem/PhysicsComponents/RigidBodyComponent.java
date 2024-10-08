package Just_Forge_2D.PhysicsSystem.PhysicsComponents;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.Components.TransformComponent;
import Just_Forge_2D.PhysicsSystem.Enums.BodyType;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.MainWindow;
import imgui.ImGui;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

// - - - Rigid Body
public class RigidBodyComponent extends Component
{
    // - - - private variables
    public Vector2f velocity = new Vector2f();
    private float angularDamping = DefaultValues.ANGULAR_DAMPING;
    private float linearDamping = DefaultValues.LINEAR_DAMPING;
    private float density = DefaultValues.DEFAULT_MASS;
    private BodyType bodyType = BodyType.Dynamic;
    private boolean fixedRotation = DefaultValues.ROTATION_FIXED;
    private boolean continuousCollision = DefaultValues.CONTINUOUS_COLLISION;
    private transient Body rawBody = null;
    public float angularVelocity = 0.0f;
    public float gravityScale = DefaultValues.GRAVITY_SCALE;
    public float frictionCoefficient = DefaultValues.DEFAULT_FRICTION;
    public float restitutionCoefficient = DefaultValues.DEFAULT_RESTITUTION;
    private boolean isSensor = false;
    private boolean isEditor = false;


    // - - - | Functions | - - -


    // - - - Velocity - - -

    public Vector2f getVelocity()
    {
        return velocity;
    }

    public void setVelocity(Vector2f VELOCITY)
    {
        this.velocity.set(VELOCITY);
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant set velocity on a null raw body");
            return;
        }
        this.rawBody.setLinearVelocity(new Vec2(velocity.x, velocity.y));
    }

    public void addVelocity(Vector2f VELOCITY)
    {
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant add velocity to a null raw body");
            return;
        }
        this.setDensity(this.getDensity());
        rawBody.setLinearVelocity(rawBody.getLinearVelocity().add(new Vec2(VELOCITY.x, VELOCITY.y)));
    }

    public Vector2f getLinearVelocity()
    {
        return new Vector2f(rawBody.getLinearVelocity().x, rawBody.getLinearVelocity().y);
    }


    // - - - Transform - - -

    public void setTransform(TransformComponent TRANSFORM)
    {
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant set velocity on a null raw body");
            return;
        }
        this.rawBody.setTransform(new Vec2(TRANSFORM.position.x, TRANSFORM.position.y), TRANSFORM.rotation);
    }

    public void setTransform(Vector2f POSITION, float ROTATION)
    {
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant set velocity on a null raw body");
            return;
        }
        this.rawBody.setTransform(new Vec2(POSITION.x, POSITION.y), ROTATION);
    }


    // - - - Angular Damping - - -

    public float getAngularDamping()
    {
        return this.angularDamping;
    }

    public void setAngularDamping(float ANGULAR_DAMPING)
    {
        this.angularDamping = ANGULAR_DAMPING;
        if (rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add angular velocity to a null raw body");
            return;
        }
        this.rawBody.setAngularDamping(ANGULAR_DAMPING);
    }


    // - - - Angular Velocity - - -

    public float getAngularVelocity()
    {
        return this.angularVelocity;
    }

    public void setAngularVelocity(float ANGULAR_VELOCITY)
    {
        this.angularVelocity = ANGULAR_VELOCITY;
        if (rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add angular velocity to a null raw body");
            return;
        }
        this.rawBody.setAngularVelocity(ANGULAR_VELOCITY);
    }


    // - - - gravity scale - - -

    public void setGravityScale(float GRAVITY)
    {
        this.gravityScale = GRAVITY;
        if (rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add gravity to a null raw body");
            return;
        }
        this.rawBody.setGravityScale(GRAVITY);
    }

    public float getGravityScale()
    {
        return this.gravityScale;
    }


    // - - - sensor - - -

    public void setSensor(boolean REALLY)
    {
        isSensor = REALLY;
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant set or unset as sensor a null raw body");
            return;
        }
        MainWindow.getPhysicsSystem().setSensor(this, REALLY);
    }

    public boolean isSensor()
    {
        return isSensor;
    }


    // - - - Linear Damping - - -

    public float getLinearDamping()
    {
        return this.linearDamping;
    }

    public void setLinearDamping(float LINEAR_DAMPING)
    {
        this.linearDamping = LINEAR_DAMPING;
        if (rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add linear damping to a null raw body");
            return;
        }
        this.rawBody.setLinearDamping(LINEAR_DAMPING);
    }


    // - - - Friction - - -

    public float getFrictionCoefficient()
    {
        return this.frictionCoefficient;
    }

    public void setFrictionCoefficient(float COEFF)
    {
        this.frictionCoefficient = Math.max(0, Math.min(1.0f, COEFF));
        if (rawBody == null || rawBody.getFixtureList() == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant set frictional coefficient to a null raw body");
            return;
        }
        this.rawBody.getFixtureList().setFriction(COEFF);
    }


    // - - - Bounciness - - -

    public float getRestitutionCoefficient()
    {
        return this.restitutionCoefficient;
    }

    public void setRestitutionCoefficient(float COEFF)
    {
        this.restitutionCoefficient = Math.max(0, Math.min(1.0f, COEFF));
        if (rawBody == null || rawBody.getFixtureList() == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add restitution to a null raw body");
            return;
        }
        this.rawBody.getFixtureList().setRestitution(COEFF);
    }


    // - - - Mass - - -

    public float getDensity()
    {
        return density;
    }

    public void setDensity(float DENSITY)
    {
        this.density = DENSITY;
        if (rawBody == null || rawBody.getFixtureList() == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add mass to a null raw body");
            return;
        }
        this.rawBody.getFixtureList().setDensity(DENSITY);
        this.rawBody.resetMassData();
    }

    public float getMass()
    {
        if (rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add mass to a null raw body");
            return -1f;
        }
        return rawBody.getMass();
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


    // - - - Force - - -

    public void addForce(Vector2f FORCE)
    {
        if (!rawBodyCheck("Force")) return;
        rawBody.applyForce(new Vec2(FORCE.x, FORCE.y), rawBody.getWorldCenter());
    }

    public void addForce(Vector2f FORCE, Vector2f POINT)
    {
        if (!rawBodyCheck("Force")) return;
        rawBody.applyForce(new Vec2(FORCE.x, FORCE.y), new Vec2(POINT.x, POINT.y));
    }


    // - - - Torque - - -

    public void addTorque(float TORQUE)
    {
        if (!rawBodyCheck("Torque")) return;
        rawBody.applyTorque(TORQUE);
    }


    // - - - Impulse - - -

    public void addImpulse(Vector2f IMPULSE)
    {
        if (!rawBodyCheck("Impulse")) return;
        addImpulse(IMPULSE, new Vector2f(rawBody.getWorldCenter().x, rawBody.getWorldCenter().y));
    }

    public void addImpulse(Vector2f IMPULSE, Vector2f POINT)
    {
        this.rawBody.applyLinearImpulse(new Vec2(IMPULSE.x, IMPULSE.y), new Vec2(POINT.x, POINT.y), true);
    }


    // - - - update - - -
    @Override
    public void update(float DELTA_TIME)
    {
        if (rawBody != null)
        {
            if (this.bodyType == BodyType.Dynamic || this.bodyType == BodyType.Kinematic)
            {
                this.gameObject.transform.position.set(rawBody.getPosition().x, rawBody.getPosition().y);
                this.gameObject.transform.rotation = rawBody.getAngle();
                Vec2 vel = rawBody.getLinearVelocity();
                this.velocity.set(vel.x, vel.y);
            }
            else if (this.bodyType == BodyType.Static)
            {
                this.rawBody.setTransform(new Vec2(this.gameObject.transform.position.x, this.gameObject.transform.position.y), this.gameObject.transform.rotation);
            }
        }
    }


    // - - - Editor UI - - -

    @Override
    public void editorGUI()
    {
        isEditor = true;
        // - - - destroy button
        if (ImGui.button("Destroy"))
        {
            this.gameObject.removeComponent(this.getClass());
        }

        // - - - body type

        Enum t = Widgets.drawEnumControls(BodyType.class, "Body Type", bodyType);
        if (t != null)
        {
            bodyType = (BodyType) t;
        }

        // - - - mass
        setDensity(Widgets.drawFloatControl("Density", getDensity()));

        // - - - gravity scale
        setGravityScale(Widgets.drawFloatControl("Gravity Scale", gravityScale));


        // - - - linear damping
        setLinearDamping(Widgets.drawFloatControl("Linear Damping", linearDamping));


        // - - - angular damping
        setAngularDamping(Widgets.drawFloatControl("Angular Damping", angularVelocity));

        // - - - friction
        setFrictionCoefficient(Widgets.drawFloatControl("Friction Coeff", frictionCoefficient));

        // - - - restitution
        setRestitutionCoefficient(Widgets.drawFloatControl("Restitution", restitutionCoefficient));

        // - - - fixed rotation, continuous collision and is Sensor
        fixedRotation = Widgets.drawBoolControl("Fixed Rotation", fixedRotation);

        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        boolean val = continuousCollision;
        if (ImGui.checkbox("Continuous Collision", val))
        {
            val = !val;
            continuousCollision = val;
        }
        val = isSensor;
        if (ImGui.checkbox("isSensor", val))
        {
            val = !val;
            isSensor = val;
        }
        Theme.resetDefaultTextColor();

        isEditor = false;
    }


    // - - - Helper functions
    private boolean rawBodyCheck(String MESSAGE)
    {
        if (this.rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add " + MESSAGE + " to a  null raw body");
            return false;
        }
        this.setDensity(this.getDensity());
        return true;
    }
}