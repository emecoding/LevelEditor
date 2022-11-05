public class LevelEditor
{
    private Window m_window;
    private String textures_dir;
    private String final_dir;
    public LevelEditor(String textures_dir, String final_dir)
    {
        this.textures_dir = textures_dir;
        this.final_dir = final_dir;

        this.m_window = new Window(800, 600, "Level Editor 0.01(" + this.textures_dir + ")");
        this.m_window.init();
    }

    public void loop()
    {
        while(!this.m_window.should_close())
        {
            this.m_window.clear();
            this.m_window.update();
        }

        this.destroy();
    }

    public void destroy()
    {
        this.m_window.destroy();
    }
}
