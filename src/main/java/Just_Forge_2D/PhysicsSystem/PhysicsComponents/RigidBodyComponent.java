package Just_Forge_2D.PhysicsSystem.PhysicsComponents;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.PhysicsSystem.Enums.BodyType;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import imgui.type.ImInt;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.joml.Vector2f;

import java.util.Arrays;

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
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant set velocity on a null raw body");
            return;
        }
        this.rawBody.setLinearVelocity(new Vec2(velocity.x, velocity.y));
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

    public void addForce(Vector2f FORCE)
    {
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant add force to a null raw body");
            return;
        }
        this.setDensity(this.getDensity());
        rawBody.applyForceToCenter(new Vec2(FORCE.x, FORCE.y));
    }

    public void addImpulse(Vector2f IMPULSE)
    {
        if (this.rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add impulse toa  null raw body");
            return;
        }
        this.setDensity(this.getDensity());
        this.rawBody.applyLinearImpulse(new Vec2(IMPULSE.x, IMPULSE.y), this.rawBody.getWorldCenter(), true);
    }

    public void addTorque(float TORQUE)
    {
        if (rawBody == null)
        {
            if (!isEditor) Logger.FORGE_LOG_ERROR("Cant add torque to a null raw body");
            return;
        }
        this.setDensity(this.getDensity());
        rawBody.applyTorque(TORQUE);
    }

    public Vec2 getLinearVelocity()
    {
        return rawBody.getLinearVelocity();
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
        String[] enumValues = Arrays.stream(BodyType.values())
                .map(Enum::name)
                .toArray(String[]::new);
        String enumType = (bodyType).name();
        ImInt index = new ImInt(Arrays.asList(enumValues).indexOf(enumType));
        if (ImGui.combo("Body Type", index, enumValues, enumValues.length))
        {
            bodyType = BodyType.values()[index.get()];
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
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        boolean val = fixedRotation;
        if (ImGui.checkbox("Fixed rotation", val))
        {
            val = !val;
            fixedRotation = val;
        }
        val = continuousCollision;
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
}