package Just_Forge_2D.CoreSystems.EntityComponentSystem.Components;

import Just_Forge_2D.CoreSystems.AnimationSystem.StateMachine;
import Just_Forge_2D.CoreSystems.EntityComponentSystem.Components.PhysicsComponents.RigidBodyComponent;
import Just_Forge_2D.CoreSystems.ForgeDynamo;
import Just_Forge_2D.CoreSystems.InputSystem.Keyboard;
import org.joml.Vector2f;

import static org.lwjgl.glfw.GLFW.*;

public class PlayerControllerComponent extends Component
{
    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    public transient boolean onGround = false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient RigidBodyComponent rb;
    private transient StateMachine stateMachine;
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
        this.stateMachine = gameObject.getCompoent(StateMachine.class);
    }

    @Override
    public void update(float DELTA_TIME)
    {
        if (Keyboard.isKeyPressed(GLFW_KEY_RIGHT) || Keyboard.isKeyPressed(GLFW_KEY_D))
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
        if (Keyboard.isKeyPressed(GLFW_KEY_LEFT) || Keyboard.isKeyPressed(GLFW_KEY_A))
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

        this.velocity.x += this.acceleration.x * DELTA_TIME;
        this.velocity.y += this.acceleration.y * DELTA_TIME;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);

        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);
    }
}
