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
    private float mass = DefaultValues.DEFAULT_MASS;
    private BodyType bodyType = BodyType.Dynamic;
    private boolean fixedRotation = DefaultValues.ROTATION_FIXED;
    private boolean continuousCollision = DefaultValues.CONTINUOUS_COLLISION;
    private transient Body rawBody = null;
    public float angularVelocity = 0.0f;
    public float gravityScale = DefaultValues.GRAVITY_SCALE;
    private boolean isSensor = false;


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


    // - - - Angular Damping - - -

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float ANGULAR_DAMPING)
    {
        this.angularDamping = ANGULAR_DAMPING;
    }

    public void setAngularVelocity(float ANGULAR_VELOCITY)
    {
        this.angularVelocity = ANGULAR_VELOCITY;
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant add angular velocity to a null raw body");
            return;
        }
        this.rawBody.setAngularVelocity(ANGULAR_VELOCITY);
    }

    public void setGravityScale(float GRAVITY)
    {
        this.gravityScale = GRAVITY;
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant add gravity to a null raw body");
            return;
        }
        this.rawBody.setGravityScale(GRAVITY);
    }

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
        rawBody.setLinearVelocity(rawBody.getLinearVelocity().add(new Vec2(VELOCITY.x, VELOCITY.y)));
    }

    public void addForce(Vector2f FORCE)
    {
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant add force to a null raw body");
            return;
        }
        rawBody.applyForceToCenter(new Vec2(FORCE.x, FORCE.y));
    }

    public void addImpulse(Vector2f IMPULSE)
    {
        if (rawBody == null)
        {
            Logger.FORGE_LOG_ERROR("Cant add impulse toa  null raw body");
            return;
        }
        rawBody.applyLinearImpulse(new Vec2(IMPULSE.x, IMPULSE.y), rawBody.getWorldCenter());
    }

    public Vec2 getLinearVelocity()
    {
        return rawBody.getLinearVelocity();
    }


    // - - - Editor UI - - -

    @Override
    public void editorGUI()
    {
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
        setMass(Widgets.drawFloatControl("Mass", getMass()));

        // - - - gravity scale
        setGravityScale(Widgets.drawFloatControl("Gravity Scale", gravityScale));


        // - - - linear damping
        Widgets.drawFloatControl("Linear Damping", linearDamping);


        // - - - angular damping
        Widgets.drawFloatControl("Angular Damping", angularVelocity);

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
    }
}