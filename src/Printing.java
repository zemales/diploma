import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Printing {
    File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
    PrintStream out = null;

    Printing(String name) {
        try {
            File targetFile = new File(targetDirectory, name);
            out = new PrintStream(new FileOutputStream(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
