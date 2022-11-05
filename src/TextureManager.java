import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class TextureManager
{
    private String m_path;
    public List<Texture> TEXTURES = new ArrayList<Texture>();

    public TextureManager(String path)
    {
        m_path = path;
        this.LoadTextures();
        System.out.println(TEXTURES.size() + " textures found!");
    }

    private void LoadTextures()
    {
        File folder = new File(this.m_path);
        File[] listOfFiles = folder.listFiles();

        for(int i = 0; i < listOfFiles.length; i++)
        {
            try
            {
                Texture tex = new Texture(this.m_path + listOfFiles[i].getName(), i);
                TEXTURES.add(tex);
            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
