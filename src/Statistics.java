import java.util.ArrayList;
import java.util.Collections;

public class Statistics {
    ArrayList<Double> coeffsArray;
    Statistics(ArrayList<Double> coeffsArray) {
        this.coeffsArray = coeffsArray;
    }

    Statistics sort() {
        Collections.sort(coeffsArray);
        return this;
    }

    Statistics confidenceLevel() {
        for (int i = 0; i < coeffsArray.size(); i++) {
            if ((i < coeffsArray.size() * 0.025) && (i > coeffsArray.size()*0.975)){
                coeffsArray.remove(i);
            }
        }
        return this;
    }

    double average() {
        double sumX = 0;
        for (Double x : coeffsArray) {
            sumX += x;
        }
        return sumX/coeffsArray.size();
    }

    Statistics detectAndDelete() {
        double numerator = 0;
        double average = average();
        for (int i = 0; i < coeffsArray.size(); i++) {
            numerator += Math.pow(coeffsArray.get(i) - average, 2);
        }
        double standardDeviation = Math.sqrt(numerator / coeffsArray.size());

        for (int i = 0; i < coeffsArray.size(); i++) {
            if (coeffsArray.get(i) >= 2*standardDeviation || coeffsArray.get(i) <= -2*standardDeviation) {
                coeffsArray.remove(i);
            }
        }
        return this;
    }
}