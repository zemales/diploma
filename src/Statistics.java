import java.util.ArrayList;
import java.util.Collections;

public class Statistics {
    ArrayList<Double> coeffsArray;
    Statistics(ArrayList<Double> coeffsArray) {
        this.coeffsArray = coeffsArray;
    }

    ArrayList<Double> sort() {
        Collections.sort(coeffsArray);
        return coeffsArray;
    }

    ArrayList<Double> confidenceLevel(ArrayList<Double> coeffsArray) {
        ArrayList<Double> corrected = new ArrayList<>();
        for (int i = 0; i < coeffsArray.size(); i++) {
            if ((i > coeffsArray.size() * 0.025) && (i < coeffsArray.size() - coeffsArray.size()*0.025)){
                corrected.add(coeffsArray.get(i));
            }
        }
        return corrected;
    }

    public double average(ArrayList<Double> array) {
        double sumX = 0;
        for (Double x : array) {
            sumX += x;
        }
        return sumX/array.size();
    }

    public ArrayList<Double> detectAndDelete(ArrayList<Double> coeffsArray) {
        double numerator = 0;
        for (int i = 0; i < coeffsArray.size(); i++) {
            numerator += Math.pow(coeffsArray.get(i) - average(coeffsArray), 2);
        }
        double standardDeviation = Math.sqrt(numerator / coeffsArray.size());

        for (int i = 0; i < coeffsArray.size(); i++) {
            if (coeffsArray.get(i) >= 2*standardDeviation || coeffsArray.get(i) <= -2*standardDeviation) {
                coeffsArray.remove(i);
            }
        }
        return coeffsArray;
    }
}