package Just_Forge_2D.Core;
import org.joml.Vector2f;

public class Transform
{
    public Vector2f position;
    public Vector2f scale;

    public Transform()
    {
        init(new Vector2f(), new Vector2f());
    }

    public void init(Vector2f POSITION, Vector2f SCALE)
    {
        this.position = POSITION;
        this.scale = SCALE;
    }

    public Transform(Vector2f POSITION)
    {
        init(position, new Vector2f());
    }

    public Transform(Vector2f POSITION, Vector2f SCALE)
    {
        init(POSITION, SCALE);
    }
}
