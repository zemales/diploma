import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

public class DataBase {
    static Printing printingObject = new Printing("000AAA.txt");

    public static void generalSelection(Connection connection) throws Exception {
        String selectQuery = "select amount, intensity " +
                "from locatorData " +
                "where intensity is not null";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        File targetDir = new File(System.getProperty("user.dir") + "\\DataFiles");
        File targetFile = new File(targetDir, "dispersionFile.txt");
        PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

        while (resultSet.next()) {
            printStream.printf(Locale.US, "%.2f\t%.2f\r\n", resultSet.getDouble("amount"), (resultSet.getDouble("intensity")/6));
        }
        printStream.close();
    }

    public static void perGauge(Connection connection) throws Exception{
        for (int i = 1001; i < 1036; i++ ) {
            String selectQuery = "select amount, intensity " +
                    "from locatorData " +
                    "where intensity is not null " +
                    "and rainGauge = " + i + ";";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (!resultSet.next()) {continue;}

            File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
            File targetFile = new File(targetDirectory, i + ".txt");
            PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

            while (resultSet.next()) {
                printStream.printf(Locale.US, "%.2f\t%.2f\r\n", resultSet.getDouble(1), (resultSet.getDouble(2)/6));
            }

            printStream.close();
            Gnuplot.gnuplotCode(targetFile);
        }
    }

    public static void coefficientsSelect(Connection connection) throws Exception {
        for (int i = 1001; i < 1036; i++) {
            String selectQuery = "select amount, z1 from locatorData " +
                    "where rainGauge = " + i +
                    " and amount != 0 " +
                    " and (z1 is not null and z1 != 0) " +
                    " and intensity is not null;";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            if (!resultSet.next()) {continue;}

            ArrayList<Double[]> arrayList = new ArrayList<>();

            while (resultSet.next()) {
                Double[] doubleArray = new Double[2];
                doubleArray[0] = resultSet.getDouble("amount");
                doubleArray[1] = resultSet.getDouble("z1");
                arrayList.add(doubleArray);
            }

            aCoeffCalculation(arrayList, i);
        }
    }

    public static void aCoeffCalculation(ArrayList<Double[]> arrayList, int iterator) throws IOException{
        double b = Math.round(bCoeffCalculation(arrayList)*100.0)/100.0;
        ArrayList<Double> arrayA = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i+1; j < arrayList.size(); j++) {
                double I2 = arrayList.get(j)[0];
                double I1 = arrayList.get(i)[0];
                double Z2 = arrayList.get(j)[1];
                double Z1 = arrayList.get(i)[1];
                double A = I1 / Math.pow(Math.pow(10, Z1/10), b);

                if ((I1 != I2) && (Z1 != Z2)) {
                    arrayA.add(A);
                }
                if ((i+2 == arrayList.size()) && (j+1 == arrayList.size())) {
                    Statistics varA = new Statistics(arrayA);

                    double coefA = Math.round(varA.sort().confidenceLevel()
                            .detectAndDelete().average()*1000000.0)/1000000.0;

                    new Coefficients(coefA, b, iterator);
                    printAverageCoeffs(coefA, b, iterator);
                }
            }
        }
    }

    public static double bCoeffCalculation(ArrayList<Double[]> arrayList) {
        ArrayList<Double> arrayB = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i+1; j < arrayList.size(); j++) {
                double I2 = arrayList.get(j)[0];
                double I1 = arrayList.get(i)[0];
                double Z2 = arrayList.get(j)[1];
                double Z1 = arrayList.get(i)[1];

                double b = Math.log(Math.pow(10, Z2/10) / Math.pow(10, Z1/10)) /
                        Math.log(I2 / I1);

                if ((I1 != I2) && (Z1 != Z2)) {
                    arrayB.add(b);
                }
                if ((i+2 == arrayList.size()) && (j+1 == arrayList.size())) {
                    Statistics variable = new Statistics(arrayB);
                    return variable.sort().confidenceLevel().detectAndDelete().average();
                }
            }
        }
        return -999;
    }

    public static void poc_meth (Connection connection) throws Exception{
        Coefficients coefficients = new Coefficients();
        File targetDir = new File(System.getProperty("user.dir") + "\\DataFiles");
        File targetFile = new File(targetDir, "poc.txt");
        PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

        for (int i = 1001; i < 1036; i++) {
            String pocQuery = "select measurementDate, measurementTime, raingauge, intensity, amount, " +
                    "z1 from locatordata where raingauge = " + i + " and intensity is not null";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(pocQuery);
            if (!resultSet.next()) {continue;}

            while (resultSet.next()) {
                printStream.print(resultSet.getDate("measurementDate") + "\t"
                        + resultSet.getTime("measurementTime") + "\t"
                        + resultSet.getInt("rainGauge") + "\t"
                        + resultSet.getInt("z1") + "\t");
                printStream.printf(Locale.US, "%.2f\t%.2f\t%.1f\r\n",
                        resultSet.getDouble("amount"),
                        coefficients.getA(i) * Math.pow(Math.pow(10, (double) resultSet.getInt("z1") / 10), coefficients.getB(i)),
                        resultSet.getDouble("intensity")/6);
            }
        }
    }

    public static void printAverageCoeffs (double A, double b, int iterator) throws IOException{
        printingObject.out.print("средние коэффициенты для " + iterator + " :\r\n");
        printingObject.out.print("A = " + A + "\r\n");
        printingObject.out.print("B = " + b + "\r\n\n");
    }
}