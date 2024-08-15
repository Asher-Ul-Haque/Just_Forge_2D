package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components;
import Just_Forge_2D.EditorSystem.ForgeIsGUI;
import Just_Forge_2D.Utils.Configurations;
import org.joml.Vector2f;

// - - - class to store data regarding position and scale
public class TransformComponent extends Component
{
    // - - - private variables
    public Vector2f position;
    public int layer;
    public Vector2f scale;
    public float rotation = Configurations.DEFAULT_ROTATION;


    // - - - | Functions | - - -


    // - - - Constructors and initialization - - -

    public void init(Vector2f POSITION, Vector2f SCALE, int LAYER)
    {
        this.position = POSITION;
        this.scale = SCALE;
        this.layer = LAYER;
    }

    public TransformComponent()
    {
        init(new Vector2f(), new Vector2f(), Configurations.DEFAULT_LAYER);
    }

    public TransformComponent(Vector2f POSITION)
    {
        init(POSITION, new Vector2f(), Configurations.DEFAULT_LAYER);
    }

    public TransformComponent(Vector2f POSITION, Vector2f SCALE)
    {
        init(POSITION, SCALE, Configurations.DEFAULT_LAYER);
    }

    public TransformComponent(Vector2f POSITION, Vector2f SCALE, int LAYER)
    {
        init(POSITION, SCALE, LAYER);
    }

    public TransformComponent(Vector2f POSITION, int LAYER)
    {
        init(POSITION, new Vector2f(), Configurations.DEFAULT_LAYER);
    }


    // - - - Basic Utility functions - - -

    // - - - Copy
    public TransformComponent copy()
    {
        return new TransformComponent(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(TransformComponent TO)
    {
        TO.position.set(this.position);
        TO.scale.set(this.scale);
    }

    // - - - Compare
    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (!(o instanceof TransformComponent t))
        {
            return false;
        }

        return (t.position.equals(this.position)) && (t.scale.equals(this.scale)) && (t.rotation == this.rotation) && (t.layer == this.layer);
    }


    // - - - Editor stuff
    @Override
    public void editorGUI()
    {
        this.gameObject.name = ForgeIsGUI.inputText("Name", this.gameObject.name);
        ForgeIsGUI.drawVec2Control("Position", this.position);
        ForgeIsGUI.drawVec2Control("Scale", this.scale, Math.min(Configurations.GRID_HEIGHT, Configurations.GRID_WIDTH));
        this.rotation = ForgeIsGUI.drawFloatControl("Rotation", this.rotation);
        this.layer = ForgeIsGUI.drawIntControl("Layer", this.layer);
    }
}
