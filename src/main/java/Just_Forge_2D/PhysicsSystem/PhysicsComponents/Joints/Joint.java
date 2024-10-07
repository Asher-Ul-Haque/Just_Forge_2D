package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Joints;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.WindowSystem.MainWindow;
import imgui.ImGui;

public abstract class Joint extends Component 
{
    protected String otherName;
    protected transient GameObject other;
    protected transient RigidBodyComponent otherRB;
    protected transient boolean firstTime = false;

    public Joint(GameObject OTHER)
    {
        other = OTHER;
        otherRB = other.getComponent(RigidBodyComponent.class);
        otherName = OTHER.name;
    }

    public Joint(){}

    public Joint(RigidBodyComponent OTHER_RB)
    {
        otherRB = OTHER_RB;
        other = OTHER_RB.gameObject;
        if (other != null)
        {
            otherName = other.name;
        }
    }

    @Override
    public void editorGUI()
    {
        if (ImGui.button("Destroy"))
        {
            this.destroy();
            this.gameObject.removeComponent(this.getClass());
        }
        otherName = Widgets.inputText("Other Game Object", otherName);
        if (ImGui.button("Create Joint"))
        {
            other = MainWindow.getCurrentScene().getGameObject(otherName);
            if (other != null)
            {
                otherRB = other.getComponent(RigidBodyComponent.class);
            }
            createJoint();
        }
    }

    protected abstract void createJoint();

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        debugDraw();
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (!firstTime)
        {
            createJoint();
            firstTime = true;
        }
        debugDraw();
    }
}
