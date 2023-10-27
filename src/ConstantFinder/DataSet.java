package ConstantFinder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import NEAT.Neural_Constants;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class DataSet {
    private ArrayList<HashMap<String, Double>> data;
    public HashMap<String, Line> vectorSet = new HashMap<>();
    public static final String generation_key = "Average generations";
    public static double scale_line;
    public static double scale_circle;

    // Comparator used for sorting data
    private static final Comparator<Entry<String, Double>> compare = new Comparator<Entry<String, Double>>() {
        @Override
        public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
            // If they're roughly the same, then we compare the strings to try and maintain some consistency
            if (Math.abs(arg0.getValue() - arg1.getValue()) < Neural_Constants.CHANGE_DIFFERENCE / 100) {
                return arg0.getKey().compareTo(arg1.getKey());
            }
            else if (arg0.getValue() < arg1.getValue()) return 1;
            else return -1;
        }
    };

    public DataSet(ArrayList<HashMap<String, Double>> data) {
        this.data = data;

        // Create vectors
        for (String key : VisualizeConstants.keySet) {
            Line vector = new Line(0, 0, 0, 0);
            vectorSet.put(key, vector);
        }
    }

    private void updateInstance(HashMap<String, Double> weights) {
        // Sort the weights and then update vectors in order of sorted weights. We also remove the generation key because that isn't a parameter
        List<Entry<String, Double>> sorted_entries = weights.entrySet().stream().filter(x -> !x.getKey().equals(generation_key)).sorted(compare).toList();
        Point previous_location = new Point(VisualizeConstants.CANVAS_SIZE / 2, VisualizeConstants.CANVAS_SIZE / 2);
        for (Entry<String, Double> entry : sorted_entries) {
            // Set start position to previous point
            vectorSet.get(entry.getKey()).setStartPosition(previous_location);

            // Calculate end point
            Point end_location = new Point(
                previous_location.getX(), 
                previous_location.getY());
            
            vectorSet.get(entry.getKey()).setEndPosition(end_location);

            // Set previous location to end location so next vector starts at the end of the previous vector
            previous_location = end_location;
        }

        // Add in node for end point
    }

    public static void main(String[] args) {
        ArrayList<HashMap<String, Double>> example = new ArrayList<>();
        HashMap<String, Double> test = new HashMap<>();
        example.add(test);
        test.put("Add C", 1.0);
        test.put("Add N", .3);
        test.put("Mod Wgt", .05);
        test.put("Diff", .4);
        test.put("Avg. W", .05);
        test.put("Excess", .09);
        test.put("Average generations", 5.078);
        DataSet data = new DataSet(example);
        data.updateInstance(test);
    }
}
