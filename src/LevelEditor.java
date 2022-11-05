import org.joml.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;

public class LevelEditor
{
    private Window m_window;
    private String textures_dir;
    private String final_dir;
    //private Grid m_grid;
    private TextureManager m_textureManager;
    private Renderer m_renderer;
    private Texture m_current_item = null;
    private List<Integer[]> MAP = new ArrayList<Integer[]>();
    private boolean just_pressed_right_mouse_button = false;
    public LevelEditor(String textures_dir, String final_dir) throws IOException
    {
        this.textures_dir = textures_dir;
        this.final_dir = final_dir;

        this.m_window = new Window(1280, 720, "Level Editor 0.01(" + this.textures_dir + ")");
        this.m_window.init();

        this.m_textureManager = new TextureManager(textures_dir);
        this.m_renderer = new Renderer();
        //this.m_grid = new Grid();
    }

    public void loop()
    {
        while(!this.m_window.should_close())
        {
            this.m_window.clear();
            //this.m_grid.render();
            this.render_map();
            this.use_current_item();
            this.use_sprite_buttons();

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
            this.m_renderer.render_sprite(new Vector2f(x, y), new Vector2f(button_width, button_height));
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
                this.m_current_item = tex;
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

        Vector2f mouse_pos = Window.get_mouse_position();

        float x = mouse_pos.x - this.m_current_item.width/2;
        float y = mouse_pos.y - this.m_current_item.height/2;

        x = Math.round(x/this.m_current_item.width)*this.m_current_item.width;
        y = Math.round(y/this.m_current_item.height)*this.m_current_item.height;

        this.m_current_item.use();
        this.m_renderer.render_sprite(new Vector2f(x, y), new Vector2f(this.m_current_item.width, this.m_current_item.height));
        Texture.unbind_every_texture();

        if(Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_RIGHT) && !just_pressed_right_mouse_button)
        {
            just_pressed_right_mouse_button = true;
            Integer[] i = new Integer[]{this.m_current_item.INDEX, (int)x, (int)y};
            this.MAP.add(i);
        }

        if(!Window.mouse_button_is_pressed(GLFW_MOUSE_BUTTON_RIGHT))
            just_pressed_right_mouse_button = false;

    }
    private void render_map()
    {
        for(int i = 0; i < this.MAP.size(); i++)
        {
            Integer[] item = this.MAP.get(i);
            Texture tex = this.m_textureManager.TEXTURES.get(item[0]);
            tex.use();
            this.m_renderer.render_sprite(new Vector2f(item[1], item[2]), new Vector2f(tex.width, tex.height));
            Texture.unbind_every_texture();
        }
    }
}
