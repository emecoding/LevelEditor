import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.nio.FloatBuffer;

import static org.lwjgl.glfw.GLFW.*;

public class Camera
{
    private Vector2f position;
    public static Matrix4f ViewMatrix;
    public static int CAMERA_OFF_SET_X = 0;
    public static int CAMERA_OFF_SET_Y = 0;
    public static int DIR = 0;
    public static int SPEED = 5;
    private Renderer RENDERER;

    public Camera(Vector2f pos, Renderer renderer)
    {
        this.position = pos;
        this.RENDERER = renderer;
        this.ViewMatrix = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f ,1.0f, 0.1f, 100.0f);
        this.ViewMatrix.translate(this.position.x, this.position.y, 1.0f);
        this.ViewMatrix = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f ,1.0f, 0.1f, 100.0f).mul(this.ViewMatrix);
    }
    private void keyboard_movement()
    {
        if(Window.key_is_pressed(GLFW_KEY_D))
        {
            if(DIR == 0)
                DIR = 1;
            else if(DIR == 1)
                DIR = 0;
        }


        if(Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_MIDDLE))
        {
            int speed = -SPEED;
            if(this.DIR == 0)
            {
                float rot = 0.0f;
                if(Window.get_mouse_position().x < Window.get_width()/2)
                {
                    speed *= -1;
                    rot = (float)Math.toRadians(180);
                }


                this.Move_Dx(new Vector2f(speed, 0.0f));
                Texture.unbind_every_texture();
                TextureManager.ArrowTexture.use();
                this.RENDERER.render_sprite(new Vector2f(Window.get_mouse_position().x - CAMERA_OFF_SET_X - TextureManager.ArrowTexture.width/2, Window.get_mouse_position().y - CAMERA_OFF_SET_Y), new Vector2f(TextureManager.ArrowTexture.width, TextureManager.ArrowTexture.height), true, rot);
                Texture.unbind_every_texture();
                Camera.CAMERA_OFF_SET_X += speed;
            }
            else if(this.DIR == 1)
            {
                float rot = (float)Math.toRadians(90);
                if(Window.get_mouse_position().y < Window.get_height()/2)
                {
                    speed *= -1;
                    rot = (float)Math.toRadians(-90);
                }


                Camera.CAMERA_OFF_SET_Y += speed;
                this.Move_Dx(new Vector2f(0.0f, speed));

                Texture.unbind_every_texture();
                TextureManager.ArrowTexture.use();
                this.RENDERER.render_sprite(new Vector2f(Window.get_mouse_position().x - CAMERA_OFF_SET_X - TextureManager.ArrowTexture.width/2, Window.get_mouse_position().y - CAMERA_OFF_SET_Y), new Vector2f(TextureManager.ArrowTexture.width, TextureManager.ArrowTexture.height), true, rot);
                Texture.unbind_every_texture();
            }

        }
    }
    private void joystick_movement()
    {
        FloatBuffer axes = glfwGetJoystickAxes(GLFW_JOYSTICK_1);
        float x = axes.get(0);
        float y = axes.get(1);

        x = Math.round(x);
        y = Math.round(y);

        this.Move_Dx(new Vector2f(-x*SPEED, 0.0f));
        Camera.CAMERA_OFF_SET_X += x*SPEED;

        this.Move_Dx(new Vector2f(0.0f, -y*SPEED));
        Camera.CAMERA_OFF_SET_Y += y*SPEED;
    }
    public void move()
    {
        keyboard_movement();
        //joystick_movement();
    }

    public void Move_Dx(Vector2f dpos)
    {
        this.ViewMatrix.translate(dpos.x, dpos.y, 1.0f);
        this.ViewMatrix = new Matrix4f().ortho(-1.0f, 1.0f, -1.0f ,1.0f, 0.1f, 100.0f).mul(this.ViewMatrix);
    }

}
