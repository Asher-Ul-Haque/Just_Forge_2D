package Just_Forge_2D.Core;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class justForgeCamera
{
    private Matrix4f projectionMatrix, viewMatrix;
    public Vector2f position;

    public justForgeCamera(Vector2f POSITION)
    {
        this.position = POSITION;
        this.projectionMatrix = new Matrix4f();
        this.viewMatrix = new Matrix4f();
        adjustProjection();
    }

    public void adjustProjection()
    {
        projectionMatrix.identity();
        projectionMatrix.ortho(0.0f, 32.0f * 32.0f, 0.0f, 32.0f * 32.0f, 0.0f, 64.0f); // the screen is 32 tiles of 32 pizels each in both directions

    }

    public Matrix4f getViewMatrix()
    {
        Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
        Vector3f cameraUp = new Vector3f(0.0f, 1.0f, 0.0f);

        this.viewMatrix.identity();
        viewMatrix.lookAt(new Vector3f(position.x, position.y, 16.0f), cameraFront.add(position.x, position.y, 0.0f), cameraUp);

        return viewMatrix;
    }

    public Matrix4f getProjectionMatrix()
    {
        return this.projectionMatrix;
    }
}
