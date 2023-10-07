package GraphVisualization;

import edu.macalester.graphics.*;

import java.awt.Color;
import java.util.ArrayList;

import Game.PipeHandler;
import NEAT.Neuron;
import NEAT_FROM_VIDEO_CODE.Neural_Constants;


public class GraphVisual {
    public final CanvasWindow canvas = new CanvasWindow("Neural Network", 500, 500);
    public final int circle_radius = 40;
    private ArrayList<GraphNode> inputNodes;
    private ArrayList<GraphNode> outputNodes;
    private ArrayList<GraphNode> middleNodes;

    public GraphVisual() {
        // Create a graph visual using the neurons in a network.
        // Each neuron has a bunch of neurons it's connected to
        // ArrayList<Neuron> neurons = network.neurons;

        inputNodes = new ArrayList<GraphNode>();
        outputNodes = new ArrayList<GraphNode>();
        middleNodes = new ArrayList<GraphNode>();
    }

    /**
     * Visually connects two neurons in the network.
     * @param n1
     * @param n2
     */
    public void connect_two_nodes(GraphNode node1, GraphNode node2) {
        double neuron1_X = node1.getRight().getX();
        double neuron2_X = node2.getLeft().getX();

        Point neuron1_coord = new Point(neuron1_X, node1.getCenter().getY());
        Point neuron2_coord = new Point(neuron2_X, node2.getCenter().getY());

        Line testingLine = new Line(neuron1_coord, neuron2_coord);
        canvas.add(testingLine);
    }

    /**
     * Uses Neural_Constants and sets up the initial layout 
     * of input neurons and output neurons
     * @param n
     */
    private void setup(int numInputNodes, int numOutputNodes) {
        double x = circle_radius/2;
        double y = circle_radius/2;
        for (int inputNodeCount = 0; inputNodeCount < numInputNodes; inputNodeCount++) {
            GraphNode inputNode = new GraphNode(x, y, circle_radius, canvas);
            inputNodes.add(inputNode);
            y += circle_radius;
        }

        x = canvas.getWidth() - circle_radius/2;
        y = circle_radius/2;
        for (int outputNodeCount = 0; outputNodeCount < numOutputNodes; outputNodeCount++) {
            GraphNode outputNode = new GraphNode(x, y, circle_radius, canvas);
            outputNodes.add(outputNode);
            y += circle_radius;
        }
    }

    // /**
    //  * Checks if two circle are overlapping.
    //  * @return boolean
    //  */
    // public boolean is_overlapping() {
    //     for (GraphNode n : allNodes) {
    //         if (
    //             (canvas.getElementAt(n.getTopLeft()) != null) ||
    //             (canvas.getElementAt(n.getTop()) != null) ||
    //             (canvas.getElementAt(n.getTopRight()) != null) ||
    //             (canvas.getElementAt(n.getLeft()) != null) ||
    //             (canvas.getElementAt(n.getRight()) != null) ||
    //             (canvas.getElementAt(n.getBottomLeft()) != null) ||
    //             (canvas.getElementAt(n.getBottom()) != null) ||
    //             (canvas.getElementAt(n.getBottomRight()) != null)
    //         ) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    public ArrayList<GraphNode> getInputNodes() {
        return inputNodes;
    }

    public ArrayList<GraphNode> getOutputNodes() {
        return outputNodes;
    }

    // public void drawNode() {
    //     if 
    // }

    public static void main(String[] args) {
        GraphVisual vis = new GraphVisual();
        vis.setup(Neural_Constants.NUM_OF_INPUTS, Neural_Constants.NUM_OF_OUTPUTS);
        GraphNode node1 = vis.getInputNodes().get(0);
        GraphNode node5 = vis.getOutputNodes().get(1);


        vis.connect_two_nodes(node1, node5);

        // Use a list of genes to make visual

        // GraphNode testNode = new GraphNode(40, 40, vis.circle_radius, vis.canvas);
        // GraphNode testNode1 = new GraphNode(80, 40, vis.circle_radius, vis.canvas);
        // vis.add_graph_node(testNode);
        // vis.add_graph_node(testNode1);

        // System.out.println("Overlapping Status: " + vis.is_overlapping());
        // Point testPoint = testNode.getTop();
        // Ellipse circle = new Ellipse(0, 0, 5, 5);
        // circle.setCenter(testPoint);
        // circle.setFillColor(Color.red);
        // vis.canvas.add(circle);
        // System.out.println(vis.canvas.getElementAt(testNode.getTop().getX(), testNode.getTop().getY()));
        // Ellipse testingCircle = new Ellipse(20, 20, vis.circle_radius, vis.circle_radius);
        // Ellipse testingCircle2 = new Ellipse(250, 60, vis.circle_radius, vis.circle_radius);
        // Ellipse testingCircle3 = new Ellipse(350, 100, vis.circle_radius, vis.circle_radius);
        
        // vis.canvas.add(testingCircle);
        // vis.canvas.add(testingCircle2);
        // vis.canvas.add(testingCircle3);
        // vis.connect_two_nodes(testingCircle, testingCircle2);
        // vis.connect_two_nodes(testingCircle2, testingCircle3);


        
    }
}