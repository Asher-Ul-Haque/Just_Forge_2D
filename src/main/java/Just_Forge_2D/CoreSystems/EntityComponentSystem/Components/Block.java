package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components;

import Just_Forge_2D.CoreSystems.EntityComponentSystem.GameObject;
import Just_Forge_2D.Utils.AssetPool;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public abstract class Block extends Component
{
    private transient boolean bobGoingUp = true;
    private transient boolean doBopAnimation = false;
    private transient Vector2f bobStart;
    private transient Vector2f topBobLocaton;
    private transient boolean active = true;
    public float bobSpeed = 0.1f;

    @Override
    public void start()
    {
        this.bobStart = new Vector2f(this.gameObject.transform.position);
        this.topBobLocaton = new Vector2f(bobStart).add(0.0f, 0.02f);
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (doBopAnimation)
        {
            if (bobGoingUp)
            {
                if (this.gameObject.transform.position.y < topBobLocaton.y)
                {
                    this.gameObject.transform.position.y += bobSpeed * DELTA_TIME;
                }
                else
                {
                    bobGoingUp = false;
                }
            }
            else
            {
                if (this.gameObject.transform.position.y > bobStart.y)
                {
                    this.gameObject.transform.position.y -= bobSpeed * DELTA_TIME;
                }
                else
                {
                    this.gameObject.transform.position.y = this.bobStart.y;
                    bobGoingUp = true;
                    doBopAnimation = false;
                }
            }
        }
    }

    @Override
    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        PlayerControllerComponent playerController = OBJ.getCompoent(PlayerControllerComponent.class);
        if (active && playerController != null && NORMAL.y < -0.8f)
        {
            doBopAnimation = true;
            AssetPool.getSound("Bump").play();
            playerHit(playerController);
        }
    }

    public void setInactive()
    {

    }

    abstract void playerHit(PlayerControllerComponent CONTROLLER);
}