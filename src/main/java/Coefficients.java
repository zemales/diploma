import java.util.HashMap;
import java.util.Map;

public class Coefficients {
    private final double A;
    private final double b;
    private static final HashMap<WeatherEvents, Coefficients> coefficientsHashMap = new HashMap<>();

    public HashMap<WeatherEvents, Coefficients> getCoefficientsHashMap() {
        return coefficientsHashMap;
    }

    static final HashMap<Coordinates, Coefficients> individual = new HashMap<>();

    public HashMap<Coordinates, Coefficients> getIndividual() {return individual;}

    static {
        individual.put(new Coordinates(96, 96), new Coefficients(375, 1.4)); //1001
        individual.put(new Coordinates(91, 95), new Coefficients(378, 1.2)); //1002
        individual.put(new Coordinates(80, 94), new Coefficients(159, 0.4)); //1003
        individual.put(new Coordinates(93, 88), new Coefficients(242, 1.0)); //1004
        individual.put(new Coordinates(98, 92), new Coefficients(258, 1.2)); //1005
        individual.put(new Coordinates(81 ,102), new Coefficients(211, 1.0)); //1006
        individual.put(new Coordinates(85, 103), new Coefficients(217, 1.1)); //1007
        individual.put(new Coordinates(91, 103), new Coefficients(332, 1.3)); //1008
        individual.put(new Coordinates(91, 105), new Coefficients(310, 1.2)); //1009
        //individual.put(new Coordinates(116, 104), new Coefficients(237, 0.5)); //1010
        individual.put(new Coordinates(104, 110), new Coefficients(199, 1.0)); //1011
        individual.put(new Coordinates(101, 112), new Coefficients(290, 1.0)); //1013
        individual.put(new Coordinates(102, 99), new Coefficients(449, 1.3)); //1014
        individual.put(new Coordinates(99, 97), new Coefficients(332, 1.4)); //1015
        individual.put(new Coordinates(91, 112), new Coefficients(263, 1.4)); //1016
        individual.put(new Coordinates(95, 96), new Coefficients(361, 1.3)); //1017
        individual.put(new Coordinates(80, 84), new Coefficients(256, 0.8)); //1018
        individual.put(new Coordinates(86, 86), new Coefficients(259, 0.9)); //1019
        individual.put(new Coordinates(87, 90), new Coefficients(288, 0.8)); //1020
        individual.put(new Coordinates(92, 91), new Coefficients(266, 1.2)); //1021
        individual.put(new Coordinates(99, 95), new Coefficients(377, 1.2)); //1022
        individual.put(new Coordinates(100, 94), new Coefficients(324, 1.1)); //1023
        individual.put(new Coordinates(90, 103), new Coefficients(273, 1.1)); //1024
        individual.put(new Coordinates(94, 100), new Coefficients(725, 1.1)); //1025
        individual.put(new Coordinates(98, 100), new Coefficients(546, 1.1)); //1026
        individual.put(new Coordinates(97, 104), new Coefficients(447, 1.3)); //1027
        individual.put(new Coordinates(101, 103), new Coefficients(458, 1.3)); //1028
        individual.put(new Coordinates(104, 105), new Coefficients(331, 1.2)); //1029
        individual.put(new Coordinates(97, 108), new Coefficients(306, 1.3)); //1030
        individual.put(new Coordinates(93, 106), new Coefficients(287, 1.2)); //1031
        individual.put(new Coordinates(88, 93), new Coefficients(254, 1.2)); //1032
        individual.put(new Coordinates(96, 97), new Coefficients(443, 1.4)); //1034
        individual.put(new Coordinates(107, 98), new Coefficients(263, 1.3)); //1035
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