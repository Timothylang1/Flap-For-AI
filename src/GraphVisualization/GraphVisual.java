package GraphVisualization;

import edu.macalester.graphics.*;

import java.util.ArrayList;
import java.util.HashMap;

import Game.PipeHandler;
import NEAT.Neuron;
import NEAT.Gene;
import NEAT.Neural_Constants;


public class GraphVisual {
    public final CanvasWindow canvas = new CanvasWindow("Neural Network", 500, 500);
    public final int circle_radius = 40;
    private ArrayList<GraphNode> allNodes;
    public int totalNodes;
    private HashMap<Integer, ArrayList<Gene>> genes; = new HashMap<>(); // NodeNumber --> List of connections

    public GraphVisual(ArrayList<Gene> geneList, int numInputNodes, int numOutputNodes) {
        // Create a graph visual using the neurons in a network.
        // Each neuron has a bunch of neurons it's connected to
        // ArrayList<Neuron> neurons = network.neurons;

        allNodes = new ArrayList<GraphNode>();
        connectionsMap = new HashMap<Integer, ArrayList<Integer>>();

        totalNodes = 0;
        for (Gene g : geneList) {
            if (g.INITIAL_NODE > totalNodes) {
                totalNodes = g.INITIAL_NODE;
            }
        }

        totalNodes += 1; // Adjust since our node numbers start at 0
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

        connect_nodes(geneList, numInputNodes, numOutputNodes);
        store_connections(geneList);

        System.out.println(connectionsMap);
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
     * @param geneList
     * @param numInputNodes
     * @param numOutputNodes
     */
    public void connect_nodes(ArrayList<Gene> geneList, int numInputNodes, int numOutputNodes) {
        for (Gene g : geneList) {
            connect_two_nodes(allNodes.get(g.INITIAL_NODE), allNodes.get(g.END_NODE));
        }
    }

    /**
     * Stores what nodes that a node is connected to 
     * @param geneList
     */
    private void store_connections(ArrayList<Gene> geneList) {
        for (Gene g : geneList) {
            if (connectionsMap.containsKey(g.INITIAL_NODE)) {
                connectionsMap.get(g.INITIAL_NODE).add(g.END_NODE);
            } else {
                connectionsMap.put(g.INITIAL_NODE, new ArrayList<Integer>());
                connectionsMap.get(g.INITIAL_NODE).add(g.END_NODE);
            }
        }
    }

    private void layering_nodes(ArrayList<Gene> geneList, int numInputNodes) {
        ArrayList<Integer> layer = new ArrayList<Integer>();

        // Start off with input layer
        for (int i = 0; i < numInputNodes; i++) {
            layer.add(i);
        }

        ArrayList<Integer> layer2 = new ArrayList<Integer>();
        for (int i : layer) {
            for (int j : connectionsMap.get(i)) {
                // If node is in layer already, don't add it
                if (!layer2.contains(j)) {
                    layer2.add(j);
                }
            }
        }

        // Check if a node is connected to another node within the layer
        for (int i : layer2) {
            connectionsMap.get(i);
        }
    }


    public static void main(String[] args) {

     
        
        
        
        
        // GraphNode node1 = vis.getInputNodes().get(0);
        // GraphNode node5 = vis.getOutputNodes().get(1);

        


        // vis.connect_two_nodes(node1, node5);

        // Use a list of genes to make visual

        // GraphNode testNode = new GraphNode(40, 40, vis.circle_radius, vis.canvas);
        // GraphNode testNode1 = new GraphNode(80, 40, vis.circle_radius, vis.canvas);
        // vis.add_graph_node(testNode);
        // vis.add_graph_node(testNode1);


        
    }
}