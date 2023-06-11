import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres",
                    "postgres", "3515");
        } catch(SQLException e) {
            e.printStackTrace();
        }

        DataBase db = new DataBase(connection);
        db.gaugePerEvent();
        db.gaugesOnly_individual();
        db.CORR_selection();
    }
}
