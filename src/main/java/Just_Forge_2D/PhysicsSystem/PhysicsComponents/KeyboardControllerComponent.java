package Just_Forge_2D.PhysicsSystem.PhysicsComponents;

import Just_Forge_2D.EditorSystem.Icons;
import Just_Forge_2D.EditorSystem.Widgets;
import Just_Forge_2D.EntityComponentSystem.Components.Component;
import Just_Forge_2D.InputSystem.Keyboard;
import Just_Forge_2D.InputSystem.Keys;
import Just_Forge_2D.PhysicsSystem.Raycasts.RayCastInfo;
import Just_Forge_2D.PhysicsSystem.Raycasts.RaycastGun;
import Just_Forge_2D.RenderingSystem.DebugPencil;
import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class KeyboardControllerComponent extends Component
{
    protected Keys rightKey = DefaultValues.DEFAULT_MOVE_RIGHT_KEY;
    protected Keys leftKey = DefaultValues.DEFAULT_MOVE_LEFT_KEY;
    protected Keys jumpKey = DefaultValues.DEFAULT_JUMP_KEY;
    protected Keys runKey = DefaultValues.DEFAULT_RUN_KEY;

    protected float maxWalkSpeed = DefaultValues.DEFAULT_MAX_WALK_SPEED;
    protected float groundAcceleration = DefaultValues.DEFAULT_GROUND_ACCELERATION;
    protected float groundDeceleration = DefaultValues.DEFAULT_GROUND_DECELERATION;
    protected float airAcceleration = DefaultValues.DEFAULT_AIR_ACCELERATION;
    protected float airDeceleration = DefaultValues.DEFAULT_AIR_DECELERATION;
    protected float jumpImpulse = DefaultValues.DEFAULT_JUMP_IMPULSE;
    protected float maxRunSpeed = DefaultValues.DEFAULT_MAX_RUN_SPEED;
    protected float groundDetectRayLength = DefaultValues.DEFAULT_GROUND_DETECT_RAY_LENGTH;

    protected float coyoteTime = DefaultValues.DEFAULT_COYOTE_TIME;
    protected transient float coyoteTimer = 0f;
    protected int maxJumps = DefaultValues.DEFAULT_MAX_JUMPS;
    protected transient int jumpsUsed = 0;

    protected RigidBodyComponent rb;
    protected transient Vector2f moveVelocity = new Vector2f();
    protected transient boolean isRight;
    protected transient boolean isEditorUpdate = true;
    protected transient boolean isGrounded;
    protected boolean birdMode = false;
    protected float maxJumpTime = 1f;
    protected float jumpTimer = maxJumpTime;
    protected boolean debugDrawAtRuntime = false;
    protected Vector4f hitColor = new Vector4f(0.0f, 1.0f, 0.0f, 1.0f);
    protected Vector4f noHitColor = new Vector4f(1.0f, 0.0f, 0.0f, 1.0f);

    @Override
    public void editorGUI()
    {
        if (Widgets.button(Icons.Trash + " Destroy##" + this.getClass().hashCode())) this.gameObject.removeComponent(this.getClass());

        Enum t = Widgets.drawEnumControls(Keys.class, Icons.LongArrowAltLeft + "  Move Left Key", leftKey);
        if (t != null) leftKey = (Keys) t;

        t = Widgets.drawEnumControls(Keys.class, Icons.LongArrowAltRight + "  Move Right Key", rightKey);
        if (t != null) rightKey = (Keys) t;

        t = Widgets.drawEnumControls(Keys.class, Icons.LongArrowAltUp + "  Jump Key", jumpKey);
        if (t != null) jumpKey = (Keys) t;

        t = Widgets.drawEnumControls(Keys.class, Icons.Running + "  Run Key", rightKey);
        if (t != null) runKey = (Keys) t;

        ImGui.separator();

        maxWalkSpeed = Widgets.drawFloatControl(Icons.Walking + "  Walk Speed", maxWalkSpeed);
        maxRunSpeed = Widgets.drawFloatControl(Icons.Running + "  Run Speed", maxRunSpeed);

        ImGui.separator();

        airAcceleration = Widgets.drawFloatControl(Icons.Wind + "  Air Acceleration", airAcceleration);
        airDeceleration = Widgets.drawFloatControl(Icons.Wind + "  Air Deceleration", airDeceleration);
        groundAcceleration = Widgets.drawFloatControl(Icons.GlobeAsia + "  Ground Acceleration", groundAcceleration);
        groundDeceleration = Widgets.drawFloatControl(Icons.GlobeAsia + "  Ground Deceleration", groundDeceleration);

        ImGui.separator();

        jumpImpulse = Widgets.drawFloatControl(Icons.CaretUp + "  Jump Impulse", jumpImpulse);
        coyoteTime = Widgets.drawFloatControl(Icons.CaretUp + "  Coyote Time", coyoteTime);
        maxJumpTime = Widgets.drawFloatControl(Icons.CaretUp + "  Max Jump Time", maxJumpTime);
        if (!birdMode) maxJumps = Widgets.drawIntControl(Icons.CaretUp + "  Max Jumps", maxJumps);
        birdMode = Widgets.drawBoolControl(Icons.Crow + "  Bird Mode", birdMode);

        ImGui.separator();

        groundDetectRayLength = Widgets.drawFloatControl(Icons.XRay + "  Ground Detect Ray Length", groundDetectRayLength);
        debugDrawAtRuntime = Widgets.drawBoolControl(Icons.Pen + "  Runtime Debug Draw", debugDrawAtRuntime);
        if (!debugDrawAtRuntime) return;

        Widgets.colorPicker4(Icons.EyeDropper + "  Hit Color ", hitColor);
        Widgets.colorPicker4(Icons.EyeDropper + "  No Hit Color", noHitColor);
    }

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


    protected void move(float ACCELERATION, float DECELERATION, Vector2f MOVE_INPUT, float DELTA_TIME)
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

    protected void turnCheck(Vector2f MOVE_INPUT)
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

    protected void turn(boolean RIGHT)
    {
        isRight = RIGHT;
        this.gameObject.transform.scale.x = Math.abs(this.gameObject.transform.scale.x) * (RIGHT ? 1 : -1);
    }

    protected void checkGrounded(float DELTA_TIME)
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
        RayCastInfo rayCastInfo2 = gun.rayCast(this.gameObject, rayCast2Begin, rayCast2End);

        this.isGrounded = (rayCastInfo.hit || rayCastInfo.hitObject != null) || (rayCastInfo2.hit || rayCastInfo2.hitObject != null);
        if (debugDrawAtRuntime || isEditorUpdate)
        {
            Vector3f color;
            if (this.isGrounded || coyoteTimer > 0f || maxJumps - jumpsUsed > 0 || birdMode) color = new Vector3f(hitColor.x, hitColor.y, hitColor.z);
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

        if (isGrounded) jumpTimer = maxJumpTime;
    }


    protected void handleJump(float DELTA_TIME)
    {
        jumpTimer -= DELTA_TIME;
        if (Keyboard.isKeyBeginPress(jumpKey))
        {
            if ((isGrounded || coyoteTimer > 0f) && maxJumps > 0)
            {
                jump(1);
            }
            else if (jumpsUsed < maxJumps)
            {
                jumpTimer = maxJumpTime;
                rb.setVelocity(new Vector2f(rb.getLinearVelocity().x, 0f));
                jump(1);
            }
        }
    }

    protected void jump(float MULTIPLIER)
    {
        rb.addImpulse(new Vector2f(0, this.jumpImpulse).mul(jumpTimer / maxJumpTime).mul(MULTIPLIER));
        jumpsUsed++;
        coyoteTimer = 0f;
    }
}