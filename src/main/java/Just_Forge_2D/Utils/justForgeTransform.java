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
}
