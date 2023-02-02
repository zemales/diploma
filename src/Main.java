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

        Excel locatorSample2 = new Excel(new File(System.getProperty("user.dir") + "\\DataFiles\\locator sample 2.xlsx"));
        locatorSample2.parseExcel(connection);

        DataBase.generalSelection(connection);
        DataBase.perGauge(connection);
        DataBase.coefficientsSelect(connection);

        DataBase.poc_meth(connection);
    }
}
