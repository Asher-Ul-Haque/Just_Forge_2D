package Just_Forge_2D.EntityComponentSystem.Components;

import Just_Forge_2D.EditorSystem.EditorComponents.GridlinesComponent;
import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.Utils.DefaultValues;
import org.joml.Vector2f;

// - - - class to store data regarding position and scale
public class TransformComponent extends Component
{
    // - - - private variables
    public Vector2f position;
    public int layer;
    public Vector2f scale;
    public float rotation = DefaultValues.DEFAULT_ROTATION;


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
        init(new Vector2f(), new Vector2f(), DefaultValues.DEFAULT_LAYER);
    }

    public TransformComponent(Vector2f POSITION)
    {
        init(POSITION, new Vector2f(), DefaultValues.DEFAULT_LAYER);
    }

    public TransformComponent(Vector2f POSITION, Vector2f SCALE)
    {
        init(POSITION, SCALE, DefaultValues.DEFAULT_LAYER);
    }

    public TransformComponent(Vector2f POSITION, Vector2f SCALE, int LAYER)
    {
        init(POSITION, SCALE, LAYER);
    }

    public TransformComponent(Vector2f POSITION, int LAYER)
    {
        init(POSITION, new Vector2f(), DefaultValues.DEFAULT_LAYER);
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
        this.gameObject.name = Widgets.inputText(Icons.User + "  Name", this.gameObject.name);
        Widgets.drawVec2Control(Icons.Crosshairs + "  Position", this.position);
        Widgets.drawVec2Control(Icons.ExpandArrowsAlt + "  Scale", this.scale, Math.min(GridlinesComponent.gridSize.x, GridlinesComponent.gridSize.y), 0.1f);
        this.rotation = Widgets.drawFloatControl(Icons.Sync + "  Rotation", this.rotation);
        this.layer = Widgets.drawIntControl(Icons.LayerGroup + "  Layer", this.layer);
    }
}
