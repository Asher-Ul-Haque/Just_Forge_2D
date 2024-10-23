package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.joml.Vector2f;

public class HingeComponent extends BaseJointComponent
{
    // - - - private varianbles
    private transient final RevoluteJointDef defJoint = new RevoluteJointDef();
    private transient RevoluteJoint joint;

    // - - - property variables
    protected Vector2f anchorA = new Vector2f();
    protected Vector2f anchorB = new Vector2f();
    protected Vector2f hingeAnchor = new Vector2f();
    protected float referenceAngle = 0f;
    protected boolean enableLimit = false;
    protected Vector2f limits = new Vector2f((float) (Math.PI / 2f), -(float) (Math.PI / 2f));
    protected boolean enableMotor = true;
    protected float motorSpeed = 5f;
    protected float maxMotorTorque = 20f;


    // - - - | Functions | - - -


    // - - - | Getters And Setters | - - -


    // - - - anchors - - -

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

    // - - - hinge anchor
    public void setHingeAnchor(Vector2f ANCHOR)
    {
        hingeAnchor = ANCHOR;
        joint = null;
        createJoint();
    }

    public Vector2f getHingeAnchor()
    {
        return hingeAnchor;
    }


    // - - - Collide
    public boolean isCollideConnected()
    {
        if (this.joint != null) this.collideConnected = joint.getCollideConnected();
        return this.collideConnected;
    }

    public void collideConnect(boolean REALLY)
    {
        this.collideConnected = REALLY;
    }


    // - - - Angles - - -

    // - - - reference
    public float getReferenceAngle() { return this.referenceAngle; }

    public void setReferenceAngle(float ANGLE)
    {
        this.referenceAngle = ANGLE;
        this.joint = null;
        createJoint();
    }

    // - - - limit enable
    public boolean isLimitEnabled()
    {
        return enableLimit;
    }

    public void enableLimit(boolean REALLY)
    {
        this.enableLimit = REALLY;
        if (this.joint != null) this.joint.enableLimit(REALLY);
    }

    // - - - limits
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
    public boolean isMotorEnabled()
    {
        return this.enableMotor;
    }

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

    // - - - torque
    public float getMaxMotorTorque()
    {
        return this.maxMotorTorque;
    }

    public void setMaxMotorTorque(float TORQUE)
    {
        this.maxMotorTorque = TORQUE;
        if (this.joint != null) this.joint.setMaxMotorTorque(TORQUE);
    }

    // - - - constructors - - -

    public HingeComponent(GameObject OTHER) {super(OTHER);}
    public HingeComponent(RigidBodyComponent OTHER_RB) {super(OTHER_RB);}
    public HingeComponent(){};

    // - - - creation of a joint
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
            defJoint.referenceAngle = this.referenceAngle;
            defJoint.enableLimit = this.enableLimit;
            defJoint.enableMotor = this.enableMotor;
            defJoint.collideConnected = this.collideConnected;
            defJoint.upperAngle = this.limits.x;
            defJoint.lowerAngle = this.limits.y;
            defJoint.maxMotorTorque = this.maxMotorTorque;
            defJoint.motorSpeed = this.motorSpeed;
            // - - - TODO: abstract stuff out check for null instead of 0.
            if (anchorA.lengthSquared() == 0) anchorA = new Vector2f(this.gameObject.transform.position);
            if (anchorB.lengthSquared() == 0) anchorB = new Vector2f(other.transform.position);
            if (hingeAnchor.lengthSquared() == 0) hingeAnchor = this.gameObject.transform.position;
            defJoint.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(hingeAnchor.x, hingeAnchor.y));
            joint = (RevoluteJoint) GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(defJoint);
        }
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();

        Widgets.colorPicker3("Joint Color", color);

        Vector2f temp = new Vector2f(getHingeAnchor());
        Widgets.drawVec2Control("Hinge Anchor", temp);
        if (!temp.equals(getHingeAnchor())) setHingeAnchor(temp);
        if (ImGui.button("Reset Hinge Anchor"))
        {
            this.hingeAnchor = new Vector2f();
            this.joint = null;
            createJoint();
        }

        temp = new Vector2f(getLimits());
        Widgets.drawVec2Control("Angle Limits", temp);
        if (!temp.equals(getLimits())) setLimits(temp);

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

        setMaxMotorTorque(Widgets.drawFloatControl("Max Torque", getMaxMotorTorque()));

        setMotorSpeed(Widgets.drawFloatControl("Motor Speed", getMotorSpeed()));
    }

    @Override
    public void destroy()
    {
        if (this.joint == null) return;
        GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().destroyJoint(joint);
    }

    @Override
    public void debugDraw()
    {
        if (other == null) return;
        DebugPencil.addLine(this.gameObject.transform.position, other.transform.position, color);
    }
}