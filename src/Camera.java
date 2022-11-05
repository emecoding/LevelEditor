import org.joml.Matrix4f;
import org.joml.Vector2f;

public class Camera
{
    private Vector2f position;
    public static Matrix4f ViewMatrix;
    public static int CAMERA_OFF_SET_X = 0;
    public static int CAMERA_OFF_SET_Y = 0;

    public Camera(Vector2f pos)
    {
        this.position = pos;
        this.ViewMatrix = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f ,1.0f, 0.1f, 100.0f);
        this.ViewMatrix.translate(this.position.x, this.position.y, 1.0f);
        this.ViewMatrix = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f ,1.0f, 0.1f, 100.0f).mul(this.ViewMatrix);
    }

    public void Move(Vector2f dpos)
    {
        this.ViewMatrix.translate(dpos.x, dpos.y, 1.0f);
        this.ViewMatrix = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f ,1.0f, 0.1f, 100.0f).mul(this.ViewMatrix);
    }

}
