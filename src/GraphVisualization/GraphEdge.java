package GraphVisualization;

import java.awt.Color;

import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.Line;
import edu.macalester.graphics.Point;

public class GraphEdge extends Line{
    public static GraphicsGroup edges = new GraphicsGroup();
    public static final double MAX_WIDTH = 10;
    public static final double MAX_OPACITY = 1;
    public static double SCALE_WIDTH = 0;
    public static double SCALE_OPACITY = 0;


    public GraphEdge(Point startingPoint, Point endPoint, double weight) {
        super(startingPoint, endPoint);
        this.setStrokeWidth(Math.abs(SCALE_WIDTH * weight));
        if (weight < 0) {
            this.setStrokeColor(new Color(0, 0, (float) 1.0, (float) Math.abs(SCALE_OPACITY * weight)));
        } else {
            setStrokeColor(new Color((float) 1.0, 0, 0, (float) (SCALE_OPACITY * weight)));
        }
        edges.add(this);        
    }

    public static void reset() {
        edges.removeAll();
    }
}
