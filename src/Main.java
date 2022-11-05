import java.io.File;
import java.sql.*;

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
        locatorSample2.locator(connection);
        System.out.println("continue of main meth");

        DataBase.generalSelection(connection);
        System.out.println("generalSelection success");
        DataBase.perGauge(connection);
        System.out.println("perGauge success");
        DataBase.coefficientsSelect(connection);
        System.out.println("coefficients select success");
    }
}
