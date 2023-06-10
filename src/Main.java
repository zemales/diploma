import java.sql.*;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "3515");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        DataBase dataBase = new DataBase(connection);
        dataBase.coefficientsSelect();
    }
}
