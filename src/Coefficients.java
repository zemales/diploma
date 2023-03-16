import java.util.HashMap;
import java.util.Map;

public class Coefficients {
    private final double A;
    private final double b;
    private static final HashMap<WeatherEvents, Coefficients> coefficientsHashMap = new HashMap<>();

    public HashMap<WeatherEvents, Coefficients> getCoefficientsHashMap() {
        return coefficientsHashMap;
    }

    Coefficients() {
        A = -999.0;
        b = -999.0;
    }

    public Coefficients(double A, double b) {
        this.A = A;
        this.b = b;
    }

    public double getA() {
        return A;
    }

    public double getB() {
        return b;
    }

    public void show() {
        for (Map.Entry<WeatherEvents, Coefficients> entry : coefficientsHashMap.entrySet()) {
            System.out.println(entry.getKey() + ":\tA: " +
                    entry.getValue().getA() + ",\tb: " + entry.getValue().getB());
        }
    }
}