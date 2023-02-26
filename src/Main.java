import java.sql.*;

public class Main {
    public static void main(String[] args) throws Exception{
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3515");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        DataBase.generalSelection(connection);
        DataBase.perGauge(connection);
        DataBase.coefficientsSelect(connection);

        DataBase.poc_meth(connection);

        Coefficients object = new Coefficients();
    }
}
