package Just_Forge_2D.Forge_Physics.Primitives;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Line
{
    private Vector2f from;
    private Vector2f to;
    private Vector3f color;

    public Line(Vector2f from, Vector2f to, Vector3f color, int lifetime)
    {
        this.from = from;
        this.to = to;
        this.color = color;
        this.lifetime = lifetime;
    }

    public Line(Vector2f from, Vector2f to, Vector3f color)
    {
        this.from = from;
        this.to = to;
        this.color = color;
    }

    public Line(Vector2f from, Vector2f to, int LIFETIME)
    {
        this.lifetime = LIFETIME;
        this.from = from;
        this.to = to;
    }

    public Line(Vector2f FROM, Vector2f TO)
    {
        this.lifetime = 60;
        this.from = FROM;
        this.to = TO;
    }

    public Vector2f getFrom() {
        return from;
    }

    public void setFrom(Vector2f from) {
        this.from = from;
    }

    public Vector2f getTo() {
        return to;
    }

    public void setTo(Vector2f to) {
        this.to = to;
    }

    public Vector3f getColor() {
        return color;
    }

    public void setColor(Vector3f color) {
        this.color = color;
    }

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    private int lifetime;

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
