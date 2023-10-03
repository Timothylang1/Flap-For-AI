package GraphVisualization;

import java.awt.Color;

import edu.macalester.graphics.CanvasWindow;
import edu.macalester.graphics.Ellipse;
import edu.macalester.graphics.Point;

public class GraphNode {
    private Point nodeTopLeft;
    private Point nodeTopRight;
    private Point nodeBottomLeft;
    private Point nodeBottomRight;

    private Point nodeLeft;
    private Point nodeRight;
    private Point nodeBottom;
    private Point nodeTop;

    private Point nodeCenter;
    

    /**
     * Draws a circle with circle_radius centered at given (x, y) onto the 
     * given canvas.
     * @param centerX
     * @param centerY
     * @param circle_radius
     * @param canvas
     */
    public GraphNode(double centerX, double centerY, int circle_radius, CanvasWindow canvas) {
        Ellipse circleDrawing = new Ellipse(0, 0, circle_radius, circle_radius);
        circleDrawing.setCenter(centerX, centerY);
        nodeTopLeft = new Point(circleDrawing.getCenter().getX() - circle_radius/2,
                            circleDrawing.getCenter().getY() - circle_radius/2);
        nodeTopRight = new Point(circleDrawing.getCenter().getX() + circle_radius/2,
                                    circleDrawing.getCenter().getY() - circle_radius/2);
        nodeBottomLeft = new Point(circleDrawing.getCenter().getX() - circle_radius/2,
                                    circleDrawing.getCenter().getY() + circle_radius/2);
        nodeBottomRight = new Point(circleDrawing.getCenter().getX() + circle_radius/2,
                                    circleDrawing.getCenter().getY() + circle_radius/2);

        nodeLeft = new Point(circleDrawing.getCenter().getX() - circle_radius/2, circleDrawing.getCenter().getY());
        nodeRight = new Point(circleDrawing.getCenter().getX() + circle_radius/2, circleDrawing.getCenter().getY());
        nodeTop = new Point(circleDrawing.getCenter().getX(), circleDrawing.getCenter().getY() - circle_radius/2);
        nodeBottom = new Point(circleDrawing.getCenter().getX(), circleDrawing.getCenter().getY() + circle_radius/2);

        nodeCenter = circleDrawing.getCenter();
        canvas.add(circleDrawing);

        // Testing coordinates of circle
        // Ellipse nl = new Ellipse(0, 0, 5, 5);
        // Ellipse nr = new Ellipse(0, 0, 5, 5);
        // Ellipse nt = new Ellipse(0, 0, 5, 5);
        // Ellipse nb = new Ellipse(0, 0, 5, 5);

        // Ellipse nTopL = new Ellipse(0, 0, 5, 5);
        // Ellipse nTopR = new Ellipse(0, 0, 5, 5);
        // Ellipse nBottomL = new Ellipse(0, 0, 5, 5);
        // Ellipse nBottomR = new Ellipse(0, 0, 5, 5);

        // nl.setFillColor(Color.red);
        // nr.setFillColor(Color.blue);
        // nt.setFillColor(Color.green);
        // nb.setFillColor(Color.pink);

        // nTopL.setFillColor(Color.MAGENTA);
        // nTopR.setFillColor(Color.cyan);
        // nBottomL.setFillColor(Color.yellow);
        // nBottomR.setFillColor(Color.lightGray);

        // nl.setCenter(nodeLeft);
        // nr.setCenter(nodeRight);
        // nt.setCenter(nodeTop);
        // nb.setCenter(nodeBottom);

        // nTopL.setCenter(nodeTopLeft);
        // nTopR.setCenter(nodeTopRight);
        // nBottomL.setCenter(nodeBottomLeft);
        // nBottomR.setCenter(nodeBottomRight);

        // canvas.add(nl);
        // canvas.add(nr);
        // canvas.add(nt);
        // canvas.add(nb);

        // canvas.add(nTopL);
        // canvas.add(nTopR);
        // canvas.add(nBottomL);
        // canvas.add(nBottomR);
    }

    //     private Point nodeTopLeft;
    // private Point nodeTopRight;
    // private Point nodeBottomLeft;
    // private Point nodeBottomRight;

    // private Point nodeLeft;
    // private Point nodeRight;
    // private Point nodeBottom;
    // private Point nodeTop;
    public Point getTopLeft() {
        return nodeTopLeft;
    }

    public Point getTopRight() {
        return nodeTopRight;
    }

    public Point getBottomLeft() {
        return nodeBottomLeft;
    }

    public Point getBottomRight() {
        return nodeBottomRight;
    }

    public Point getLeft() {
        return nodeLeft;
    }

    public Point getRight() {
        return nodeRight;
    }

    public Point getBottom() {
        return nodeBottom;
    }

    public Point getTop() {
        return nodeTop;
    }

    public Point getCenter() {
        return nodeCenter;
    }

    
}
