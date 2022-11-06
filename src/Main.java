import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {
        LevelEditor editor = null;
        try
        {
            editor = new LevelEditor("../../TestImagesForLevelEditor/", "../../TestSavesForLevelEditor/", "test01");
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }


        editor.PROGRAM_WINDOW_WIDTH = 800;
        editor.PROGRAM_WINDOW_HEIGHT = 600;
        editor.loop();
    }
}