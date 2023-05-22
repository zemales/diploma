import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Gnuplot {
    public static void gnuplotCode(File targetFile) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream( targetFile + ".plt"))) {
            printStream.println("set terminal png");
            printStream.println("set output \"PNG" + targetFile.getName().substring(0, targetFile.getName().length()-4) + ".png\"");
            printStream.println("plot \"" + targetFile.getName() + "\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
