package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.WindowSystem.MainWindow;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.joml.Vector2f;

public class PistonComponent extends BaseJointComponent
{
    // - - - private variables
    private transient final PrismaticJointDef jointDef = new PrismaticJointDef();
    private transient PrismaticJoint joint;
    protected boolean enableLimit = false;
    protected boolean enableMotor = false;
    protected float motorSpeed = 10f;
    protected float maxMotorForce = 20f;
    protected Vector2f limits;
    protected float referenceAngle = 0f;
    protected Vector2f anchorA;
    protected Vector2f anchorB;
    protected Vector2f axis;
    protected Vector2f pistonAnchor;


    // - - - | Functions | - - -
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
            jointDef.collideConnected = this.collideConnected;
            jointDef.enableLimit = this.enableLimit;
            jointDef.enableMotor = this.enableMotor;
            jointDef.motorSpeed = this.motorSpeed;
            jointDef.maxMotorForce = this.maxMotorForce;
            jointDef.lowerTranslation = this.limits.x;
            jointDef.upperTranslation = this.limits.y;
            jointDef.referenceAngle = this.referenceAngle;
            if (anchorA == null) anchorA = new Vector2f(this.gameObject.transform.position);
            if (anchorB == null) anchorB = new Vector2f(other.transform.position);
            if (this.axis == null) axis = new Vector2f(1.0f, 0.0f);
            if (this.pistonAnchor == null) pistonAnchor = this.gameObject.transform.position;
            jointDef.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(pistonAnchor.x, pistonAnchor.y), new Vec2(this.axis.x, this.axis.y));
            joint = (PrismaticJoint)  MainWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(jointDef);
        }
    }

}
