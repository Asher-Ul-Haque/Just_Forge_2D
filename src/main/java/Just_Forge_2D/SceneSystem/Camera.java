package Just_Forge_2D.SceneSystem;

import Just_Forge_2D.Utils.DefaultValues;
import Just_Forge_2D.Utils.Logger;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

// - - - 2D Camera
public class Camera
{
    // - - - Private Variables - - -

    // - - - projection
    private final Matrix4f projectionMatrix;
    private final Matrix4f inverseProjectionMatrix;
    private final Vector2f projectionSize = DefaultValues.DEFAULT_CAMERA_PROJECTION_SIZE;

    // - - - view
    private final Matrix4f viewMatrix;
    private final Matrix4f inverseViewMatrix;

    // - - - position and zoom
    private Vector2f position;
    private float zoom = DefaultValues.DEFAULT_CAMERA_ZOOM;
    private Vector3f rotation = new Vector3f();


    // - - - Functions - - -

    // - - - Camera constructor to use camera immediately
    public Camera(Vector2f POSITION)
    {
        this.position = POSITION;
        this.projectionMatrix = new Matrix4f();
        this.inverseProjectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        this.inverseViewMatrix = new Matrix4f();
        adjustProjection();
    }


    // - - - Getters and Setters - - -

    // - - - get the view matrix
    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.viewMatrix.identity();
        this.viewMatrix.lookAt(new Vector3f(position.x, position.y, 20.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);
        this.viewMatrix.rotateXYZ(rotation);
        this.viewMatrix.invert(this.inverseViewMatrix);

        return this.viewMatrix;
    }

    // - - - get the projection matrix
    public Matrix4f getProjectionMatrix()
    {
        return this.projectionMatrix;
    }

    public void adjustProjection()
    {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, projectionSize.x * this.zoom, 0.0f, projectionSize.y * this.zoom, 0.0f, 100.0f);
        projectionMatrix.invert(inverseProjectionMatrix);
    }


    // - - - Get the inverse matrices
    public Matrix4f getInverseProjectionMatrix()
    {
        return this.inverseProjectionMatrix;
    }

    public Matrix4f getInverseViewMatrix()
    {
        return this.inverseViewMatrix;
    }

    // - - - for projectionSize
    public Vector2f getProjectionSize()
    {
        return this.projectionSize;
    }


    // - - - Zoom - - -

    public float getZoom()
    {
        return this.zoom;
    }

    public void setZoom(float ZOOM)
    {
        this.zoom = ZOOM;
    }

    public void addZoom(float OFFSET)
    {
        this.zoom += OFFSET;
    }


    // - - - Position - - -

    public Vector2f getPosition()
    {
        return position;
    }

    public void setPositionAbsolute(Vector2f POSITION)
    {
        this.position = POSITION;
    }

    public void setPosition(Vector2f POSITION)
    {
        float cosZ = (float) Math.cos(this.rotation.z);
        float sinZ = (float) Math.sin(this.rotation.z);

        float rotatedX = POSITION.x * cosZ - POSITION.y * sinZ;
        float rotatedY = POSITION.x * sinZ + POSITION.y * cosZ;

        this.position.set(rotatedX, rotatedY);
    }


    // - - - Rotation - - -
    public void setRotation(Vector3f ROTATION)
    {
        if (ROTATION == null)
        {
            Logger.FORGE_LOG_ERROR("Rotation cannot be null");
            return;
        }
        this.rotation = ROTATION;
    }

    public void setRotation(float ROTATION)
    {
        this.rotation.z = (float) (ROTATION % 2 * Math.PI);
    }

    public Vector3f getRotationVector()
    {
        return this.rotation;
    }

    public float getRotation() {return this.rotation.z; }
}
