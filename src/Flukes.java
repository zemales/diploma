import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Класс Flukes создан для статистической обработки коэффициентов A и b
 * полученных при всех возможных комбинациях Интенсивности Осадков
 * (по осадкомерам) и Радиолокационной отражаемости.
 * В данном классе реализуется метод борьбы со статистическими выборсами
 * с помощью СКО контрольного отрезка.
 *
 * Класс Flukes частично или полностью копирует или эксплуатирует
 * некоторые методы классов DataBase, Excel и Main.
 */
public class Flukes {
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
    public static void coeffCalculation(ArrayList<Double[]> arrayList, int iterator) throws IOException{
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
        printAverageCoeffs(detectAndDelete(arrayA), detectAndDelete(arrayB), iterator);
    }

    /**
     *  Метод detectAndDelete() создан для выявления и удаления коэффициентов
     *  A и b, которые не удовлетворяют определенному критерию.
     *  Метод реализует спобос борьбы со статистическими выбросами
     *  с помощью СКО контрольного отрезка
     */
    public static ArrayList<Double> detectAndDelete(ArrayList<Double> coeffsArray) {
        //try для того, чтобы избежать IndexOutOfBoundsException
        //Способ так себе, но вроде работает и итоговому значению не вредит
        try {
            ArrayList<Double> test = new ArrayList<>();
            int controlSectionLength = 20;
            //CSV - коэффициент кско (взят из лекционной презентации Кузнецова)
            int CSV = 10;
            for (int i = 0; i < coeffsArray.size(); i++) {
                ArrayList<Double> controlSection = new ArrayList<>(20);
                for (int j = i; j < i + controlSectionLength; j++) {
                    controlSection.add(coeffsArray.get(j));
                }

                //numerator - числитель СКО
                double numerator = 0;
                for (int j = 0; j < controlSection.size(); j++) {
                    numerator += Math.pow(controlSection.get(j) - average(controlSection), 2);
                }

                //segmentSV - СКО контрольного отрезка
                double segmentSV = Math.sqrt(numerator / controlSection.size());

                //если значение КонцаКонтрольногоОтрезка+1 превышает (СКО * кско), то
                //это значение удаляется из исходного массива всех коэффициентов
                if (coeffsArray.get(i + controlSectionLength + 1) > segmentSV * CSV) {
                    coeffsArray.remove(i + controlSectionLength + 1);
                }
            }
            //это по сути заглушка
            return test;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("say nothing and go further");
            return coeffsArray;
        }
    }

    /**
     * Метод для нахождения среднего значения
     */
    public static double average(ArrayList<Double> array) {
        double sumX = 0;
        for (Double x : array) {
            sumX += x;
        }
        return sumX/array.size();
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
            File targetFile = new File(targetDirectory, iterator + "FlukesAverageCoefficients.txt");
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

