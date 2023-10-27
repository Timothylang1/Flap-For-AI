package ConstantFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;

import edu.macalester.graphics.Arc;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.Line;

public class VisualizeConstants {
    public static final int CANVAS_SIZE = 800;
    private CanvasWindow canvas = new CanvasWindow("Constants", CANVAS_SIZE, CANVAS_SIZE);
    private CsvMaker csv = new CsvMaker();
    private ArrayList<DataSet> dataHolders = new ArrayList<>();
    public static ArrayList<String> keySet = new ArrayList<>();

    /*
     * IMPORTANT: DO NOT SWAP THE ORDER OF THESE METHODS
     */
    public VisualizeConstants(ArrayList<ArrayList<HashMap<String, Double>>> data) {
        // Get all the keys
        keySet.addAll(data.get(0).get(0).keySet());
        keySet.remove(DataSet.generation_key);

        // Then create data objects
        data.forEach(x -> dataHolders.add(new DataSet(x)));

        // Setup scales
        double scale_line = 0;
        for (int i = 0; i < keySet.size() / 2; i++) {
            scale_line += Math.cos(i);
        }

        DataSet.scale_line = (CANVAS_SIZE - 40) / 2 * scale_line;
        

        double max_generations = data.stream().mapToDouble(x -> x.stream().mapToDouble(y -> y.get(DataSet.generation_key)).max().getAsDouble()).max().getAsDouble();


        // Setup visual elements
        setup();
    }

    private void setup() {
        // Initial setup of elements
        for (int i = 0; i < keySet.size(); i++) {

            // Color assignment for variable
            Color color = new Color(255 - i * 255 / keySet.size(), i * 255 / keySet.size(), i * 255 / keySet.size());

            // Arc
            Arc arc = new Arc(20, 20, CANVAS_SIZE - 40, CANVAS_SIZE - 40, 360.0 / keySet.size() * i, 360.0 / keySet.size());
            arc.setStrokeWidth(10);
            arc.setStrokeColor(color);
            canvas.add(arc);

            // Text field
            GraphicsText text = new GraphicsText(keySet.get(i));
            text.setFontSize(20);
            double rotation = 90 - 360.0 / keySet.size() * (i + 0.5);
            text.rotateBy((rotation < -90 ? rotation + 180: rotation));
            text.setCenter(
                CANVAS_SIZE / 2 + ((CANVAS_SIZE - 90) / 2 * Math.cos(2 * Math.PI / keySet.size() * (i + 0.5))),
                CANVAS_SIZE / 2 + ((CANVAS_SIZE - 90) / 2 * -Math.sin(2 * Math.PI / keySet.size() * (i + 0.5)))
            );
            canvas.add(text);

            // Set color of vector lines
            for (DataSet holder : dataHolders) {
                holder.vectorSet.get(keySet.get(i)).setStrokeColor(color);
            }
        }
    }

    public static void main(String[] args) {
        ArrayList<ArrayList<HashMap<String, Double>>> data = new ArrayList<>();
        ArrayList<HashMap<String, Double>> example = new ArrayList<>();
        HashMap<String, Double> test = new HashMap<>();
        example.add(test);
        data.add(example);
        test.put("Add C", 1.0);
        test.put("Add N", .3);
        test.put("Mod Wgt", .05);
        test.put("Diff", .4);
        test.put("Avg. W", .05);
        test.put("Excess", .09);
        test.put("Average generations", 5.078);
        new VisualizeConstants(data);

        // CanvasWindow canvas = new CanvasWindow("test", CANVAS_SIZE, CANVAS_SIZE);
        // Arc arc = new Arc(0, 0, CANVAS_SIZE / 2, CANVAS_SIZE / 2, 90, 90);
        // arc.setCenter(CANVAS_SIZE / 2, CANVAS_SIZE / 2);
        // canvas.add(arc);

    }
}
