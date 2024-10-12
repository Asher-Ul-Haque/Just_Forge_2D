package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.GameWindow;
import imgui.ImGui;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.joml.Vector2f;


public class SpringComponent extends BaseJointComponent
{
    // - - - private variables
    private transient final DistanceJointDef defJoint = new DistanceJointDef ();
    private transient DistanceJoint joint;

    // - - - property variables
    protected float length = 1f;
    protected float dampingRatio = 0.5f;
    protected float springConstant = 2f;
    protected Vector2f anchorA = new Vector2f();
    protected Vector2f anchorB = new Vector2f();


    // - - - | Functions | - - -


    // - - - constructors - - -

    public SpringComponent(GameObject OTHER)
    {
        super(OTHER);
    }
    public SpringComponent(RigidBodyComponent OTHER_RB)
    {
        super(OTHER_RB);
    }
    public SpringComponent() {}

    // - - - creation of joint
    @Override
    public void createJoint()
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
        RigidBodyComponent my = this.gameObject.getComponent(RigidBodyComponent.class);
        if (my == null)
        {
            Logger.FORGE_LOG_ERROR(this.gameObject + " does not have a rigid body");
            return;
        }
        if (other != null && otherRB != null)
        {
            defJoint.dampingRatio = getDampingRatio();
            defJoint.length = getLength();
            defJoint.frequencyHz = getSpringConstant();
            defJoint.collideConnected = isCollideConnected();
            if (anchorA.lengthSquared() == 0) anchorA = new Vector2f(this.gameObject.transform.position);
            if (anchorB.lengthSquared() == 0) anchorB = new Vector2f(other.transform.position);
            defJoint.initialize(my.getRawBody(), otherRB.getRawBody(), new Vec2(anchorA.x, anchorA.y), new Vec2(anchorB.x, anchorB.y));
            joint = (DistanceJoint) GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(defJoint);
        }
    }


    // - - - | Getters and Setters | - - -


    // - - - length - - -

    public float getLength()
    {
        return this.length;
    }

    public void setLength(float LENGTH)
    {
        this.length = LENGTH;
        if (this.joint != null) this.joint.setLength(LENGTH);
    }


    // - - - damping ratio - - -

    public float getDampingRatio()
    {
        return this.dampingRatio;
    }

    public void setDampingRatio(float RATIO)
    {
        this.dampingRatio = RATIO;
        if (this.joint != null) this.joint.setDampingRatio(RATIO);
    }


    // - - - SHM - - -

    public float getSpringConstant()
    {
        return this.springConstant;
    }

    public void setSpringConstant(float FREQUENCY)
    {
        this.springConstant = FREQUENCY;
        if (this.joint != null) this.joint.setFrequency(FREQUENCY);
    }


    // - - - collide connected - - -

    public boolean isCollideConnected()
    {
        if (this.joint != null) this.collideConnected = joint.getCollideConnected();
        return this.collideConnected;
    }

    public void collideConnect(boolean REALLY)
    {
        this.collideConnected = REALLY;
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

    public void setAnchorA(Vector2f ANCHOR)
    {
        this.anchorA = ANCHOR;
        joint = null;
        createJoint();
    }

    public void setAnchorB(Vector2f ANCHOR)
    {
        this.anchorB = ANCHOR;
        joint = null;
        createJoint();
    }


    // - - - editor


    @Override
    public void editorGUI()
    {
        super.editorGUI();
        Widgets.colorPicker3("Joint Color", color);
        setDampingRatio(Widgets.drawFloatControl("Damping Ratio", getDampingRatio()));
        setSpringConstant(Widgets.drawFloatControl("SHM Frequency Hz", getSpringConstant()));
        setLength(Widgets.drawFloatControl("Length", getLength()));
        collideConnect(Widgets.drawBoolControl("Collide with Connection", isCollideConnected()));
        Vector2f temp = new Vector2f(getAnchorA());
        Widgets.drawVec2Control("Anchor A", temp);
        if (!temp.equals(getAnchorA()))
        {
            setAnchorA(temp);
        }
        if (ImGui.button("Reset Anchor A"))
        {
            this.anchorA = new Vector2f();
            this.joint = null;
            createJoint();
        }
        temp = new Vector2f(getAnchorB());
        Widgets.drawVec2Control("Anchor B", temp);
        if (!temp.equals(getAnchorB()))
        {
            setAnchorB(temp);
        }
        if (ImGui.button("Reset Anchor B"))
        {
            this.anchorB = new Vector2f();
            this.joint = null;
            createJoint();
        }
    }

    @Override
    public void destroy()
    {
        if (this.joint == null) return;
        GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().destroyJoint(joint);
    }
}