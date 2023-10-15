package GraphVisualization;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.GraphicsGroup;
import edu.macalester.graphics.GraphicsText;

public class GraphNode extends Ellipse {
    public static GraphicsGroup nodes = new GraphicsGroup(); 
    public static final double circle_radius = 40;
    private GraphicsText text;


    /**
     * Draws a circle with circle_radius centered at given (x, y) onto the 
     * given canvas.
     * @param centerX
     * @param centerY
     * @param circle_radius
     * @param canvas
     */
    public GraphNode(int number, double centerX, double centerY, double circle_radius, CanvasWindow canvas) {
        super(0, 0, circle_radius, circle_radius);
        setCenter(centerX, centerY);
        nodes.add(this);
        setFillColor(Color.WHITE);
        text = new GraphicsText(Integer.toString(number), centerX, centerY);
    }

    public static void reset() {
        nodes.removeAll();
    }

    public GraphicsText getText() {
        return text;
    }
}
