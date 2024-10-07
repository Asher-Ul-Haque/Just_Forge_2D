package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.MainWindow;
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
    protected boolean collideConntected = false;
    protected float referenceAngle = 0f;
    protected boolean enableLimit = false;
    protected float upperAngle = (float) (Math.PI / 2f);
    protected float lowerAngle = -(float) (Math.PI / 2f);;
    protected boolean enableMotor = true;
    protected float motorSpeed = 5f;
    protected float maxMotorTorque = 20f;


    // - - - | Functions | - - -


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
            other = MainWindow.getCurrentScene().getGameObject(otherName);
            otherRB = other.getComponent(RigidBodyComponent.class);
        }
        if (other != null && otherRB != null)
        {
            defJoint.referenceAngle = this.referenceAngle;
            defJoint.enableLimit = this.enableLimit;
            defJoint.enableMotor = this.enableMotor;
            defJoint.collideConnected = this.collideConntected;
            defJoint.lowerAngle = this.lowerAngle;
            defJoint.upperAngle = this.upperAngle;
            defJoint.maxMotorTorque = this.maxMotorTorque;
            defJoint.motorSpeed = this.motorSpeed;
            // - - - TODO: abstract stuff out check for null instead of 0.
            if (anchorA.lengthSquared() == 0) anchorA = new Vector2f(this.gameObject.transform.position);
            if (anchorB.lengthSquared() == 0) anchorB = new Vector2f(other.transform.position);
            if (hingeAnchor.lengthSquared() == 0) hingeAnchor = new Vector2f(anchorA);
            defJoint.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(hingeAnchor.x, hingeAnchor.y));
            joint = (RevoluteJoint) MainWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(defJoint);
        }
    }

    @Override
    public void debugDraw()
    {
        DebugPencil.addCircle(hingeAnchor, 0.5f);
        DebugPencil.addLine(anchorA, anchorB);
    }
}
