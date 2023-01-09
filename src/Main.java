import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception{
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3515");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        DataBase.initialCreation(connection);
        System.out.println("creation success");

        Excel locatorSample2 = new Excel(new File(System.getProperty("user.dir") + "\\DataFiles\\locator sample 2.xlsx"));
        locatorSample2.parseExcel(connection);
        System.out.println("excel parse success");

        DataBase.generalSelection(connection);
        System.out.println("generalSelection success");
        DataBase.perGauge(connection);
        System.out.println("perGauge success");
        DataBase.coefficientsSelect(connection);
        System.out.println("coefficients select success");

        Coefficients coefficients = new Coefficients();

        poc_meth(connection);
    }

    public static void poc_meth (Connection connection) throws Exception{
        Coefficients coefficients = new Coefficients();
        File targetDir = new File(System.getProperty("user.dir") + "\\DataFiles");
        File targetFile = new File(targetDir, "poc for 1001.txt");
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
                        + resultSet.getInt("rainGauge") + "\t");
                printStream.printf(Locale.US, "%.2f\t%.2f\t%.1f\r\n",
                        resultSet.getDouble("amount"),
                        coefficients.getA(i) * Math.pow(Math.pow(10, (double) resultSet.getInt("z1") / 10), coefficients.getB(i)),
                        resultSet.getDouble("intensity")/6);
            }
        }
    }
}
