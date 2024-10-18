package Just_Forge_2D.PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.PhysicsSystem.PhysicsManagers.CollisionLayer;
import imgui.ImGui;
import imgui.type.ImBoolean;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector4f;

public abstract class ColliderComponent extends Component
{
    private final transient ImBoolean[] collisionMasks = new ImBoolean[16];
    protected transient boolean useCollisionColor = false;
    protected boolean debugDrawAtRuntime = false;
    protected Vector4f hitboxColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);
    protected Vector4f collisionColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
    public CollisionLayer collisionLayer = new CollisionLayer();

    public ColliderComponent()
    {
        for (int i = 0; i < 16; ++i)
        {
            collisionMasks[i] = new ImBoolean(collisionLayer.canCollideWith(i + 1));
        }
    }

    @Override
    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        useCollisionColor = true;
    }

    @Override
    public void endCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        useCollisionColor = false;
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (debugDrawAtRuntime)
        {
            debugDraw();
        }
    }

    @Override
    public void editorGUI()
    {
        if (Widgets.button(Icons.Trash + " Destroy##" + this.getClass().hashCode()))
        {
            this.gameObject.removeComponent(this.getClass());
            return;
        }
        debugDrawAtRuntime = Widgets.drawBoolControl(Icons.Pen + "  Runtime Debug Draw", debugDrawAtRuntime);
        if (debugDrawAtRuntime)
        {
            Widgets.colorPicker4(Icons.EyeDropper + "  HitBox Color", this.hitboxColor);
            Widgets.colorPicker4(Icons.EyeDropper + "  Collision Color", this.collisionColor);
        }
        ImGui.indent(16f);
        if (ImGui.collapsingHeader(Icons.Bomb + "  Collision Editor"))
        {
            collisionEditor();
        }
        ImGui.unindent();
        ImGui.separator();
    }

    protected void collisionEditor()
    {
        collisionLayer.setLayer(Widgets.drawIntControl("Layer", Math.max(Math.min(16, collisionLayer.getLayer()), 1)));
        ImGui.columns(2);
        if (Widgets.button(Icons.PlusSquare + "  Select All")) for (int i = 0; i < 16; ++i) collisionLayer.setCollideWithLayer(i + 1, true);
        ImGui.nextColumn();
        if (Widgets.button(Icons.MinusSquare + "  Unselect All")) for (int i = 0; i < 16; ++i) collisionLayer.setCollideWithLayer(i + 1, false);
        ImGui.columns(1);
        String[] layers = new String[16];
        for (int i = 0; i < layers.length; ++i)
        {
            layers[i] = "Layer " + (i + 1);
        }
        Widgets.text("Collides with: ");
        ImGui.columns(2);
        for (int i = 0; i < 16; ++i)
        {
            collisionLayer.setCollideWithLayer(i + 1, Widgets.drawBoolControl(layers[i], collisionLayer.canCollideWith(i + 1)));
            ImGui.nextColumn();
        }
        ImGui.columns(1);
        Widgets.text(collisionLayer.toString());
    }
}
