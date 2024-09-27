package PhysicsSystem.PhysicsComponents;

import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import PhysicsSystem.Raycasts.RayCastInfo;
import PhysicsSystem.Raycasts.RaycastGun;
import RenderingSystem.DebugPencil;
import Utils.DefaultValues;
import Utils.Logger;
import org.jbox2d.common.Vec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class KeyboardControllerComponent extends Component
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
    private float jumpImpulse = DefaultValues.DEFAULT_JUMP_IMPULSE;
    private float maxRunSpeed = DefaultValues.DEFAULT_MAX_RUN_SPEED;
    private float groundDetectRayLength = DefaultValues.DEFAULT_GROUND_DETECT_RAY_LENGTH;

    private float coyoteTime = DefaultValues.DEFAULT_COYOTE_TIME;
    private transient float coyoteTimer = 0f;
    private int maxJumps = DefaultValues.DEFAULT_MAX_JUMPS;
    private transient int jumpsUsed = 0;

    private RigidBodyComponent rb;
    private transient Vector2f moveVelocity = new Vector2f();
    private transient boolean isRight;
    private transient boolean isEditorUpdate = true;
    private transient boolean isGrounded;
    private boolean birdMode = false;
    private float maxJumpTime = 1f;
    private float jumpTimer = maxJumpTime;
    private boolean debugDrawAtRuntime = false;
    private Vector4f hitColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
    private Vector4f noHitColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

    @Override
    public void start()
    {
        this.rb = this.gameObject.getComponent(RigidBodyComponent.class);
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
        isEditorUpdate = false;
        checkGrounded(DELTA_TIME);

        Vector2f moveInput = new Vector2f();
        if (Keyboard.isKeyPressed(rightKey))
        {
            moveInput.x = 1.0f;
        }
        if (Keyboard.isKeyPressed(leftKey))
        {
            moveInput.x = -1.0f;
        }

        if (isGrounded)
        {
            jumpsUsed = 0;
            move(this.groundAcceleration, this.groundDeceleration, moveInput, DELTA_TIME);
        }
        else
        {
            move(this.airAcceleration, this.airDeceleration, moveInput, DELTA_TIME);
        }

        handleJump(DELTA_TIME);
    }

    @Override
    public void editorUpdate(float DELTA_TIME)
    {
        isEditorUpdate = true;
        checkGrounded(DELTA_TIME);
    }


    private void move(float ACCELERATION, float DECELERATION, Vector2f MOVE_INPUT, float DELTA_TIME)
    {
        Keyboard.reset();
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
        boolean wasGrounded = this.isGrounded;
        if (birdMode)
        {
            this.isGrounded = true;
            return;
        }
        RaycastGun gun = new RaycastGun();

        Vector2f rayCastBegin = new Vector2f(this.gameObject.transform.position);
        rayCastBegin.sub(this.gameObject.transform.scale.x / 2.0f,  (this.gameObject.transform.scale.y / 2 + groundDetectRayLength));

        Vector2f rayCastEnd = new Vector2f(rayCastBegin);
        rayCastEnd.add(this.gameObject.transform.scale.x, 0.0f);

        Vector2f rayCast2Begin = new Vector2f(this.gameObject.transform.position);
        rayCast2Begin.sub(0f, this.gameObject.transform.scale.y /2.0f);
        Vector2f rayCast2End = new Vector2f(rayCast2Begin).sub(0.0f, groundDetectRayLength);
        RayCastInfo rayCast2 = gun.rayCast(this.gameObject, rayCast2Begin, rayCast2End);

        RayCastInfo rayCastInfo = gun.rayCast(this.gameObject, rayCastBegin, rayCastEnd);

        this.isGrounded = (rayCastInfo.hit && rayCastInfo.hitObject != null) || (rayCast2.hit && rayCast2.hitObject != null);
        if (debugDrawAtRuntime || isEditorUpdate)
        {
            Vector3f color;
            if (this.isGrounded || rayCastInfo.hit || coyoteTimer > 0f) color = new Vector3f(hitColor.x, hitColor.y, hitColor.z);
            else color = new Vector3f(noHitColor.x, noHitColor.y, noHitColor.z);
            DebugPencil.addLine(rayCastBegin, rayCastEnd, color);
            DebugPencil.addLine(rayCast2Begin, rayCast2End, color);
        }

        if (!isGrounded && wasGrounded)
        {
            coyoteTimer = coyoteTime;
        }

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
            else if (jumpsUsed + 1 < maxJumps)
            {
                jumpTimer = maxJumpTime;
                rb.setVelocity(new Vector2f(rb.getLinearVelocity().x, 0f));
                jump();
            }
        }
    }

    private void jump()
    {
        Vec2 jumpImpulse = new Vec2(0, this.jumpImpulse);
        rb.addImpulse(new Vector2f(jumpImpulse.x, jumpImpulse.y).mul(jumpTimer / maxJumpTime));

        jumpsUsed++;

        coyoteTimer = 0f;
    }
}