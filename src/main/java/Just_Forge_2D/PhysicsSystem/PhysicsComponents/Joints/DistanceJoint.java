package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.MainWindow;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.joml.Vector2f;


public class DistanceJoint extends Joint
{
    private Vector2f anchorPointA = new Vector2f();
    private Vector2f anchorPointB = new Vector2f();
    private float length;
    private float oscillationFrequency;
    private float dampingRatio;
    private transient org.jbox2d.dynamics.joints.DistanceJoint joint;


    public DistanceJoint(GameObject OTHER)
    {
        super(OTHER);
    }

    public DistanceJoint(RigidBodyComponent OTHER_RB)
    {
        super(OTHER_RB);
    }

    public DistanceJoint(){}

    @Override
    protected void createJoint()
    {
        if (joint != null)
        {
            Logger.FORGE_LOG_ERROR("Distance Joint has already been created");
            return;
        }
        if (otherRB == null)
        {
            Logger.FORGE_LOG_WARNING("Other RigidBody is null or not initialized, cannot create DistanceJoint");
            return;
        }

        RigidBodyComponent thisRB = this.gameObject.getComponent(RigidBodyComponent.class);
        if (thisRB == null || thisRB.getRawBody() == null)
        {
            Logger.FORGE_LOG_WARNING(this.gameObject + " does not have a valid RigidBodyComponent");
            return;
        }

        // - - - Initialize the DistanceJointDef
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.bodyA = thisRB.getRawBody();
        jointDef.bodyB = otherRB.getRawBody();

        // - - - Set anchor points for the joint (relative to the bodies' local coordinates)
        Vec2 anchorA = new Vec2(anchorPointA.x, anchorPointA.y);
        Vec2 anchorB = new Vec2(anchorPointB.x, anchorPointB.y);

        // - - - Set distance parameters
        jointDef.length = length;
        jointDef.frequencyHz = oscillationFrequency;
        jointDef.dampingRatio = dampingRatio;

        // - - - Create the joint in the physics world
        jointDef.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), anchorA, anchorB);
        this.joint = (org.jbox2d.dynamics.joints.DistanceJoint) MainWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(jointDef);

        Logger.FORGE_LOG_INFO("Distance Joint created between " + thisRB.gameObject.name + " and " + otherRB.gameObject.name);
    }

    public Vector2f getAnchorPointA()
    {
        if (this.joint != null)
        {
            Vec2 realAnchor = new Vec2();
            this.joint.getAnchorA(realAnchor);
            anchorPointA = new Vector2f(realAnchor.x, realAnchor.y);
        }
        return anchorPointA;
    }

    public Vector2f getAnchorPointB()
    {
        if (this.joint != null)
        {
            Vec2 realAnchor = new Vec2();
            this.joint.getAnchorB(realAnchor);
            anchorPointB = new Vector2f(realAnchor.x, realAnchor.y);
        }
        return anchorPointB;
    }

    public float getDampingRatio()
    {
        if (this.joint != null)
        {
            this.dampingRatio = this.joint.getDampingRatio();
        }
        return this.dampingRatio;
    }

    public float getOscillationFrequency()
    {
        if (this.joint != null)
        {
            this.dampingRatio = this.joint.getDampingRatio();
        }
        return this.dampingRatio;
    }

    public float getLength()
    {
        if (this.joint != null)
        {
            this.length = this.joint.getLength();
        }
        return this.length;
    }

    public void setLength(float LENGTH)
    {
        this.length = LENGTH;
        if (this.joint != null)
        {
            this.joint.setLength(LENGTH);
        }
    }

    public Vector2f getReactionForce(float INVERSE_DISTANCE)
    {
        if (this.joint == null)
        {
            Logger.FORGE_LOG_ERROR("No Joint means no force");
            return new Vector2f();
        }
        Vec2 real = new Vec2();
        this.joint.getReactionForce(INVERSE_DISTANCE, real);
        return new Vector2f(real.x, real.y);
    }

    public float getReactionTorque(float INVERSE_DISTANCE)
    {
        if (this.joint == null)
        {
            Logger.FORGE_LOG_ERROR("No Joint means no torque");
            return 0;
        }
        return this.joint.getReactionTorque(INVERSE_DISTANCE);
    }

    public void setOscillationFrequency(float HZ)
    {
        this.oscillationFrequency = HZ;
        if (this.joint != null)
        {
            this.joint.setFrequency(HZ);
        }
    }

    public void setDampingRatio(float RATIO)
    {
        this.dampingRatio = RATIO;
        if (this.joint != null)
        {
            this.joint.setDampingRatio(RATIO);
        }
    }

    public void setAnchorPoints(Vector2f ANCHOR_A, Vector2f ANCHOR_B)
    {
        this.anchorPointA = ANCHOR_A;
        this.anchorPointB = ANCHOR_B;

        if (this.joint != null)
        {
            // - - - Destroy the old joint
            MainWindow.getCurrentScene().getPhysics().rawWorld.getWorld().destroyJoint(this.joint);
            this.joint = null;
            createJoint();
        }
    }




    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (joint != null)
        {
            Vec2 pointA = new Vec2();
            Vec2 pointB = new Vec2();
            joint.getAnchorA(pointA);
            joint.getAnchorB(pointB);
            DebugPencil.addLine(new Vector2f(pointA.x, pointA.y), new Vector2f(pointB.x, pointB.y));
        }
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();
        setLength(Widgets.drawFloatControl("Length", getLength()));
        setDampingRatio(Widgets.drawFloatControl("Damping Ratio", getDampingRatio()));
        setOscillationFrequency(Widgets.drawFloatControl("Oscillation Frequency", getOscillationFrequency()));
        Vector2f anchorA = new Vector2f(getAnchorPointA());
        Widgets.drawVec2Control("Anchor A", anchorA);
        Vector2f anchorB = new Vector2f(getAnchorPointB());
        Widgets.drawVec2Control("Anchor B", anchorB);
        if (!anchorA.equals(getAnchorPointA()) || !anchorB.equals(getAnchorPointB()))
        {
            setAnchorPoints(anchorA, anchorB);
        }
    }
}