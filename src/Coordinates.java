import java.util.HashMap;

public class Coordinates {
    private final int x;
    private final int y;
    static HashMap<Integer, Coordinates> coordinatesHashMap = new HashMap<>();

    static {
        coordinatesHashMap.put(22, new Coordinates(99, 95));
        coordinatesHashMap.put(25, new Coordinates(94, 100));
        coordinatesHashMap.put(26, new Coordinates(98, 100));
        coordinatesHashMap.put(32, new Coordinates(88, 93));
        coordinatesHashMap.put(34, new Coordinates(96, 97));
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
