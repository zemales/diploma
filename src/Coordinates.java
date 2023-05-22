import java.util.HashMap;

public class Coordinates {
    private final int x;
    private final int y;
    private static final HashMap<Integer, Coordinates> coordinatesHashMap = new HashMap<>();

    static {
        coordinatesHashMap.put(1022, new Coordinates(99, 95)); //pluvio
        coordinatesHashMap.put(1025, new Coordinates(94, 100)); //pluvio
        coordinatesHashMap.put(1026, new Coordinates(98, 100)); //pluvio
        coordinatesHashMap.put(1032, new Coordinates(88, 93)); //pluvio
        coordinatesHashMap.put(1034, new Coordinates(96, 97)); //pluvio
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }
}
