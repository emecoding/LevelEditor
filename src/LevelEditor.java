import java.io.IOException;

public class LevelEditor
{
    private Window m_window;
    private String textures_dir;
    private String final_dir;
    private Grid m_grid;
    public LevelEditor(String textures_dir, String final_dir) throws IOException
    {
        this.textures_dir = textures_dir;
        this.final_dir = final_dir;

        this.m_window = new Window(800, 600, "Level Editor 0.01(" + this.textures_dir + ")");
        this.m_window.init();

        this.m_grid = new Grid();
    }

    public void loop()
    {
        while(!this.m_window.should_close())
        {
            this.m_window.clear();
            this.m_grid.render();
            this.m_window.update();
        }

        this.destroy();
    }

    public void destroy()
    {
        this.m_window.destroy();
    }
}
