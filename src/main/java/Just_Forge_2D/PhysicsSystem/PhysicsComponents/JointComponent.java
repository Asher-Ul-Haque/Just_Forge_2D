package Just_Forge_2D.PhysicsSystem.PhysicsComponents;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.Logger;
import Just_Forge_2D.WindowSystem.MainWindow;
import imgui.ImGui;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.DistanceJoint;
import org.jbox2d.dynamics.joints.DistanceJointDef;
import org.joml.Vector2f;


public class JointComponent extends Component
{
    private String otherName;
    private transient GameObject other;
    private transient RigidBodyComponent otherRB;
    private transient DistanceJointDef defJoint = new DistanceJointDef ();
    private transient DistanceJoint joint;
    private transient boolean firstTime = false;


    public JointComponent(GameObject OTHER)
    {
        other = OTHER;
        otherRB = other.getComponent(RigidBodyComponent.class);
        otherName = OTHER.name;
    }

    public JointComponent(RigidBodyComponent OTHER_RB)
    {
        otherRB = OTHER_RB;
        other = OTHER_RB.gameObject;
        if (other != null)
        {
            otherName = other.name;
        }
    }

    public JointComponent() {}

    private void createJoint()
    {
        if (other == null)
        {
            other = MainWindow.getCurrentScene().getGameObject(otherName);
            otherRB = other.getComponent(RigidBodyComponent.class);
        }
        if (other != null && otherRB != null)
        {
            defJoint.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(this.gameObject.transform.position.x, this.gameObject.transform.position.y), new Vec2(other.transform.position.x, other.transform.position.y));
            joint = (DistanceJoint) MainWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(defJoint);
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
    public void update(float DELTA_TIME)
    {
        if (!firstTime)
        {
            try
            {
                createJoint();
            }
            catch (Exception e)
            {
                Logger.FORGE_LOG_ERROR("sry");
            }
            firstTime = true;
        }
        editorUpdate(DELTA_TIME);
    }


    @Override
    public void editorGUI()
    {
        super.editorGUI();
        if (ImGui.button("Set other | Joint : " + (joint == null)))
        {
            createJoint();
        }
    }
}
