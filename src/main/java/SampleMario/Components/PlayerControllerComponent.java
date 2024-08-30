package SampleMario.Components;

import Just_Forge_2D.AnimationSystem.AnimationComponent;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.PhysicsSystem.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.EntityComponentSystem.GameObject;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.EditorSystem.MainWindow;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.PhysicsSystem.RayCastInfo;
import Just_Forge_2D.Utils.AssetPool;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;

public class PlayerControllerComponent extends Component
{
    private enum PlayerState
    {
        Small,
        Big,
        Fire,
        Invincible
    }

    private PlayerState playerState = PlayerState.Small;
    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    public transient boolean onGround = false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient RigidBodyComponent rb;
    private transient AnimationComponent stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient int enemyBounce = 0;

    @Override
    public void start()
    {
        this.rb = gameObject.getCompoent(RigidBodyComponent.class);
        this.stateMachine = gameObject.getCompoent(AnimationComponent.class);
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (Keyboard.isKeyPressed(Keys.ARROW_RIGHT) || Keyboard.isKeyPressed(Keys.D))
        {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x < 0)
            {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            }
            else
            {
                this.stateMachine.trigger("startRunning");
            }
        }
        else if (Keyboard.isKeyPressed(Keys.ARROW_LEFT) || Keyboard.isKeyPressed(Keys.A))
        {
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x > 0)
            {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            }
            else
            {
                this.stateMachine.trigger("startRunning");
            }
        }
        else
        {
            this.acceleration.x = 0;
            if (this.velocity.x > 0)
            {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            }
            else if (this.velocity.x < 0)
            {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0)
            {
                this.stateMachine.trigger("stopRunning");
            }
        }

        checkOnGround();
        if ((Keyboard.isKeyPressed(Keys.SPACE)) && ((jumpTime > 0) || (groundDebounce > 0 ) || (onGround)))
        {
            if ((onGround || groundDebounce > 0) && jumpTime == 0)
            {
               // AssetPool.addSound("Jump Small", "Assets/Sounds/jump-small.ogg", false);
                AssetPool.getSound("Jump Small").play();
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
            }
            else if (jumpTime > 0)
            {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            }
            else
            {
                this.velocity.y = 0;
            }
            groundDebounce = 0;
        }
        else if (!onGround)
        {
            if (this.jumpTime > 0)
            {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            this.acceleration.y = -7f;
            groundDebounce -= DELTA_TIME;
        }
        else if (enemyBounce > 0)
        {
            // TODO: implement
        }
        else
        {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }

        this.velocity.y += this.acceleration.y * DELTA_TIME;
        this.velocity.x += this.acceleration.x * DELTA_TIME;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);

        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);

        if (!onGround)
        {
            stateMachine.trigger("jump");
        }
        else
        {
            stateMachine.trigger("stopJumping");
        }
    }

    public void checkOnGround()
    {
        Vector2f raycastBegin = new Vector2f(this.gameObject.transform.position);
        float innerPlayerWidth = this.playerWidth * 0.6f;
        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        float yVal = playerState == PlayerState.Small ? -0.14f : -0.24f;
        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, yVal);

        RayCastInfo info = MainWindow.getPhysicsSystem().rayCast(gameObject, raycastBegin, raycastEnd);

        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
        RayCastInfo info2 = MainWindow.getPhysicsSystem().rayCast(gameObject, raycast2Begin, raycast2End);

        onGround = ((info.hit || info2.hit) && info.hitObject != null);
    }

    @Override
    public void beginCollision(GameObject OBJ, Contact CONTACT, Vector2f NORMAL)
    {
        if (isDead) return;
        if (Math.abs(NORMAL.x) > 0.8f)
        {
            this.velocity.x = 0f;
        }
        else if (NORMAL.y > 0.8f)
        {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            this.jumpTime = 0;
        }
    }

    public boolean isSmol()
    {
        return this.playerState == PlayerState.Small;
    }
}