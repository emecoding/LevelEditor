import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditor
{
    private Window m_window;
    private String textures_dir;
    private String final_dir;
    private Grid m_grid;
    private TextureManager m_textureManager;
    private Renderer m_renderer;
    private Camera m_camera;
    private float[] m_current_item = null;
    private List<float[]> MAP = new ArrayList<float[]>();
    private boolean just_pressed_right_mouse_button = false;
    private boolean just_rotated = false;

    public int PROGRAM_WINDOW_WIDTH = 0;
    public int PROGRAM_WINDOW_HEIGHT = 0;
    public int DEFAULT_CELL_WIDTH = 32;
    public int DEFAULT_CELL_HEIGHT = 32;
    public int SCALE_POWER = 5;
    public float ROTATION_POWER = 0.1f;
    public int CAMERA_SPEED = 5;
    public int CAMERA_DIR = 0;//0=LEFT, RIGHT, 1=UP, DOWN
    public LevelEditor(String textures_dir, String final_dir) throws IOException
    {
        this.textures_dir = textures_dir;
        this.final_dir = final_dir;

        this.m_window = new Window(1280, 720, "Level Editor 0.01(" + this.textures_dir + ")");
        this.m_window.init();

        this.m_textureManager = new TextureManager(textures_dir);
        this.m_renderer = new Renderer();
        this.m_grid = new Grid();
        this.m_camera = new Camera(new Vector2f(0.0f, 0.0f));
    }
    public void loop()
    {
        while(!this.m_window.should_close())
        {
            this.m_window.clear();
            this.move_camera();
            this.render_map();
            this.use_current_item();
            this.use_sprite_buttons();
            this.render_program_window();
            this.m_window.update();
        }

        this.destroy();
    }
    public void destroy()
    {
        this.m_window.destroy();
    }
    private void use_sprite_buttons()
    {
        float start_x = 10.0f;
        float x = start_x;
        float y = 10.0f;
        float offset = 5.0f;
        float button_width = 32.0f;
        float button_height = 32.0f;

        for(int i = 0; i < this.m_textureManager.TEXTURES.size(); i++)
        {
            Vector2f mouse_pos = Window.get_mouse_position();
            Texture tex = this.m_textureManager.TEXTURES.get(i);
            tex.use();
            this.m_renderer.render_sprite(new Vector2f(x, y), new Vector2f(button_width, button_height), false);
            Texture.unbind_every_texture();

            float[] rect1 = new float[]
                    {
                            x, y, button_width, button_height
                    };

            float[] rect2 = new float[]
                    {
                            mouse_pos.x, mouse_pos.y, 10.0f, 10.0f
                    };

            if(Window.collides(rect1, rect2) && Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_LEFT))
            {
                this.m_current_item = new float[]{(float)i, (float)tex.width, (float)tex.height, 0.0f};
            }

            x += button_width + offset;
            if(x >= Window.get_width() - button_width)
            {
                x = start_x;
                y += button_height + offset;
            }

        }
    }
    private void use_current_item()
    {
        if(this.m_current_item == null)
            return;

        if(Window.key_is_pressed(GLFW_KEY_LEFT))
            this.m_current_item[1] -= SCALE_POWER;
        if(Window.key_is_pressed(GLFW_KEY_RIGHT))
            this.m_current_item[1] += SCALE_POWER;
        if(Window.key_is_pressed(GLFW_KEY_UP))
            this.m_current_item[2] += SCALE_POWER;
        if(Window.key_is_pressed(GLFW_KEY_DOWN))
            this.m_current_item[2] -= SCALE_POWER;

        if(Window.key_is_pressed(GLFW_KEY_R) && !just_rotated)
        {
            this.m_current_item[3] += ROTATION_POWER;
            just_rotated = true;
        }
        if(!Window.key_is_pressed(GLFW_KEY_R))
            just_rotated = false;

        if(Window.key_is_pressed(GLFW_KEY_SPACE))
            ROTATION_POWER += 0.1f;

        Vector2f mouse_pos = Window.get_mouse_position();

        float x = mouse_pos.x - this.m_current_item[1]/2;
        float y = mouse_pos.y - this.m_current_item[2]/2;

        x = Math.round(x/DEFAULT_CELL_WIDTH)*DEFAULT_CELL_WIDTH;
        y = Math.round(y/DEFAULT_CELL_HEIGHT)*DEFAULT_CELL_HEIGHT;

        Texture tex = this.m_textureManager.TEXTURES.get((int)this.m_current_item[0]);

        tex.use();
        this.m_renderer.render_sprite(new Vector2f(x - Camera.CAMERA_OFF_SET_X, y - Camera.CAMERA_OFF_SET_Y), new Vector2f(this.m_current_item[1], this.m_current_item[2]), false, (float)this.m_current_item[3]);
        Texture.unbind_every_texture();

        if(Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_RIGHT) && !just_pressed_right_mouse_button)
        {
            just_pressed_right_mouse_button = true;
            float[] i = new float[]{tex.INDEX, (int)x, (int)y, this.m_current_item[1], this.m_current_item[2], this.m_current_item[3]};
            this.MAP.add(i);
        }

        if(!Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_RIGHT))
            just_pressed_right_mouse_button = false;

    }
    private void render_map()
    {
        for(int i = 0; i < this.MAP.size(); i++)
        {
            float[] item = this.MAP.get(i);
            Texture tex = this.m_textureManager.TEXTURES.get((int)item[0]);
            tex.use();
            this.m_renderer.render_sprite(new Vector2f(item[1], item[2]), new Vector2f(item[3], item[4]), true, item[3]);
            Texture.unbind_every_texture();
        }
    }
    private void render_program_window()
    {
        if(this.PROGRAM_WINDOW_WIDTH != 0 && this.PROGRAM_WINDOW_HEIGHT != 0)
        {
            float x = Window.get_width()/2-this.PROGRAM_WINDOW_WIDTH/2;
            float y = Window.get_height()/2-this.PROGRAM_WINDOW_HEIGHT/2;
            this.m_grid.render_single_rect(new Vector2f(x, y), new Vector2f(this.PROGRAM_WINDOW_WIDTH, this.PROGRAM_WINDOW_HEIGHT));
        }
    }
    private void move_camera()
    {
        if(Window.key_is_pressed(GLFW_KEY_D))
        {
            if(CAMERA_DIR == 0)
                CAMERA_DIR = 1;
            else if(CAMERA_DIR == 1)
                CAMERA_DIR = 0;
        }


        if(Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_MIDDLE))
        {
            int speed = -CAMERA_SPEED;
            if(this.CAMERA_DIR == 0)
            {
                if(Window.get_mouse_position().x < Window.get_width()/2)
                    speed *= -1;

                this.m_camera.Move(new Vector2f(speed, 0.0f));
                Camera.CAMERA_OFF_SET_X += speed;
            }
            else if(this.CAMERA_DIR == 1)
            {
                if(Window.get_mouse_position().y < Window.get_height()/2)
                    speed *= -1;

                Camera.CAMERA_OFF_SET_Y += speed;
                this.m_camera.Move(new Vector2f(0.0f, speed));
            }

        }
    }
}
