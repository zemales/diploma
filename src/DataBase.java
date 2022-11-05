import org.apache.poi.ss.usermodel.Cell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Locale;

import static java.sql.Types.NULL;

/**
 * Класс DataBase создан для работы с СУБД PostgreSQL,
 * написания SQL-запросов на ввод и вывод данных.
 * У класса нет собственных переменных, только методы.
 *
 */
public class DataBase {
    /**
     * Просто создаем новую таблицу в PostgreSQL,
     * предварительно удалив предыдущую, если такая была
     */
    public static void initialCreation(Connection connection) throws SQLException {
        String creationQuery = "drop table if exists locatorData;" +
                "create table locatorData(" +
                "id SERIAL PRIMARY KEY," +
                "measurementDate date," +
                "measurementTime time," +
                "rainGauge int," +
                "amount numeric(3,2)," +
                "height int," +
                "intensity numeric(3,1)," +
                "z11 int," +
                "z10 int," +
                "z9 int," +
                "z8 int," +
                "z7 int," +
                "z6 int," +
                "z5 int," +
                "z4 int," +
                "z3 int," +
                "z2 int," +
                "z1 int);";
        Statement creationStatement = connection.createStatement();
        creationStatement.executeUpdate(creationQuery);
    }

    /**
     * Метод для ввода данных в базу данных, собраных в массив
     * во время выполнения метода locator() класса Excel.
     * Оператор switch подставляет каждое значение в надлежащее
     * поле таблицы SQL.
     * Здесь суммируются выпавшие осадки по осадкомеру
     * и приводятся к размерности [мм/10мин], так они вводятся в таблицу
     */
    public static void insertLocator(Connection connection, ArrayList<Cell> arrayList) throws SQLException{
        String query = "insert into locatorData (measurementDate, measurementTime, rainGauge, amount, height, intensity, z11, z10, z9, z8, z7, z6, z5, z4, z3, z2, z1) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(query/*, Statement.RETURN_GENERATED_KEYS*/);

        for (int i = 1; i < 18; i++) {
            switch (i) {
                case 1 : //Дата
                    preparedStatement.setObject(1, arrayList.get(0).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                    break;
                case 2: //Время
                    preparedStatement.setObject(2, arrayList.get(1).getLocalDateTimeCellValue().toLocalTime());
                    break;
                case 3: //номер осадкомера
                    preparedStatement.setInt(3, (int) arrayList.get(2).getNumericCellValue());
                    break;
                case 4: //количество выпавших осадков [мм/10мин]
                    preparedStatement.setDouble(4, new BigDecimal(arrayList.get(3).getNumericCellValue()
                            + arrayList.get(4).getNumericCellValue(), new MathContext(2)).doubleValue());
                    break;
                case 5: //высота облака
                    if (arrayList.get(5) == null) {preparedStatement.setNull(5, NULL);}
                    else {preparedStatement.setInt(5, (int) arrayList.get(5).getNumericCellValue());}
                    break;
                case 6: //интенсивность
                    if (arrayList.get(6) == null) {preparedStatement.setNull(6, NULL);}
                    else {preparedStatement.setDouble(6, arrayList.get(6).getNumericCellValue());}
                    break;
                case 7: //Z11
                    if (arrayList.get(7) == null) {preparedStatement.setNull(7, NULL);}
                    else {preparedStatement.setInt(7, (int) arrayList.get(7).getNumericCellValue());}
                    break;
                case 8: //Z10
                    if (arrayList.get(8) == null) {preparedStatement.setNull(8, NULL);}
                    else {preparedStatement.setDouble(8, arrayList.get(8).getNumericCellValue());}
                    break;
                case 9: //Z9
                    if (arrayList.get(9) == null) {preparedStatement.setNull(9, NULL);}
                    else {preparedStatement.setDouble(9, arrayList.get(9).getNumericCellValue());}
                    break;
                case 10: //Z8
                    if (arrayList.get(10) == null) {preparedStatement.setNull(10, NULL);}
                    else {preparedStatement.setDouble(10, arrayList.get(10).getNumericCellValue());}
                    break;
                case 11: //Z7
                    if (arrayList.get(11) == null) {preparedStatement.setNull(11, NULL);}
                    else {preparedStatement.setDouble(11, arrayList.get(11).getNumericCellValue());}
                    break;
                case 12: //Z6
                    if (arrayList.get(12) == null) {preparedStatement.setNull(12, NULL);}
                    else {preparedStatement.setDouble(12, arrayList.get(12).getNumericCellValue());}
                    break;
                case 13: //Z5
                    if (arrayList.get(13) == null) {preparedStatement.setNull(13, NULL);}
                    else {preparedStatement.setDouble(13, arrayList.get(13).getNumericCellValue());}
                    break;
                case 14: //Z4
                    if (arrayList.get(14) == null) {preparedStatement.setNull(14, NULL);}
                    else {preparedStatement.setDouble(14, arrayList.get(14).getNumericCellValue());}
                    break;
                case 15: //Z3
                    if (arrayList.get(15) == null) {preparedStatement.setNull(15, NULL);}
                    else {preparedStatement.setDouble(15, arrayList.get(15).getNumericCellValue());}
                    break;
                case 16: //Z2
                    if (arrayList.get(16) == null) {preparedStatement.setNull(16, NULL);}
                    else {preparedStatement.setDouble(16, arrayList.get(16).getNumericCellValue());}
                    break;
                case 17: //Z1
                    if (arrayList.get(17) == null) {preparedStatement.setNull(17, NULL);}
                    else {preparedStatement.setDouble(17, arrayList.get(17).getNumericCellValue());}
                    break;
            }
        }
        preparedStatement.execute();
        preparedStatement.close();
    }

    /**
     * Не используется
     */
    public static void delete(Connection connection) throws SQLException {
        String deleteQuery = "delete from meteorology " +
                "where (id = ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
        preparedStatement.setInt(1, 4);
        preparedStatement.execute();
        preparedStatement.close();
    }

    /**
     * Не используется
     */
    public static void truncateLocatorData(Connection connection) {
        try {
            String truncationQuery = "truncate locatorData;";
            PreparedStatement preparedStatement = connection.prepareStatement(truncationQuery);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для вывода значений для всех осадкомера
     * в один .txt файл для построения графика распределения точек
     */
    public static void generalSelection(Connection connection) throws Exception {
        //По запросу выводятся только пары Количество - Интенсивность,
        //где поле интенсивность в таблице не NULL
        String selectQuery = "select amount, intensity " +
                "from locatorData " +
                "where intensity is not null";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);

        File targetDir = new File(System.getProperty("user.dir") + "\\DataFiles");
        File targetFile = new File(targetDir, "dispersionFile.txt");
        PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

        /*
        В теле цикла while просходит деление интенсивности осадков по локатору,
        полученных из таблицы
        Перед занесением в файл .txt интенсивность делится на 6 для соответствия размерностей
        [10/час] / 6 = [мм/10мин]
        */
        while (resultSet.next()) {
            printStream.printf(Locale.US, "%.2f\t%.2f\r\n", resultSet.getDouble(1), (resultSet.getDouble(2)/6));
        }
        printStream.close();
    }

    /**
     * Метод для вывода количества выпавших осадков
     * и интенсивности осадков по локатору для каждого
     * отдельного осадкомера.
     * Результат выводится файлом .txt
     */
    public static void perGauge(Connection connection) throws Exception{
        //итерация по осадкомерам
        for (int i = 1001; i < 1036; i++ ) {
            //По запросу выводятся только пары Количество - Интенсивность,
            //где поле интенсивность в таблице не NULL
            String selectQuery = "select amount, intensity " +
                    "from locatorData " +
                    "where intensity is not null " +
                    "and rainGauge = " + i + ";";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            //если resultSet пустой, то for переходит к следующей итерации,
            // чтобы не создавать лишних файлов в папке per gauge
            if (!resultSet.next()) {continue;}

            File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
            File targetFile = new File(targetDirectory, i + ".txt");
            PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

            /*
            В теле цикла while просходит деление интенсивности осадков по локатору,
            полученных из таблицы
            Перед занесением в файл .txt интенсивность делится на 6 для соответствия размерностей
            [10/час] / 6 = [мм/10мин]
            */
            while (resultSet.next()) {
                printStream.printf(Locale.US, "%.2f\t%.2f\r\n", resultSet.getDouble(1), (resultSet.getDouble(2)/6));
            }

            printStream.close();

            //Вызов метода gnuplotCode для формирования файла .plt для построения графика
            Gnuplot.gnuplotCode(targetFile);
        }
    }

    /**
     * Метод coefficientsSelect() создан для выборки
     * значений специально для расчетов коэффициентов A и b
     * для каждого отдельного осадкомера
     */
    public static void coefficientsSelect(Connection connection) throws Exception {
        //итерации по осадкомерам
        for (int i = 1001; i < 1036; i++) {
            /*
            При количествее осадков = 0 расчет невозможен
            При z1 = 0 расчет невозможен
            При z1 = null расчет невозможен
            */
            String selectQuery = "select amount, z1 from locatorData " +
                    "where rainGauge = " + i +
                    " and amount != 0 " +
                    " and (z1 is not null and z1 != 0);";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);
            //если resultSet пустой, то for переходит к следующей итерации,
            // чтобы не создавать лишних файлов в папке per gauge
            if (!resultSet.next()) {continue;}

            /*создание двумерного массива arrayList
            под каждым индексом массива arrayList находится массив типа Double */
            ArrayList<Double[]> arrayList = new ArrayList<>();

            while (resultSet.next()) {
                //Вот тот самый массив типа Double, состоящий из 2х элементов
                Double[] doubleArray = new Double[2];
                //В нулевую позицию помещается Количество выпавших осадков
                doubleArray[0] = resultSet.getDouble("amount");
                //В первую позицию помещается значение отражаемости на уровне z1
                doubleArray[1] = resultSet.getDouble("z1");
                arrayList.add(doubleArray);
            }

            //двумерный массив, наполненный парами значений передается для вычисления коэффициентов
            DataBase.coeffCalculation(arrayList, i);
        }
    }

    /**
     * Метод coeffCalculation() создан для вычисления всех вариантов
     * коэффициентов A и b из системы уравнений
     * I1 = A * z1 ^ b
     * I2 = A * z2 ^ b
     */
    public static void coeffCalculation(ArrayList<Double[]> arrayList, int iterator) throws IOException {
        File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
        File targetFile = new File(targetDirectory, iterator + "coefficients.txt");
        PrintStream printStream = new PrintStream(new FileOutputStream(targetFile));

        //массивы, в кототрых будут содержаться коэффициенты от каждой возможной комбинации
        ArrayList<Double> arrayA = new ArrayList<>();
        ArrayList<Double> arrayB = new ArrayList<>();

        //цикл для перебора всех комбинаций значений
        for (int i = 0; i < arrayList.size(); i++) {
            for (int j = i+1; j < arrayList.size(); j++) {
                //на каждую итерацию переменным присваиваются новые значения
                double I2 = arrayList.get(j)[0];
                double I1 = arrayList.get(i)[0];
                double Z2 = arrayList.get(j)[1];
                double Z1 = arrayList.get(i)[1];
                double b = Math.log(I2 / I1) /
                        Math.log(Math.pow(10, Z2/10) / Math.pow(10, Z1/10));
                double A = I1 / Math.pow(Math.pow(10, Z1/10), b);

                if ((I1 != I2) && (Z1 != Z2)) {
                    //значения выводятся в файл .txt
                    printStream.printf(Locale.US, "%.2e\t%.2f\r\n", A, b);
                    //значения каждого из коэффициентов заносятся в массив для вычисления средни значений
                    arrayA.add(A);
                    arrayB.add(b);
                }
            }
        }
        //вызов метода для вычисления средний коэффициентов для каждого осадкомера
        DataBase.printAverageCoeffs(arrayA, arrayB, iterator);
    }

    /**
     * Метод printAverageCoeffs() создан для вывода средних
     * значений коэффициентов A и b для каждого осадкомера в файл .txt
     */
    public static void printAverageCoeffs (ArrayList<Double> arrayA, ArrayList<Double> arrayB, int iterator) throws IOException {
        double sumA = 0;
        double sumB = 0;

        for (int i = 0; i < arrayA.size(); i++) {
            sumA += arrayA.get(i);
            sumB += arrayB.get(i);

            File targetDirectory = new File(System.getProperty("user.dir") + "\\DataFiles\\per gauge");
            File targetFile = new File(targetDirectory, iterator + "AverageCoefficients.txt");
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