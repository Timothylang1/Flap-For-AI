package NEAT_FROM_VIDEO_CODE;

import java.util.ArrayList;

/*
 * Helper class used to select a random element based on it's score
 */
public class RandomSelector<T> {
    private ArrayList<T> objects = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();

    private double total_score = 0;

    public void add(T element, double score) {
        objects.add(element);
        scores.add(score);
        total_score += score;
    }

    /*
     * Returns a random element, but a higher score means it's more likely to be selected
     */
    public T random() {
        double v = Math.random() * total_score;
        double c = 0;
        for (int i = 0; i < objects.size(); i++) {
            c += scores.get(i);
            if (c > v) {
                return objects.get(i);
            }
        }
        return null;
    }
}
