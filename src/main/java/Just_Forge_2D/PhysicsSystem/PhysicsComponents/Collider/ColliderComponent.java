package PhysicsSystem.PhysicsComponents.Collider;

import Just_Forge_2D.EditorSystem.EditorSystemManager;
import Just_Forge_2D.EditorSystem.Themes.Theme;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import PhysicsSystem.PhysicsManagers.CollisionLayer;
import imgui.ImGui;
import imgui.type.ImBoolean;
import imgui.type.ImInt;
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
        if (ImGui.button("Destroy"))
        {
            this.gameObject.removeComponent(this.getClass());
            return;
        }
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        if (ImGui.checkbox("Debug Draw at Runtime", this.debugDrawAtRuntime))
        {
            this.debugDrawAtRuntime = !this.debugDrawAtRuntime;
        }
        Theme.resetDefaultTextColor();
        Widgets.colorPicker4("HitBox Color", this.hitboxColor);
        Widgets.colorPicker4("Collision Color", this.collisionColor);
        ImGui.indent(16f);
        if (ImGui.collapsingHeader("Collision Editor"))
        {
            collisionEditor();
        }
        ImGui.unindent();
        ImGui.separator();
    }

    protected void collisionEditor()
    {
        if (ImGui.button("Select All"))
        {
            for (int i = 0; i < 16; ++i)
            {
                collisionLayer.setCollideWithLayer(i + 1, true);
            }
        }
        ImGui.sameLine();
        if (ImGui.button("Unselect All"))
        {
            for (int i = 0; i < 16; ++i)
            {
                collisionLayer.setCollideWithLayer(i + 1, false);
            }
        }
        String[] layers = new String[16];
        for (int i = 0; i < layers.length; ++i)
        {
            layers[i] = "Layer " + (i + 1);
        }

        collisionLayer.setLayer(Widgets.drawIntControl("Layer", Math.max(Math.min(16, collisionLayer.getLayer()), 1)));
        Theme.setDefaultTextColor(EditorSystemManager.getCurrentTheme().secondaryColor);
        ImGui.text("Collides with: ");
        ImGui.columns(2);
        for (int i = 0; i < 16; ++i)
        {
            if (ImGui.checkbox(layers[i], collisionLayer.canCollideWith(i + 1)))
            {
                collisionLayer.setCollideWithLayer(i + 1, !collisionLayer.canCollideWith(i + 1));
            }
            ImGui.nextColumn();
        }
        ImGui.columns(1);
        ImGui.text(collisionLayer.toString());
        Theme.resetDefaultTextColor();
        ImGui.separator();
    }
}
