import java.util.ArrayList;

public class Test {
    public static void main(String[] args) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 351; i++) {
            arrayList.add(i + 100);
        }

        display(correct1(arrayList));
    }

    static ArrayList<Integer> correct1 (ArrayList<Integer> arrayList) {
        ArrayList<Integer> corrected = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            if ((i > arrayList.size() * 0.025) && (i < arrayList.size() - arrayList.size()*0.025)) {
                corrected.add(arrayList.get(i));
            }
        }
        System.out.println(arrayList.size()*0.025);
        System.out.println(arrayList.size() - arrayList.size()*0.025);
        return corrected;
    }
    static void display(ArrayList<Integer> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            System.out.println(arrayList.get(i));
        }
    }
}
