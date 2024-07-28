package Just_Forge_2D.Core.ECS.Components.Unattachable;
import Just_Forge_2D.Core.ECS.Components.Component;
import org.joml.Vector2f;

// - - - class to store data regarding position and scale
public class TransformComponent extends Component
{
    // - - - private variables
    public Vector2f position;
    public Vector2f scale;


    // - - - | Functions | - - -


    // - - - Constructors and initialization - - -

    public void init(Vector2f POSITION, Vector2f SCALE)
    {
        this.position = POSITION;
        this.scale = SCALE;
    }

    public TransformComponent()
    {
        init(new Vector2f(), new Vector2f());
    }

    public TransformComponent(Vector2f POSITION)
    {
        init(POSITION, new Vector2f());
    }

    public TransformComponent(Vector2f POSITION, Vector2f SCALE)
    {
        init(POSITION, SCALE);
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

        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
