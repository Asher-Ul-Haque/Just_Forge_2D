package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.joml.Vector2f;

public class PistonComponent extends BaseJointComponent
{
    // - - - private variables
    private transient final PrismaticJointDef jointDef = new PrismaticJointDef();
    private transient PrismaticJoint joint;

    // - - - property variables
    protected boolean enableLimit = false;
    protected boolean enableMotor = false;
    protected float motorSpeed = 10f;
    protected float maxMotorForce = 20f;
    protected Vector2f limits = new Vector2f();
    protected float referenceAngle = 0f;
    protected Vector2f anchorA = new Vector2f();
    protected Vector2f anchorB = new Vector2f();
    protected Vector2f axis = new Vector2f();
    protected Vector2f pistonAnchor = new Vector2f();


    // - - - | Functions | - - -


    // - - - constructors - - -

    public PistonComponent(GameObject OTHER) { super(OTHER);}
    public PistonComponent(RigidBodyComponent OTHER_RB) {super(OTHER_RB);}
    public PistonComponent(){}

    // - - - creation og joint
    @Override
    protected void createJoint()
    {
        if (joint != null) joint = null;
        if (otherRB != null)
        {
            other = otherRB.gameObject;
        }
        if (other == null)
        {
            other = GameWindow.getCurrentScene().getGameObject(otherName);
            otherRB = other.getComponent(RigidBodyComponent.class);
        }
        if (other != null && otherRB != null)
        {
            jointDef.collideConnected = this.collideConnected;
            jointDef.enableLimit = this.enableLimit;
            jointDef.enableMotor = this.enableMotor;
            jointDef.motorSpeed = this.motorSpeed;
            jointDef.maxMotorForce = this.maxMotorForce;
            jointDef.lowerTranslation = this.limits.x;
            jointDef.upperTranslation = this.limits.y;
            jointDef.referenceAngle = this.referenceAngle;
            if (anchorA.lengthSquared() == 0) anchorA = new Vector2f(this.gameObject.transform.position);
            if (anchorB.lengthSquared() == 0) anchorB = new Vector2f(other.transform.position);
            if (this.axis.lengthSquared() == 0) axis = new Vector2f(1.0f, 0.0f);
            if (this.pistonAnchor.lengthSquared() == 0) pistonAnchor = this.gameObject.transform.position;
            jointDef.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(pistonAnchor.x, pistonAnchor.y), new Vec2(this.axis.x, this.axis.y));
            joint = (PrismaticJoint)  GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(jointDef);
        }
    }


    // - - - | Getters and Setters | - - -


    // - - - limits - - -

    // - - - enable
    public boolean isLimitEnabled()
    {
        return this.enableLimit;
    }

    public void enableLimit(boolean REALLY)
    {
        this.enableLimit = REALLY;
        if (this.joint != null) this.joint.enableLimit(REALLY);
    }

    // - - - get and set
    public void setLimits(Vector2f LIMITS)
    {
        this.limits = LIMITS;
        if (this.joint != null) this.joint.setLimits(LIMITS.x, LIMITS.y);
    }

    public Vector2f getLimits()
    {
        return this.limits;
    }


    // - - - Motor - - -

    // - - - enable
    public boolean isMotorEnabled() { return this.enableMotor; }

    public void enableMotor(boolean REALLY)
    {
        this.enableMotor = REALLY;
        if (this.joint != null) this.joint.enableMotor(REALLY);
    }

    // - - - speed
    public float getMotorSpeed()
    {
        return this.motorSpeed;
    }

    public void setMotorSpeed(float SPEED)
    {
        this.motorSpeed = SPEED;
        if (this.joint != null) this.joint.setMotorSpeed(SPEED);
    }

    // - - - force
    public float getMaxMotorForce()
    {
        return this.maxMotorForce;
    }

    public void setMaxMotorForce(float FORCE)
    {
        this.maxMotorForce = FORCE;
        if (this.joint != null) this.joint.setMaxMotorForce(FORCE);
    }


    // - - - Axis - - -

    // - - - reference angle
    public float getReferenceAngle() { return this.referenceAngle; }

    public void setReferenceAngle(float ANGLE)
    {
        this.referenceAngle = ANGLE;
        this.joint = null;
        createJoint();
    }

    // - - - vector
    public Vector2f getAxis() {return this.axis;}

    public void setAxis(Vector2f AXIS)
    {
        axis = AXIS;
        joint = null;
        createJoint();
    }


    // - - - Anchors - - -

    // - - - piston anchor
    public Vector2f getPistonAnchor() {return this.pistonAnchor;}

    public void setPistonAnchor(Vector2f ANCHOR)
    {
        pistonAnchor = ANCHOR;
        joint = null;
        createJoint();
    }

    // - - - anchorA
    public void setAnchorA(Vector2f ANCHOR)
    {
        this.anchorA = ANCHOR;
        joint = null;
        createJoint();
    }

    public Vector2f getAnchorA()
    {
        Vec2 temp = new Vec2();
        if (this.joint != null)
        {
            joint.getAnchorA(temp);
            this.anchorA.x = temp.x;
            this.anchorA.y = temp.y;
        }
        return this.anchorA;
    }

    // - - - anchor B
    public void setAnchorB(Vector2f ANCHOR)
    {
        this.anchorB = ANCHOR;
        joint = null;
        createJoint();
    }

    public Vector2f getAnchorB()
    {
        Vec2 temp = new Vec2();
        if (this.joint != null)
        {
            joint.getAnchorB(temp);
            this.anchorB.x = temp.x;
            this.anchorB.y = temp.y;
        }
        return this.anchorB;
    }


    // - - - Collision - - -

    public boolean isCollideConnected()
    {
        if (this.joint != null) this.collideConnected = joint.getCollideConnected();
        return this.collideConnected;
    }

    public void collideConnect(boolean REALLY)
    {
        this.collideConnected = REALLY;
    }


    // - - - Destruction - - -

    @Override
    public void destroy()
    {
        if (this.joint == null) return;
        GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().destroyJoint(joint);
    }


    // - - - Editor - - -

    @Override
    public void debugDraw()
    {
        if (other == null || joint == null) return;
        DebugPencil.addLine(this.gameObject.transform.position, other.transform.position);
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();

        Widgets.colorPicker3("Joint Color", color);

        Vector2f temp = new Vector2f(getPistonAnchor());
        Widgets.drawVec2Control("Piston Anchor", temp);
        if (!temp.equals(getPistonAnchor())) setPistonAnchor(temp);
        if (ImGui.button("Reset Piston Anchor"))
        {
            this.pistonAnchor = new Vector2f();
            this.joint = null;
            createJoint();
        }

        temp = new Vector2f(getLimits());
        Widgets.drawVec2Control("Translation Limits", temp);
        if (!temp.equals(getLimits())) setLimits(temp);

        temp = new Vector2f(getAxis());
        Widgets.drawVec2Control("Axis", temp);
        if (!temp.equals(getAxis())) setAxis(temp);

        temp = new Vector2f(getAnchorA());
        Widgets.drawVec2Control("Anchor A", temp);
        if (!temp.equals(getAnchorA())) setAnchorA(temp);
        if (ImGui.button("Reset Anchor A"))
        {
            this.anchorA = new Vector2f();
            this.joint = null;
            createJoint();
        }

        temp = new Vector2f(getAnchorB());
        Widgets.drawVec2Control("Anchor B", temp);
        if (!temp.equals(getAnchorB())) setAnchorB(temp);
        if (ImGui.button("Reset Anchor B"))
        {
            this.anchorB = new Vector2f();
            this.joint = null;
            createJoint();
        }

        collideConnect(Widgets.drawBoolControl("Collide with Connection", isCollideConnected()));

        float tempF = Widgets.drawFloatControl("Reference Angle", getReferenceAngle());
        if (tempF != getReferenceAngle()) setReferenceAngle(tempF);

        enableLimit(Widgets.drawBoolControl("Enable Limits", isLimitEnabled()));

        enableMotor(Widgets.drawBoolControl("Enable Motor", isMotorEnabled()));

        setMaxMotorForce(Widgets.drawFloatControl("Max Torque", getMaxMotorForce()));

        setMotorSpeed(Widgets.drawFloatControl("Motor Speed", getMotorSpeed()));
    }
}
