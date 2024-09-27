package PhysicsSystem.PhysicsManagers;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.WorldManifold;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class CollisionManager implements ContactListener
{
    @Override
    public void beginContact(Contact CONTACT)
    {
        GameObject objA = (GameObject) CONTACT.getFixtureA().getUserData();
        GameObject objB = (GameObject) CONTACT.getFixtureB().getUserData();

        WorldManifold worldManifold = new WorldManifold();
        CONTACT.getWorldManifold(worldManifold);

        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : objA.getComponents())
        {
            c.beginCollision(objB, CONTACT, aNormal);
        }

        for (Component c : objB.getComponents())
        {
            c.beginCollision(objA, CONTACT, bNormal);
        }
    }

    @Override
    public void endContact(Contact CONTACT)
    {
        GameObject objA = (GameObject) CONTACT.getFixtureA().getUserData();
        GameObject objB = (GameObject) CONTACT.getFixtureB().getUserData();

        WorldManifold worldManifold = new WorldManifold();
        CONTACT.getWorldManifold(worldManifold);

        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : objA.getComponents())
        {
            c.endCollision(objB, CONTACT, aNormal);
        }

        for (Component c : objB.getComponents())
        {
            c.endCollision(objA, CONTACT, bNormal);
        }
    }

    @Override
    public void preSolve(Contact CONTACT, Manifold MANIFOLD)
    {
        GameObject objA = (GameObject) CONTACT.getFixtureA().getUserData();
        GameObject objB = (GameObject) CONTACT.getFixtureB().getUserData();

        WorldManifold worldManifold = new WorldManifold();
        CONTACT.getWorldManifold(worldManifold);

        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : objA.getComponents())
        {
            c.beforeCollision(objB, CONTACT, aNormal);
        }

        for (Component c : objB.getComponents())
        {
            c.beforeCollision(objA, CONTACT, bNormal);
        }
    }

    @Override
    public void postSolve(Contact CONTACT, ContactImpulse CONTACT_IMPULSE)
    {
        GameObject objA = (GameObject) CONTACT.getFixtureA().getUserData();
        GameObject objB = (GameObject) CONTACT.getFixtureB().getUserData();

        WorldManifold worldManifold = new WorldManifold();
        CONTACT.getWorldManifold(worldManifold);

        Vector2f aNormal = new Vector2f(worldManifold.normal.x, worldManifold.normal.y);
        Vector2f bNormal = new Vector2f(aNormal).negate();

        for (Component c : objA.getComponents())
        {
            c.afterCollision(objB, CONTACT, aNormal);
        }

        for (Component c : objB.getComponents())
        {
            c.afterCollision(objA, CONTACT, bNormal);
        }
    }
}
