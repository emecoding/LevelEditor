import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {
        LevelEditor editor = null;
        try
        {
            editor = new LevelEditor("../../TestImagesForLevelEditor/", "");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        editor.loop();
    }
}