import java.sql.*;
import java.util.*;

public class DataBase {
    private final Connection connection;
    static Printing printingObject = new Printing("000AAA.txt");
    public DataBase(Connection connection) {
        this.connection = connection;
    }

    public static String coordinator() {
        StringBuilder result = new StringBuilder();
        Coordinates coordinates = new Coordinates();

        for (Map.Entry<Integer, Coordinates> entry : coordinates.getCoordinatesHashMap().entrySet()) {
            result.append("(p.rainGauge = ").append(entry.getKey());
            result.append(" AND l.x = ").append(entry.getValue().getX());
            result.append(" AND l.y = ").append(entry.getValue().getY()).append(") \nOR ");
        }
        return result.delete(result.length()-3, result.length()).toString();
    }

    public void coefficientsSelect() {
        try {
            for (WeatherEvents event : WeatherEvents.values()) {
                String Query = "select l.z1, p.amount*6 as amount " +
                        "from locator as l " +
                        "inner join precipitation as p " +
                        "on l.date = p.measurementDate " +
                        "and l.time = p.measurementTime " +
                        "where (" + DataBase.coordinator() + ")" +
                        "and weatherEvent = '" + event.getWeatherCode() + "' " +
                        "and l.z1 between " + event.getZ1MIN() + " and " + event.getZ1MAX() +
                        " and p.amount > 0.5" +
                        "and date between '2019-06-01' and '2019-08-31'";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(Query);

                if (!resultSet.next()) {
                    continue;
                }
                ArrayList<Double[]> dataArray = new ArrayList<>();
                while (resultSet.next()) {
                    dataArray.add(new Double[]{
                            resultSet.getDouble("z1"),
                            resultSet.getDouble("amount")
                    });
                }

                aCoeffCalculation(dataArray, event);
            }

            Coefficients coefficients = new Coefficients();
            coefficients.show();
        } catch (SQLException e) {
            e. printStackTrace();
        }
    }

    public static void aCoeffCalculation(ArrayList<Double[]> dataArray, WeatherEvents event) {
        double bCoeff = Math.round(bCoeffCalculation(dataArray)*100.0)/100.0;
        ArrayList<Double> arrayA = new ArrayList<>();

        for (int i = 0; i < dataArray.size(); i++) {
            for (int j = i+1; j < dataArray.size(); j++) {
                double I2 = dataArray.get(j)[0];
                double I1 = dataArray.get(i)[0];
                double Z2 = dataArray.get(j)[1];
                double Z1 = dataArray.get(i)[1];
                double A = I1 / Math.pow(Math.pow(10, Z1/10.0), bCoeff);

                if ((I1 != I2) && (Z1 != Z2)) {
                    arrayA.add(A);
                }
                if ((i+2 == dataArray.size()) && (j+1 == dataArray.size())) {
                    Statistics varA = new Statistics(arrayA);

                    double ACoeff = Math.round(varA.sort().confidenceLevel()
                            .detectAndDelete().average()*1000000.0)/1000000.0;
                    //new Coefficients(coefA, b, iterator);
                    Coefficients coefficients = new Coefficients();
                    coefficients.getCoefficientsHashMap().put(event, new Coefficients(ACoeff, bCoeff));


                    printAverageCoeffs(ACoeff, bCoeff, event);

                }
            }
        }
    }

    public static double bCoeffCalculation(ArrayList<Double[]> dataArray) {
        ArrayList<Double> arrayB = new ArrayList<>();

        for (int i = 0; i < dataArray.size(); i++) {
            for (int j = i+1; j < dataArray.size(); j++) {
                double I2 = dataArray.get(j)[0];
                double I1 = dataArray.get(i)[0];
                double Z2 = dataArray.get(j)[1];
                double Z1 = dataArray.get(i)[1];

                double b = Math.log(Math.pow(10, Z2/10.0) / Math.pow(10, Z1/10.0)) /
                        Math.log(I2 / I1);

                if ((I1 != I2) && (Z1 != Z2)) {
                    arrayB.add(b);
                }
                if ((i+2 == dataArray.size()) && (j+1 == dataArray.size())) {
                    Statistics variable = new Statistics(arrayB);
                    return variable.sort().confidenceLevel().detectAndDelete().average();
                }
            }
        }
        return -999;
    }



    public static void printAverageCoeffs (double A, double b, WeatherEvents events) {
        printingObject.out.print("средние коэффициенты для " + events.getWeatherCode() + " :\r\n");
        printingObject.out.print("A = " + A + "\r\n");
        printingObject.out.print("B = " + b + "\r\n\n");
    }
}