package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.jbox2d.dynamics.joints.WheelJoint;
import org.jbox2d.dynamics.joints.WheelJointDef;
import org.joml.Vector2f;

public class SuspensionComponent extends BaseJointComponent
{
    private transient WheelJointDef jointDef = new WheelJointDef();
    private transient WheelJoint joint;
    private Vector2f anchor;
    private Vector2f axis;
    private float dampingRatio;
    private float frequencyHz;


    @Override
    protected void createJoint()
    {
        joint = null;
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
            jointDef.dampingRatio = this.dampingRatio;
            jointDef.frequencyHz = this.frequencyHz;
            if (anchor.lengthSquared() == 0) anchor = new Vector2f(this.gameObject.transform.position);
        }
    }
}
