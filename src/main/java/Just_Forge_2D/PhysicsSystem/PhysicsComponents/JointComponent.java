package Just_Forge_2D.PhysicsSystem.PhysicsComponents;

import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.WindowSystem.MainWindow;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JointComponent extends Component
{
    private List<Vector2f> points = new ArrayList<>();
    private Set<RigidBodyComponent> bodies = new HashSet<>();
    private String name;


    @Override
    public void editorGUI()
    {
        super.editorGUI();
        name = Widgets.inputText("Other Object: " , name);
        GameObject other = MainWindow.getCurrentScene().getGameObject(name);
        if (other != null)
        {
            points.add(other.transform.position);
            RigidBodyComponent rb = other.getComponent(RigidBodyComponent.class);
            if ( rb != null) {
                bodies.add(rb);
            }
        }
        Widgets.drawIntControl("Body Count", bodies.size());
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        for (Vector2f p : points)
        {
            DebugPencil.addLine(p, this.gameObject.transform.position);
        }
    }

    @Override
    public void update(float DELTA_TIME)
    {
        editorUpdate(DELTA_TIME);
    }
}
