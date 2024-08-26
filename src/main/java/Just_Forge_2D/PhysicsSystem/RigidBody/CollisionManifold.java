package Just_Forge_2D.PhysicsSystem.RigidBody;

import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class CollisionManifold
{
    private Vector2f normal;
    private List<Vector2f> contactPoints;
    private float depth;
    private boolean isColliding;

    public Vector2f getNormal() {
        return normal;
    }

    public List<Vector2f> getContactPoints() {
        return contactPoints;
    }

    public float getDepth() {
        return depth;
    }

    public CollisionManifold()
    {
        normal = new Vector2f();
        depth = 0.0f;
        isColliding = false;
    }
    public CollisionManifold(Vector2f NORMAL, float DEPTH)
    {
        this.normal = NORMAL;
        this.contactPoints = new ArrayList<>();
        this.depth = DEPTH;
    }

    public void addContactPoint(Vector2f CONTACT)
    {
        this.contactPoints.add(CONTACT);
    }
}
