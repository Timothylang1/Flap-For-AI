package ConstantFinder;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import NEAT.Neural_Constants;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class DataSet {
    private ArrayList<HashMap<String, Double>> data;
    public HashMap<String, Line> vectorSet = new HashMap<>();
    public static final String GENERATION_KEY = "Average generations";
    public static double scale_line;
    public static double scale_circle_color;
    public Ellipse final_point = new Ellipse(0, 0, 10, 10);

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

    public DataSet(ArrayList<HashMap<String, Double>> data, CanvasWindow canvas) {
        this.data = data;

        // Create vectors
        for (String key : VisualizeConstants.keySet) {
            Line vector = new Line(0, 0, 0, 0);
            vector.setStrokeWidth(3);
            vectorSet.put(key, vector);
            canvas.add(vector);
        }
        canvas.add(final_point);
    }

    public boolean update(int current_number) {
        // Get the next weight available. If no weight available, then do nothing
        if (current_number >= data.size()) return false;
        HashMap<String, Double> weights = data.get(current_number);

        // Sort the weights and then update vectors in order of sorted weights. We also remove the generation key because that isn't a parameter
        List<Entry<String, Double>> sorted_entries = weights.entrySet().stream().filter(x -> VisualizeConstants.keySet.contains(x.getKey())).sorted(compare).toList();
        Point previous_location = new Point(VisualizeConstants.CANVAS_SIZE / 2, VisualizeConstants.CANVAS_SIZE / 2);
        for (Entry<String, Double> entry : sorted_entries) {

            // Set start position to previous point
            vectorSet.get(entry.getKey()).setStartPosition(previous_location);

            // Calculate end point
            Point end_location = new Point(
                previous_location.getX() + entry.getValue() * Math.cos(Math.PI * 2 / VisualizeConstants.keySet.size() * (VisualizeConstants.keySet.indexOf(entry.getKey()) + 0.5)) * DataSet.scale_line, 
                previous_location.getY() + entry.getValue() * -Math.sin(Math.PI * 2 / VisualizeConstants.keySet.size() * (VisualizeConstants.keySet.indexOf(entry.getKey()) + 0.5)) * DataSet.scale_line);
            
            vectorSet.get(entry.getKey()).setEndPosition(end_location);

            // Set previous location to end location so next vector starts at the end of the previous vector
            previous_location = end_location;
        }

        // Whatever the last location is, we update the final ellipse to have that location and then scale it based on the weight
        // The darker the color, the better the average generation
        final_point.setCenter(previous_location);
        final_point.setFillColor(new Color((int) (255 - weights.get(GENERATION_KEY) * scale_circle_color)));

        // Return true because successfully ran
        return true;
    }

    // public static void main(String[] args) {
    //     ArrayList<HashMap<String, Double>> example = new ArrayList<>();
    //     HashMap<String, Double> test = new HashMap<>();
    //     example.add(test);
    //     test.put("Add C", 1.0);
    //     test.put("Add N", .3);
    //     test.put("Mod Wgt", .05);
    //     test.put("Diff", .4);
    //     test.put("Avg. W", .05);
    //     test.put("Excess", .09);
    //     test.put("Average generations", 5.078);
    //     DataSet data = new DataSet(example);
    //     data.update(test);
    // }
}
