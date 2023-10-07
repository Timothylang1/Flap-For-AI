package GraphVisualization;

import edu.macalester.graphics.*;

import java.awt.Color;
import java.util.ArrayList;

import Game.PipeHandler;
import NEAT.Neuron;
import NEAT.Gene;
import NEAT.Neural_Constants;


public class GraphVisual {
    public final CanvasWindow canvas = new CanvasWindow("Neural Network", 500, 500);
    public final int circle_radius = 40;
    private ArrayList<GraphNode> allNodes;
    public int totalNodes;

    public GraphVisual(ArrayList<Gene> geneList, int numInputNodes, int numOutputNodes) {
        // Create a graph visual using the neurons in a network.
        // Each neuron has a bunch of neurons it's connected to
        // ArrayList<Neuron> neurons = network.neurons;

        allNodes = new ArrayList<GraphNode>();
        setup(numInputNodes, numOutputNodes);

        totalNodes = 0;
        for (Gene g : geneList) {
            if (g.INITIAL_NODE > totalNodes) {
                totalNodes = g.INITIAL_NODE;
            }
        }

        totalNodes += 1; // Adjust since our node numbers start at 0
        double middleNodeX = canvas.getWidth()/2;
        double middleNodeY = canvas.getHeight()/2;
        // Create the middle nodes
        for (int i = 0; i < totalNodes - numInputNodes - numOutputNodes; i++) {
            allNodes.add(new GraphNode(middleNodeX, middleNodeY, circle_radius, canvas));
            middleNodeY += circle_radius;
        }

        drawNodes(geneList, numInputNodes, numOutputNodes);
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

    public void drawNodes(ArrayList<Gene> geneList, int numInputNodes, int numOutputNodes) {
        for (Gene g : geneList) {

            connect_two_nodes(allNodes.get(g.INITIAL_NODE), allNodes.get(g.END_NODE));
            // if (g.INITIAL_NODE >= numInputNodes + numOutputNodes) {
            //     int middleNodeIndex2 = g.INITIAL_NODE - numInputNodes - numOutputNodes;
            // } else if (g.INITIAL_NODE < numInputNodes) {
            //     int inputNodeIndex2 = g.INITIAL_NODE;
            // } else if (g.INITIAL_NODE >= numInputNodes && g.INITIAL_NODE < numInputNodes + numOutputNodes) {
            //     int outputNodeIndex2 = g.INITIAL_NODE - 1;
            // }
        }


        // Split the nodes into layers

        // If something is in a layer, then put in the layerList
        // Node.layer?
        // Can get layer from

        // For each layer, add the position of the node on the screen to the node poses arraylist

        // Draw the connections
        // Draw the nodes on top of the connections
    }

    public static void main(String[] args) {
        Gene g1 = new Gene(0, 3, 0.5);
        Gene g2 = new Gene(0, 3, 0.1);
        ArrayList<Gene> genes = new ArrayList<Gene>();
        genes.add(g1);
        genes.add(g2);
        
        
        GraphVisual vis = new GraphVisual(genes, Neural_Constants.NUM_OF_INPUTS, Neural_Constants.NUM_OF_OUTPUTS);


     
        
        
        
        
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