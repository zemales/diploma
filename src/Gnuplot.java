import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Gnuplot {
    public static void gnuplotCode(File targetFile) throws IOException {
        PrintStream printStream = new PrintStream(new FileOutputStream(targetFile + ".plt"));
        printStream.println("set terminal png");
        printStream.println("set output \"" + targetFile.getName() + ".png\"");
        printStream.println("plot \"" + targetFile.getName() + "\"");
        printStream.close();
    }
}
