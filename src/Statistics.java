import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Класс Statistics создан для статистической обработки коэффициентов A и b
 * полученных при всех возможных комбинациях Интенсивности Осадков
 * (по осадкомерам) и Радиолокационной отражаемости.
 * В данном классе не реализуются никакие строго определенные методы
 * статистической обработки. Я просто решил посмотреть, что будет, если
 * отбросить 2,5% наименьших и 2,5% наибольших значений коэффициентов
 * и уже от этого брать среднее значение.
 *
 * Класс Statistics частично или полностью копирует или эксплуатирует
 * некоторые методы классов DataBase, Excel и Main.
 */
public class Statistics {
    public static void main(String[] args) throws Exception{
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3515");

        DataBase.initialCreation(connection);
        System.out.println("creation success");
        Excel locatorSample2 = new Excel(new File(System.getProperty("user.dir") + "\\DataFiles\\locator sample 2.xlsx"));
        locatorSample2.locator(connection);
        System.out.println("continue of main meth");

        coefficientsSelect(connection);
        System.out.println("coefficients select success");
    }

    /**
     * Копия метода coefficientSelect из класса DataBase
     */
    public static void coefficientsSelect(Connection connection) throws Exception {
        for (int i = 1001; i < 1036; i++) {
            String selectQuery = "select amount, z1 from locatorData " +
                    "where rainGauge = " + i +
                    " and amount != 0 " +
                    " and (z1 is not null and z1 != 0);";
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
            coeffCalculation(arrayList, i);
        }
    }

    /**
     * Копия метода coeffCalculation из класса DataBase
     * Но в конце метоа вызывается метод printAverageCoefficients()
     * с ообновленными аргументами
     */
    public static void coeffCalculation(ArrayList<Double[]> arrayList, int iterator) throws IOException {
        ArrayList<Double> arrayA = new ArrayList<>();
        ArrayList<Double> arrayB = new ArrayList<>();

        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i+1; j < arrayList.size(); j++) {
                double I2 = arrayList.get(j)[0];
                double I1 = arrayList.get(i)[0];
                double Z2 = arrayList.get(j)[1];
                double Z1 = arrayList.get(i)[1];
                double b = Math.log(I2 / I1) /
                        Math.log(Math.pow(10, Z2/10) / Math.pow(10, Z1/10));
                double A = I1 / Math.pow(Math.pow(10, Z1/10), b);

                if ((I1 != I2) && (Z1 != Z2)) {
                    arrayA.add(A);
                    arrayB.add(b);
                }
            }
        }

        printAverageCoeffs(confidenceLevel(sort(arrayA)), confidenceLevel(sort(arrayB)), iterator);
    }

    /**
     * Выполняется сортировка массива с коэффциентами, полученного
     * из всех комбинаций.
     */
    public static ArrayList<Double> sort(ArrayList<Double> coeffsArray) {
        Collections.sort(coeffsArray);
        return coeffsArray;
    }

    /**
     * В массив corrected записываются 95% значений.
     * Он возвращается.
     */
    public static ArrayList<Double> confidenceLevel(ArrayList<Double> coeffsArray) {
        ArrayList<Double> corrected = new ArrayList<>();
        for (int i = 0; i < coeffsArray.size(); i++) {
            if ((i > coeffsArray.size() * 0.025) && (i < coeffsArray.size() - coeffsArray.size()*0.025)){
                corrected.add(coeffsArray.get(i));
            }
        }
        return corrected;
    }

    /**
     * Метод printAverageCoeffs() скопирован из класса DataBase
     * за тем исключением, что в нем изменено имя выходного файла
     */
    public static void printAverageCoeffs(ArrayList<Double> arrayA, ArrayList<Double> arrayB, int iterator) throws IOException {
        double sumA = 0;
        double sumB = 0;

        for (int i = 0; i < arrayA.size(); i++) {
            sumA += arrayA.get(i);
            sumB += arrayB.get(i);

            File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
            File targetFile = new File(targetDirectory, iterator + "StatisticsAverageCoefficients.txt");
            PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

            printStream.print("средние коэффициенты для " + iterator + " :\r\n");
            printStream.print("A = ");
            printStream.printf(Locale.US, "%.1e\r\n", sumA/arrayA.size());
            printStream.print("B = ");
            printStream.printf(Locale.US, "%.1e\r\n", sumB/arrayB.size());
            printStream.close();
        }
    }
}
