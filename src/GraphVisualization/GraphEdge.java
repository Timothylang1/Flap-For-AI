package GraphVisualization;

import java.awt.Color;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class GraphEdge extends Line{
    public static GraphicsGroup edges = new GraphicsGroup();
    public static final double MAX_WIDTH = 10;
    public static double SCALE = 0;


    public GraphEdge(Point startingPoint, Point endPoint, double weight) {
        super(startingPoint, endPoint);
        this.setStrokeWidth(Math.abs(weight));
        if (weight < 0) {
            this.setStrokeColor(Color.BLUE);
        } else {
            this.setStrokeColor(Color.RED);
        }
        edges.add(this);
    }

    public static void reset() {
        edges.removeAll();
    }
}
