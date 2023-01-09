import java.util.ArrayList;

public class Coefficients {
    static ArrayList<Double[]> container = new ArrayList<>();

    Coefficients() {
    }

    Coefficients(double A, double b, int rainGauge) {
        if (duplicateCheck(rainGauge)) {
            Double[] content = new Double[3];
            content[0] = A;
            content[1] = b;
            content[2] = (double) rainGauge;

            container.add(content);
        }
        else {
            System.out.println("Данный rainGauge уже есть в массиве");
        }
    }

    boolean duplicateCheck(int rainGauge) {
        for (Double[] var : container) {
            if (var[2] == rainGauge) {
                return false;
            }
        }
        return true;
    }

    void show() {
        for (Double[] var : container) {
            System.out.println("Для осадкомера " + var[2]);
            System.out.println("A = " + var[0]);
            System.out.println("b = " + var[1] + "\n");
        }
    }

    /**
     * FOLLOWING CODE IS SHIIIIIIIIIT
     */
    double getA(int rainGauge) {
        for (Double[] var : container) {
            if (var[2] == rainGauge) {
                return var[0];
            }
        }
        return -999.0;
    }

    double getB(int rainGauge) {
        for (Double[] var : container) {
            if (var[2] == rainGauge) {
                return var[1];
            }
        }
        return -999.0;
    }
}