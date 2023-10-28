package ConstantFinder;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Color;

import edu.macalester.graphics.Arc;
import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;
import edu.macalester.graphics.ui.Button;

public class VisualizeConstants {
    public static final int CANVAS_SIZE = 800;
    public static final int CANVAS_WIDTH = 1000;
    private CanvasWindow canvas = new CanvasWindow("Constants", CANVAS_WIDTH, CANVAS_SIZE);
    private ArrayList<DataSet> dataHolders = new ArrayList<>();
    public static ArrayList<String> keySet;
    private double current_run = 0;
    private GraphicsGroup border = new GraphicsGroup();
    private boolean play;
    private double speed = 0.1; // How fast the canvas updates
    public static double min_generations;

    /*
     * IMPORTANT: DO NOT SWAP THE ORDER OF THESE METHODS
     */
    public VisualizeConstants() {
        ArrayList<ArrayList<HashMap<String, Double>>> data = CsvMaker.read_csv_files();
        
        // Get all the keys
        keySet = new ArrayList<>(data.get(0).get(0).keySet());
        keySet.remove(DataSet.GENERATION_KEY);

        // Add in border
        canvas.add(border);

        // Then create data objects
        data.forEach(x -> dataHolders.add(new DataSet(x, canvas)));

        // Add in the final circles last so that they are above everything else
        dataHolders.forEach(x -> canvas.add(x.final_point));

        // Add in button objects
        for (int i = 0; i < keySet.size(); i++) {
            String variable_name = keySet.get(i);

            // Add in text button
            Button button = new Button(variable_name);
            button.setPosition(CANVAS_SIZE, i * 30 + 10);
            button.onClick(() -> {
                // Set color of vector lines + reset all vector lines
                dataHolders.forEach(dataHolder -> {
                    keySet.forEach(key -> {
                        dataHolder.vectorSet.get(key).setStartPosition(0, 0);
                        dataHolder.vectorSet.get(key).setEndPosition(0, 0);
                    });
                    dataHolder.final_point.setCenter(-20, -20);
                });

                // Remove from keySet if not in, else add it back in
                if (keySet.contains(variable_name)) keySet.remove(variable_name);
                else keySet.add(variable_name);

                // Reset based on new keyset, then update visual
                reset();
                update();
            });

            canvas.add(button);
        };

        // Add in other buttons
        Button reset_button = new Button("Reset");
        reset_button.setPosition(CANVAS_SIZE + 100, 0);
        reset_button.onClick(this::reset);
        canvas.add(reset_button);

        Button pause_button = new Button("Pause");
        pause_button.setPosition(CANVAS_SIZE + 100, 30);
        pause_button.onClick(() -> {play = false;});
        canvas.add(pause_button);

        Button play_button = new Button("Play");
        play_button.setPosition(CANVAS_SIZE + 100, 60);
        play_button.onClick(() -> {play = true;});
        canvas.add(play_button);

        // Setup visual elements + line scale
        reset();
        
        // Setup max generation
        double max_generations = data.stream().mapToDouble(x -> x.stream().mapToDouble(y -> y.get(DataSet.GENERATION_KEY)).max().getAsDouble()).max().getAsDouble();
        min_generations = data.stream().mapToDouble(x -> x.stream().mapToDouble(y -> y.get(DataSet.GENERATION_KEY)).min().getAsDouble()).min().getAsDouble();
        DataSet.scale_circle_color = 255 / (max_generations - min_generations);
        System.out.println(DataSet.scale_circle_color);

        // Update the visual to hold the inital dataset
        update();

        // Animate the canvas
        canvas.animate(() -> {
            if (play) {
                current_run += speed;
                update();
            }
        });
    }

    private void reset() {
        // Clear border
        border.removeAll();

        // Reset current run
        current_run = 0;

        // Initial setup of elements
        for (int i = 0; i < keySet.size(); i++) {

            // Color assignment for variable
            Color color = new Color(255 - i * 255 / keySet.size(), i * 255 / keySet.size(), i * 255 / keySet.size());

            // Arc
            Arc arc = new Arc(20, 20, CANVAS_SIZE - 40, CANVAS_SIZE - 40, 360.0 / keySet.size() * i, 360.0 / keySet.size());
            arc.setStrokeWidth(10);
            arc.setStrokeColor(color);
            border.add(arc);

            // Text field
            String variable_name = keySet.get(i);
            GraphicsText text = new GraphicsText(variable_name);
            text.setFontSize(20);
            double rotation = 90 - 360.0 / keySet.size() * (i + 0.5);
            text.rotateBy((rotation < -90 ? rotation + 180: rotation));
            text.setCenter(
                CANVAS_SIZE / 2 + ((CANVAS_SIZE - 90) / 2 * Math.cos(2 * Math.PI / keySet.size() * (i + 0.5))),
                CANVAS_SIZE / 2 + ((CANVAS_SIZE - 90) / 2 * -Math.sin(2 * Math.PI / keySet.size() * (i + 0.5)))
            );
            border.add(text);

            // Reset the colors of the vector lines
            for (DataSet holder : dataHolders) {
                holder.vectorSet.get(variable_name).setStrokeColor(color);
            }
        }

        // Setup line_scale
        double scale_line = 0;
        double current_angle = (((keySet.size() / 2 + 1) - 2) * 180 - ((keySet.size() - 2) * 180.0 / keySet.size()) * (keySet.size() / 2 - 1)) / 2; // Calculates starting angle
        for (int i = 0; i < keySet.size() / 2; i++) {
            scale_line += Math.abs(Math.cos(current_angle * Math.PI / 180));
            current_angle += (keySet.size() - 2) * 180.0 / keySet.size();
        }
        DataSet.scale_line = ((CANVAS_SIZE - 40) / 2) / scale_line;

        // Stops the animation
        play = false;
    }

    private void update() {
        for (DataSet dataset : dataHolders) {
            dataset.update((int) current_run);
        }
    }

    public static void main(String[] args) {
        new VisualizeConstants();
    }
}
