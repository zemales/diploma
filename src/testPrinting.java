import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class testPrinting {

    public static void printAverage(int iterator) {
        try {
            File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
            File targetFile = new File(targetDirectory, iterator + "StatisticsAverageCoefficients.txt");
            PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
