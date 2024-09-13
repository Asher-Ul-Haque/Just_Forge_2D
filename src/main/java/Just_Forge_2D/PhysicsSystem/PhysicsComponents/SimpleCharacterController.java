package Just_Forge_2D.PhysicsSystem.PhysicsComponents;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.PhysicsSystem.Raycasts.RayCastInfo;
import Just_Forge_2D.PhysicsSystem.Raycasts.Raycast;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;

public class SimpleCharacterController extends Component
{
    private Keys rightKey = DefaultValues.DEFAULT_MOVE_RIGHT_KEY;
    private Keys leftKey = DefaultValues.DEFAULT_MOVE_LEFT_KEY;
    private Keys jumpKey = DefaultValues.DEFAULT_JUMP_KEY;
    private Keys runKey = DefaultValues.DEFAULT_RUN_KEY;

    private float maxWalkSpeed = DefaultValues.DEFAULT_MAX_WALK_SPEED;
    private float groundAcceleration = DefaultValues.DEFAULT_GROUND_ACCELERATION;
    private float groundDeceleration = DefaultValues.DEFAULT_GROUND_DECELERATION;
    private float airAcceleration = DefaultValues.DEFAULT_AIR_ACCELERATION;
    private float airDeceleration = DefaultValues.DEFAULT_AIR_DECELERATION;
    private float jumpImpulse = DefaultValues.DEFAULT_JUMP_IMPULSE;  // Jump force to be applied
    private float maxRunSpeed = DefaultValues.DEFAULT_MAX_RUN_SPEED;
    private float groundDetectRayLength = DefaultValues.DEFAULT_GROUND_DETECT_RAY_LENGTH;

    private float coyoteTime = DefaultValues.DEFAULT_COYOTE_TIME;  // Grace period for jumping after leaving ground
    private transient float coyoteTimer = 0f;
    private int maxJumps = DefaultValues.DEFAULT_MAX_JUMPS;  // Number of allowed jumps (1 jump on ground, 1 mid-air)
    private transient int jumpsUsed = 0;  // Track the number of jumps used

    private RigidBodyComponent rb;
    private transient Vector2f moveVelocity = new Vector2f();
    private transient boolean isRight;
    private transient boolean isGrounded;
    private float maxJumpTime = 1f;
    private float jumpTimer = maxJumpTime;

    @Override
    public void start()
    {
        this.rb = this.gameObject.getCompoent(RigidBodyComponent.class);
        if (rb == null)
        {
            Logger.FORGE_LOG_ERROR("Cannot use a controller without a rigid body");
            return;
        }
        this.isRight = this.gameObject.transform.scale.x >= 0f;
    }

    @Override
    public void update(float DELTA_TIME)
    {
        // Check if the player is grounded and handle coyote time
        checkGrounded(DELTA_TIME);

        Vector2f moveInput = new Vector2f();
        if (Keyboard.isKeyPressed(rightKey)) moveInput.x = 1.0f;
        if (Keyboard.isKeyPressed(leftKey)) moveInput.x = -1.0f;

        if (isGrounded)
        {
            // Reset jump when grounded
            jumpsUsed = 0;
            move(this.groundAcceleration, this.groundDeceleration, moveInput, DELTA_TIME);
        }
        else
        {
            move(this.airAcceleration, this.airDeceleration, moveInput, DELTA_TIME);
        }

        // Handle jumping logic
        handleJump(DELTA_TIME);
    }

    private void move(float ACCELERATION, float DECELERATION, Vector2f MOVE_INPUT, float DELTA_TIME)
    {
        if (!MOVE_INPUT.equals(0, 0))
        {
            turnCheck(MOVE_INPUT);
            Vector2f targetVelocity = new Vector2f(MOVE_INPUT.x, 0.0f).mul(Keyboard.isKeyPressed(runKey) ? maxRunSpeed : maxWalkSpeed);
            this.moveVelocity.lerp(targetVelocity, ACCELERATION * DELTA_TIME);
        }
        else
        {
            this.moveVelocity.lerp(new Vector2f(0.0f), DECELERATION * DELTA_TIME);
        }
//        this.rb.addImpulse(new Vector2f(moveVelocity.x / this.rb.getMass(), 0));
        this.rb.setVelocity(new Vector2f(moveVelocity.x, rb.getVelocity().y));
    }

    private void turnCheck(Vector2f MOVE_INPUT)
    {
        if (this.isRight && MOVE_INPUT.x < 0)
        {
            turn(false);
        }
        else if (!this.isRight && MOVE_INPUT.x > 0)
        {
            turn(true);
        }
    }

    private void turn(boolean RIGHT)
    {
        isRight = RIGHT;
        this.gameObject.transform.scale.x = Math.abs(this.gameObject.transform.scale.x) * (RIGHT ? 1 : -1);
    }

    private void checkGrounded(float DELTA_TIME)
    {
        Raycast gun = new Raycast();
        Vector2f rayCast1Begin = new Vector2f(this.gameObject.transform.position);
        rayCast1Begin.sub(this.gameObject.transform.scale.x / 2.0f, this.gameObject.transform.scale.y / 2.0f);
        Vector2f rayCast1End = new Vector2f(rayCast1Begin).sub(0.0f, groundDetectRayLength);
        DebugPencil.addLine(rayCast1Begin, rayCast1End);
        RayCastInfo rayCast1 = gun.rayCast(this.gameObject, rayCast1Begin, rayCast1End);

        Vector2f rayCast2Begin = new Vector2f(this.gameObject.transform.position);
        rayCast2Begin.sub(- this.gameObject.transform.scale.x / 2.0f, this.gameObject.transform.scale.y /2.0f);
        Vector2f rayCast2End = new Vector2f(rayCast2Begin).sub(0.0f, groundDetectRayLength);
        DebugPencil.addLine(rayCast2Begin, rayCast2End);
        RayCastInfo rayCast2 = gun.rayCast(this.gameObject, rayCast2Begin, rayCast2End);

        boolean wasGrounded = this.isGrounded;
        this.isGrounded = rayCast1.hit && rayCast1.hitObject != null || rayCast2.hit && rayCast2.hitObject != null;

        // Start or reduce coyote time when the player leaves the ground
        if (!isGrounded && wasGrounded)
        {
            coyoteTimer = coyoteTime;
        }

        // Reduce coyote timer
        if (coyoteTimer > 0)
        {
            coyoteTimer -= DELTA_TIME;
        }
    }

    private void handleJump(float DELTA_TIME)
    {
        jumpTimer -= DELTA_TIME;
        if (Keyboard.isKeyBeginPress(jumpKey))
        {
            if ((isGrounded || coyoteTimer > 0f) && maxJumps > 0)
            {
                jumpTimer = maxJumpTime;
                jump();
            }
            else if (jumpsUsed < maxJumps)
            {
                jumpTimer = maxJumpTime;
                rb.setVelocity(new Vector2f(rb.getLinearVelocity().x, 0f));
                jump();
            }
        }
    }

    private void jump()
    {
        // Apply upward impulse for jumping
        Vec2 jumpImpulse = new Vec2(0, this.jumpImpulse);
        rb.addImpulse(new Vector2f(jumpImpulse.x, jumpImpulse.y).mul(jumpTimer / maxJumpTime));

        // Track jumps
        jumpsUsed++;

        // Reset coyote timer to prevent jumping after coyote time expires
        coyoteTimer = 0f;
    }
}