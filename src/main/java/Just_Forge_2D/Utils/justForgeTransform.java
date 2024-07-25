package Just_Forge_2D.Utils;
import org.joml.Vector2f;

public class justForgeTransform
{
    public Vector2f position;
    public Vector2f scale;

    public justForgeTransform()
    {
        init(new Vector2f(), new Vector2f());
    }

    public void init(Vector2f POSITION, Vector2f SCALE)
    {
        this.position = POSITION;
        this.scale = SCALE;
    }

    public justForgeTransform(Vector2f POSITION)
    {
        init(position, new Vector2f());
    }

    public justForgeTransform(Vector2f POSITION, Vector2f SCALE)
    {
        init(POSITION, SCALE);
    }

    public justForgeTransform copy()
    {
        return new justForgeTransform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(justForgeTransform TO)
    {
        TO.position.set(this.position);
        TO.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null)
        {
            return false;
        }
        if (!(o instanceof justForgeTransform t))
        {
            return false;
        }

        return t.position.equals(this.position) && t.scale.equals(this.scale);
    }
}
