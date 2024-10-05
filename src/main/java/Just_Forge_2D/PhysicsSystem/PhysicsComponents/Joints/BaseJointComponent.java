package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import imgui.ImGui;

public abstract class BaseJointComponent extends Component
{
    protected String otherName;
    protected boolean debugDrawAtRuntime = false;
    protected transient GameObject other;
    protected transient RigidBodyComponent otherRB;
    protected transient boolean firstTime = true;

    public BaseJointComponent(GameObject OTHER)
    {
        other = OTHER;
        otherRB = other.getComponent(RigidBodyComponent.class);
        otherName = OTHER.name;
    }

    public BaseJointComponent(RigidBodyComponent OTHER_RB)
    {
        otherRB = OTHER_RB;
        other = OTHER_RB.gameObject;
        if (other != null)
        {
            otherName = other.name;
        }
    }

    public BaseJointComponent() {}

    protected abstract void createJoint();

    @Override
    public void update(float DELTA_TIME)
    {
        if (firstTime)
        {
            createJoint();
            firstTime = false;
        }
        if (debugDrawAtRuntime) debugDraw();
    }

    @Override
    public void editorGUI()
    {
        otherName = Widgets.inputText("Other name: ", otherName);
        debugDrawAtRuntime = Widgets.drawBoolControl("Debug Draw At Runtime", debugDrawAtRuntime);
        if (ImGui.button("Set other"))
        {
            createJoint();
        }
    }
}
