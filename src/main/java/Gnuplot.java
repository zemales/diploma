import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class Gnuplot {
    public static void gnuplotCode(File targetFile) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream( targetFile + ".plt"))) {
            printStream.println("set terminal png");
            printStream.println("set output \"PNG" + targetFile.getName().substring(0, targetFile.getName().length()-4) + ".png\"");
            printStream.println("plot \"" + targetFile.getName() + "\" using 1:2");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void gnuplotStandardDeviation(File targetFile) {
        try (PrintStream printStream = new PrintStream(new FileOutputStream( targetFile + ".plt"))) {
            printStream.println("set arrow from 80,100,0 to 116,100,0 nohead lc rgb \"black\" lw 2 front");
            printStream.println("set arrow from 100,84,0 to 100,112,0 nohead lc rgb \"black\" lw 2 front\n");

            printStream.println("set xrange [80:116]");
            printStream.println("set yrange [84:112]\n");

            printStream.println("set xlabel \"X\" offset 0, -52 font \",25\"");
            printStream.println("set ylabel \"Y\" offset -133,0 rotate by 90 font \",25\"\n");

            printStream.println("set xtics offset 0,-44 font \",16\"");
            printStream.println("set ytics offset -117,0 rotate by 90 font \",16\"");
            printStream.println("set ztics format \"\"\n");

            printStream.println("set dgrid3d 70, 60");
            printStream.println("set view 180, 0, 1.5, 1.3");
            printStream.println("set terminal png size 1200, 800");
            printStream.println("set output \"PNG_" + targetFile.getName().substring(0, targetFile.getName().length()-4) + ".png\"");
            printStream.println("splot \"" + targetFile.getName() + "\" using 1:2:3 with pm3d,\\");
            printStream.println("\"\" using 1:2:3:(sprintf(\"(%g)\", $4)) with labels offset 1,1 notitle");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
