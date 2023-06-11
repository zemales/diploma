import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;
import java.util.Map;

public class DataBase {
    private final Connection connection;
    private final File targetDir = new File(System.getProperty("user.dir") + "\\DataFiles\\MLS");

    DataBase(Connection connection) {
        this.connection = connection;
    }

    void gaugesOnly_individual() {
        File targetDirectory = new File(targetDir, "\\gaugesOnly");
        Coordinates coordinates = new Coordinates();
        WeatherEvents weatherEvents = WeatherEvents.NULL;
        try {
            for (Map.Entry<Integer, Coordinates> values : coordinates.getCoordinatesHashMap().entrySet()) {
                String query = " WITH general AS (SELECT " +
                        "ROUND(POWER(10, l.z1/10.0), 0) as refl, p.amount*6 as intensity, rainGauge " +
                        "FROM locatorFinal as l " +
                        "INNER JOIN precipitation as p " +
                        "ON l.date = p.measurementDate " +
                        "AND l.time = p.measurementTime " +
                        "AND rainGauge = " + values.getKey() + " " +
                        "AND l.x = " + values.getValue().getX() + " " +
                        "AND l.y = " + values.getValue().getY() + " " +
                        "AND weatherEvent IN (" + weatherEvents.rainable() + ") " +
                        "AND amount*6 > 0.6), " +
                        "" +
                        "limits AS (" +
                        "   SELECT " +
                        "       refl, " +
                        "       AVG(intensity) + (STDDEV(intensity)) as upper_limit " +
                        "   FROM general " +
                        "   GROUP BY refl" +
                        ")" +
                        "" +
                        "SELECT gen.refl, gen.intensity, l.upper_limit " +
                        "FROM general AS gen " +
                        "JOIN (" +
                        "   SELECT refl, MAX(upper_limit) AS upper_limit" +
                        "   FROM limits " +
                        "   GROUP BY refl " +
                        ") AS l ON gen.refl = l.refl " +
                        "WHERE gen.intensity <= l.upper_limit " +
                        "   AND rainGauge = " + values.getKey() + " " +
                        "ORDER BY gen.refl desc, gen.intensity";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                if (!resultSet.next()) {continue;}

                File targetFile = new File(targetDirectory, values.getKey() + ".txt");
                PrintStream out = new PrintStream(targetFile);

                while (resultSet.next()) {
                    out.printf(Locale.US, "%.2f\t%s\r\n",
                            resultSet.getDouble("intensity"),
                            resultSet.getInt("refl"));
                }
                out.close();
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    void CORR_selection() {
        File targetDirectory = new File(targetDir, "\\CORR\\08");
        File targetFile = new File(targetDirectory, "CORR_general.txt");
        Coordinates coordinates = new Coordinates();
        Coefficients coefficients = new Coefficients();
        WeatherEvents weatherEvents = WeatherEvents.NULL;
        try (PrintStream out = new PrintStream(targetFile)) {
            for (Map.Entry<Integer, Coordinates> values : coordinates.getCoordinatesHashMap().entrySet()) {
                String query = " WITH general AS (SELECT " +
                        "ROUND(POWER(10, l.z1/10.0), 0) as refl, p.amount*6 as intensity, rainGauge, x, y " +
                        "FROM locatorFinal as l " +
                        "INNER JOIN precipitation as p " +
                        "ON l.date = p.measurementDate " +
                        "AND l.time = p.measurementTime " +
                        "AND rainGauge = " + values.getKey() + " " +
                        "AND l.x = " + values.getValue().getX() + " " +
                        "AND l.y = " + values.getValue().getY() + " " +
                        "AND weatherEvent IN (" + weatherEvents.rainable() + ") " +
                        "AND amount*6 > 0.6), " +
                        "" +
                        "limits AS (" +
                        "   SELECT " +
                        "       refl, " +
                        "       AVG(intensity) + (STDDEV(intensity)) as upper_limit " +
                        "   FROM general " +
                        "   GROUP BY refl" +
                        ")" +
                        "" +
                        "SELECT MIN(x) as x, MIN(y) as y, MIN(rainGauge) as rainGauge, " +
                        "   CORR(gen.refl, gen.intensity) AS IwithI, " +
                        "   CORR(ROUND(POWER((gen.refl/200), 1/1.6), 1), gen.intensity) as Marshall, " +
                        "   CORR(ROUND(POWER((gen.refl/" + coefficients.getIndividual().get(values.getValue()).getA() +
                        "), 1/" + coefficients.getIndividual().get(values.getValue()).getB() + "), 1), gen.intensity) as new " +
                        "FROM general AS gen " +
                        "JOIN (" +
                        "   SELECT refl, MAX(upper_limit) AS upper_limit" +
                        "   FROM limits " +
                        "   GROUP BY refl " +
                        ") AS l ON gen.refl = l.refl " +
                        "WHERE gen.intensity <= l.upper_limit " +
                        "   AND rainGauge = " + values.getKey() + " ";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);

                while (resultSet.next()) {
                    out.printf(Locale.US, "%s\t%s\t%.2f\t%.2f\t%.2f\t%s\r\n",
                            resultSet.getInt("x"),
                            resultSet.getInt("y"),
                            resultSet.getDouble("IwithI"),
                            resultSet.getDouble("Marshall"),
                            resultSet.getDouble("new"),
                            resultSet.getInt("rainGauge"));
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    void gaugePerEvent() {
        File targetDirectory = new File(targetDir, "\\gaugePerEvent");
        Coordinates coordinates = new Coordinates();
        try {
            for (WeatherEvents events : WeatherEvents.values()) {
                File targetFile = new File(targetDirectory, events + ".txt");
                FileOutputStream outputStream = new FileOutputStream(targetFile, true);
                PrintStream out = new PrintStream(outputStream);
                for (Map.Entry<Integer, Coordinates> values : coordinates.getCoordinatesHashMap().entrySet()) {
                    String query = "SELECT " +
                            "l.x, l.y, stddev(amount*6) as std, rainGauge " +
                            "FROM locatorFinal as l " +
                            "INNER JOIN precipitation as p " +
                            "ON l.date = p.measurementDate " +
                            "AND l.time = p.measurementTime " +
                            "AND weatherEvent LIKE '" + events.getWeatherCode() + "%' " +
                            "AND weatherEvent IN (" + events.rainable() + ") " +
                            "AND p.rainGauge = " + values.getKey() + " " +
                            "AND l.x = " + values.getValue().getX() + " " +
                            "AND l.y = " + values.getValue().getY() + " " +
                            "AND p.amount*6 > 0.6 " +
                            "GROUP BY l.x, l.y, rainGauge";
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery(query);

                    while (resultSet.next()) {
                        out.printf(Locale.US, "%s\t%s\t%.3f\t%s\r\n",
                                resultSet.getInt("x"),
                                resultSet.getInt("y"),
                                resultSet.getDouble("std"),
                                resultSet.getInt("rainGauge"));
                    }
                }
                out.close();
                Gnuplot.gnuplotStandardDeviation(targetFile);
            }
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
}
