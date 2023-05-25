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

    public static void gnuplotStandardDeviation(File targetFile) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream( targetFile + ".plt"))) {
            printStream.println("set xlabel \"X\"");
            printStream.println("set ylabel \"Y\"");
            printStream.println("set zlabel \"Standard Deviation\"");
            printStream.println("set dgrid3d");
            printStream.println("set view 45, 60, 1.0, 1.0");
            printStream.println("set terminal png");
            printStream.println("set output \"PNG" + targetFile.getName().substring(0, targetFile.getName().length()-4) + ".png\"");
            printStream.println("splot \"" + targetFile.getName() + "\" using 1:2:3 with pm3d");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
