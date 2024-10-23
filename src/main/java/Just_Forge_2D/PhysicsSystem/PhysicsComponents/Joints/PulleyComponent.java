package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.GameWindow;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PulleyJointDef;
import org.joml.Vector2f;

public class PulleyComponent extends BaseJointComponent
{
    // - - - private variables
    private transient final PulleyJointDef jointDef = new PulleyJointDef();
    private transient PrismaticJoint joint;

    // - - - property variables
    private float ratio;
    protected Vector2f anchorA = new Vector2f();
    protected Vector2f anchorB = new Vector2f();
    protected Vector2f pulleyAnchorA = new Vector2f();
    protected Vector2f pulleyAnchorB = new Vector2f();


    // - - - | Functions | - - -


    // - - - Constructors - - -

    public PulleyComponent(GameObject OTHER) { super(OTHER);}
    public PulleyComponent(RigidBodyComponent OTHER_RB) {super(OTHER_RB);}
    public PulleyComponent(){}

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
            jointDef.ratio = this.ratio;
            if (anchorA.lengthSquared() == 0) anchorA = new Vector2f(this.gameObject.transform.position);
            if (anchorB.lengthSquared() == 0) anchorB = new Vector2f(other.transform.position);
            if (pulleyAnchorA.lengthSquared() == 0) pulleyAnchorA = new Vector2f(this.gameObject.transform.position);
            if (pulleyAnchorB.lengthSquared() == 0) pulleyAnchorB = new Vector2f(other.transform.position);
            jointDef.initialize(this.gameObject.getComponent(RigidBodyComponent.class).getRawBody(), otherRB.getRawBody(), new Vec2(pulleyAnchorA.x, pulleyAnchorA.y), new Vec2(pulleyAnchorB.x, pulleyAnchorB.y), new Vec2(anchorA.x, anchorA.y), new Vec2(anchorB.x, anchorB.y), this.ratio);
            joint = (PrismaticJoint)  GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().createJoint(jointDef);
        }
    }


    // - - - | Getters and Setters | - - -


    // - - - ratio
    public float getRatio()
    {
        return this.ratio;
    }

    public void setRatio(float RATIO)
    {
        this.ratio = RATIO;
        destroy();
        createJoint();
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

    public void setPulleyAnchorA(Vector2f ANCHOR)
    {
        this.pulleyAnchorA = ANCHOR;
        joint = null;
        createJoint();
    }

    public void setPulleyAnchorB(Vector2f ANCHOR)
    {
        this.pulleyAnchorB = ANCHOR;
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

    public Vector2f getPulleyAnchorA()
    {
        return this.pulleyAnchorA;
    }

    public Vector2f getPulleyAnchorB()
    {
        return this.pulleyAnchorB;
    }

    @Override
    public void destroy()
    {
        if (this.joint == null) return;
        GameWindow.getCurrentScene().getPhysics().rawWorld.getWorld().destroyJoint(joint);
    }
}