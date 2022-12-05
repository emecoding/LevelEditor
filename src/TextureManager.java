import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class TextureManager
{
    private String m_path;
    public List<Integer[]> TEXTURES = new ArrayList<Integer[]>();//id, width, height
    public static Texture ArrowTexture = null;

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

        try
        {
            this.ArrowTexture = new Texture("res/arrow.png", listOfFiles.length+100);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for(int i = 0; i < listOfFiles.length; i++)
        {
            try
            {
                String p = this.m_path + listOfFiles[i].getName();
                String[] splittedP = p.split("\\.", 10);
                String type = splittedP[splittedP.length-1];
                if(!type.equals("txt"))
                {
                    Texture tex = new Texture(p, i);
                    TEXTURES.add(new Integer[]{tex.ID, tex.width, tex.height});
                    continue;
                }

                if(listOfFiles[i].getName().equals("spritesheets.txt"))
                {
                    File file = new File(p);
                    Scanner reader = new Scanner(file);
                    List<String> lines = new ArrayList<String>();
                    while(reader.hasNextLine())
                        lines.add(reader.nextLine());

                    reader.close();

                    for(int line = 0; line < lines.size(); line++)
                    {
                        String LINE = lines.get(line).replace("[", "");
                        LINE = LINE.replace("]", "");
                        String[] SPLITTED_LINE = LINE.split(",", 10);
                        String fileName = SPLITTED_LINE[0];
                        int width = Integer.parseInt(SPLITTED_LINE[1]);
                        int height = Integer.parseInt(SPLITTED_LINE[2]);
                        SpriteSheet sheet = new SpriteSheet(this.m_path + fileName, width, height);
                        for(int j = 0; j < sheet.AmountOfSprites(); j++)
                        {
                            TEXTURES.add(new Integer[]{sheet.GetTextureID(sheet.SPRITES.get(j)[0], sheet.SPRITES.get(j)[1]), width, height});
                        }
                    }
                }

            }
            catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

}
