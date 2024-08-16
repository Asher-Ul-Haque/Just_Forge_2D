package Just_Forge_2D.EditorSystem;

import org.joml.Vector2f;
import org.joml.Vector3f;

//  - - - Line Class
public class Line
{
    // - - - Private Variables - - -
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;
    private int lifetime;


    // - - - | Functions | - - -


    // - - - Constructors - - -

    // - - - with everything
    public Line(Vector2f from, Vector2f to, Vector3f color, int lifetime)
    {
        this.from = from;
        this.to = to;
        this.color = color;
        this.lifetime = lifetime;
    }

    // - - - no lifetime
    public Line(Vector2f from, Vector2f to, Vector3f color)
    {
        this.from = from;
        this.to = to;
        this.color = color;
    }

    // - - - no color
    public Line(Vector2f from, Vector2f to, int LIFETIME)
    {
        this.lifetime = LIFETIME;
        this.from = from;
        this.to = to;
    }

    // - - - neither lifetime nor color
    public Line(Vector2f FROM, Vector2f TO)
    {
        this.lifetime = 60;
        this.from = FROM;
        this.to = TO;
    }


    // - - - Getters and Setters - - -

    // - - - From
    public Vector2f getFrom() {
        return from;
    }
    public void setFrom(Vector2f FROM) {
        this.from = FROM;
    }

    // - - - To
    public Vector2f getTo() {
        return to;
    }
    public void setTo(Vector2f TO) {
        this.to = TO;
    }

    // - - - Color
    public Vector3f getColor() {
        return color;
    }
    public void setColor(Vector3f COLOR) {
        this.color = COLOR;
    }

    // - - - Lifetime
    public int getLifetime() {
        return lifetime;
    }
    public void setLifetime(int LIFETIME) {
        this.lifetime = LIFETIME;
    }


    // - - - Utility - - -

    public int beginFrame()
    {
        lifetime--;
        return this.lifetime;
    }

    public float lengthSquared()
    {
        return new Vector2f(this.to).sub(this.from).lengthSquared();
    }
}
