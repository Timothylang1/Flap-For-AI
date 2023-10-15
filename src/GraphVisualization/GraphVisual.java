package GraphVisualization;

import edu.macalester.graphics.*;

import java.util.ArrayList;
import java.util.HashMap;

import Game.PipeHandler;
import NEAT.Neuron;
import NEAT.Gene;
import NEAT.Neural_Constants;


public class GraphVisual {
    private CanvasWindow canvas;
    public final int circle_radius = 40;
    private ArrayList<GraphNode> allNodes;
    public int totalNodes;
    private HashMap<Integer, ArrayList<Gene>> genes; // NodeNumber --> List of connections

    public GraphVisual(CanvasWindow canvas, HashMap<Integer, ArrayList<Gene>> genes, int numInputNodes, int numOutputNodes) {
        // Create a graph visual using the neurons in a network.
        // Each neuron has a bunch of neurons it's connected to
        // ArrayList<Neuron> neurons = network.neurons;
        this.canvas = canvas;
        allNodes = new ArrayList<GraphNode>();
        this.genes = genes;

        totalNodes = genes.size();

        double middleNodeX = canvas.getWidth()/2 - 2*circle_radius;
        double middleNodeY = canvas.getHeight()/2;

        // Create the input and output nodes
        setup(numInputNodes, numOutputNodes);

        // Create the middle nodes
        for (int i = 0; i < totalNodes - numInputNodes - numOutputNodes; i++) {
            allNodes.add(new GraphNode(middleNodeX, middleNodeY, circle_radius, canvas));
            middleNodeY += circle_radius;
            middleNodeX += circle_radius;
        }

        connect_nodes();


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
        double y = circle_radius/2 + circle_radius*(numInputNodes/2);

        for (int inputNodeCount = 0; inputNodeCount < numInputNodes; inputNodeCount++) {
            GraphNode inputNode = new GraphNode(x, y, circle_radius, canvas);
            allNodes.add(inputNode);
            y += circle_radius;
        }

        x = canvas.getWidth() - circle_radius/2;
        y = circle_radius/2;
        for (int outputNodeCount = 0; outputNodeCount < numOutputNodes; outputNodeCount++) {
            GraphNode outputNode = new GraphNode(x, y, circle_radius, canvas);
            allNodes.add(outputNode);
            y += circle_radius;
        }
    }

    /**
     * Draws the connections between 2 nodes
     */
    public void connect_nodes() {
        for (int key : genes.keySet()) {
            System.out.println("This is my key: " + key);
            for (Gene gene : genes.get(key)) {
                connect_two_nodes(allNodes.get(key), allNodes.get(gene.END_NODE));
            }
        }
    }

    // private void layering_nodes(ArrayList<Gene> geneList, int numInputNodes) {
    //     ArrayList<Integer> layer = new ArrayList<Integer>();

    //     // Start off with input layer
    //     for (int i = 0; i < numInputNodes; i++) {
    //         layer.add(i);
    //     }

    //     ArrayList<Integer> layer2 = new ArrayList<Integer>();
    //     for (int i : layer) {
    //         for (int j : connectionsMap.get(i)) {
    //             // If node is in layer already, don't add it
    //             if (!layer2.contains(j)) {
    //                 layer2.add(j);
    //             }
    //         }
    //     }

    //     // Check if a node is connected to another node within the layer
    //     for (int i : layer2) {
    //         connectionsMap.get(i);
    //     }
    // }


    public static void main(String[] args) {



        
    }
}