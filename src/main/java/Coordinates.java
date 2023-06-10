import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Coordinates {
    private final int x;
    private final int y;
    private static final HashMap<Integer, Coordinates> coordinatesHashMap = new HashMap<>();

    static {
        coordinatesHashMap.put(1001, new Coordinates(96, 96));
        coordinatesHashMap.put(1002, new Coordinates(91, 95));
        coordinatesHashMap.put(1003, new Coordinates(80, 94));
        coordinatesHashMap.put(1004, new Coordinates(93, 88));
        coordinatesHashMap.put(1005, new Coordinates(98, 92));
        coordinatesHashMap.put(1006, new Coordinates(81 ,102));
        coordinatesHashMap.put(1007, new Coordinates(85, 103));
        coordinatesHashMap.put(1008, new Coordinates(91, 103));
        coordinatesHashMap.put(1009, new Coordinates(91, 105));
        coordinatesHashMap.put(1010, new Coordinates(116, 104));
        coordinatesHashMap.put(1011, new Coordinates(104, 110));
        coordinatesHashMap.put(1013, new Coordinates(101, 112));
        coordinatesHashMap.put(1014, new Coordinates(102, 99));
        coordinatesHashMap.put(1015, new Coordinates(99, 97));
        coordinatesHashMap.put(1016, new Coordinates(91, 112));
        coordinatesHashMap.put(1017, new Coordinates(95, 96));
        coordinatesHashMap.put(1018, new Coordinates(80, 84));
        coordinatesHashMap.put(1019, new Coordinates(86, 86));
        coordinatesHashMap.put(1020, new Coordinates(87, 90));
        coordinatesHashMap.put(1021, new Coordinates(92, 91));
        coordinatesHashMap.put(1022, new Coordinates(99, 95));
        coordinatesHashMap.put(1023, new Coordinates(100, 94));
        coordinatesHashMap.put(1024, new Coordinates(90, 103));
        coordinatesHashMap.put(1025, new Coordinates(94, 100));
        coordinatesHashMap.put(1026, new Coordinates(98, 100));
        coordinatesHashMap.put(1027, new Coordinates(97, 104));
        coordinatesHashMap.put(1028, new Coordinates(101, 103));
        coordinatesHashMap.put(1029, new Coordinates(104, 105));
        coordinatesHashMap.put(1030, new Coordinates(97, 108));
        coordinatesHashMap.put(1031, new Coordinates(93, 106));
        coordinatesHashMap.put(1032, new Coordinates(88, 93));
        coordinatesHashMap.put(1034, new Coordinates(96, 97));
        coordinatesHashMap.put(1035, new Coordinates(107, 98));
    }

    public Coordinates() {
        x = -999;
        y = -999;
    }

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public HashMap<Integer, Coordinates> getCoordinatesHashMap() {
        return coordinatesHashMap;
    }

    public static String coordinator() {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<Integer, Coordinates> entry : coordinatesHashMap.entrySet()) {
            result.append("(p.rainGauge = ").append(entry.getKey());
            result.append(" AND l.x = ").append(entry.getValue().getX());
            result.append(" AND l.y = ").append(entry.getValue().getY()).append(") \nOR ");
        }
        return result.delete(result.length()-3, result.length()).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
