package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.MainWindow;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.joml.Vector2f;


public class DistanceJointComponent extends BaseJointComponent
{
    // - - - private variables
    private transient final DistanceJointDef defJoint = new DistanceJointDef ();
    private transient DistanceJoint joint;

    // - - - property variables
    protected float length = 1f;
    protected float dampingRatio = 0.5f;
    protected float shmFrequency = 20f;
    protected boolean collideConnected = false;


    // - - - | Functions | - - -


    // - - - constructors - - -

    public DistanceJointComponent(GameObject OTHER)
    {
        super(OTHER);
    }
    public DistanceJointComponent(RigidBodyComponent OTHER_RB)
    {
        super(OTHER_RB);
    }
    public DistanceJointComponent() {}

    // - - - creation of joint
    @Override
    public void createJoint()
    {
        if (other == null)
        {
            other = MainWindow.getCurrentScene().getGameObject(otherName);
            otherRB = other.getComponent(RigidBodyComponent.class);
        }
        if (other != null && otherRB != null)
        {
            defJoint.dampingRatio = getDampingRatio();
            defJoint.length = getLength();
            defJoint.frequencyHz = getSHMFrequency();
            defJoint.collideConnected = isCollideConnected();
            defJoint.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(this.gameObject.transform.position.x, this.gameObject.transform.position.y), new Vec2(other.transform.position.x, other.transform.position.y));
            joint = (DistanceJoint) MainWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(defJoint);
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

    public float getSHMFrequency()
    {
        return this.shmFrequency;
    }

    public void setSHMFrequency(float FREQUENCY)
    {
        this.dampingRatio = FREQUENCY;
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


    // - - - editor


    @Override
    public void editorGUI()
    {
        super.editorGUI();
        setDampingRatio(Widgets.drawFloatControl("Damping Ratio", getDampingRatio()));
        setSHMFrequency(Widgets.drawFloatControl("SHM Frequency Hz", getSHMFrequency()));
        setLength(Widgets.drawFloatControl("Length", getLength()));
        collideConnect(Widgets.drawBoolControl("Collide with Connection", isCollideConnected()));
    }

    // - - - debug
    @Override
    public void debugDraw()
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
}