package Just_Forge_2D.PhysicsSystem.PhysicsComponents;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.MainWindow;
import imgui.ImGui;


public class JointComponent extends Component
{
    private String otherName;
    private transient GameObject other;
    private transient RigidBodyComponent otherRB;

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

    @Override
    public void start()
    {
        if (other == null)
        {
            other = MainWindow.getCurrentScene().getGameObject(otherName);
        }
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        if (other != null) DebugPencil.addLine(other.transform.position, this.gameObject.transform.position);
    }

    @Override
    public void update(float DELTA_TIME)
    {
        editorUpdate(DELTA_TIME);
    }

    @Override
    public void editorGUI()
    {
        super.editorGUI();
        if (ImGui.button("Set other"))
        {
            other = MainWindow.getCurrentScene().getGameObject(otherName);
        }
    }
}
