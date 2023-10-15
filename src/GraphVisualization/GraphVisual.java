package GraphVisualization;

import edu.macalester.graphics.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Queue;
import java.util.ArrayDeque;

import Game.PipeHandler;
import NEAT.Neuron;
import NEAT.Gene;
import NEAT.Neural_Constants;


public class GraphVisual {
    private CanvasWindow canvas;
    private ArrayList<GraphNode> allNodes;
    public int totalNodes;
    private HashMap<Integer, ArrayList<Gene>> genes;

    public GraphVisual(CanvasWindow canvas) {
        // Create a graph visual using the neurons in a network.
        // Each neuron has a bunch of neurons it's connected to
        this.canvas = canvas;
        allNodes = new ArrayList<GraphNode>();

        // double middleNodeX = canvas.getWidth()/2 - 2*circle_radius;
        // double middleNodeY = canvas.getHeight()/2;

        // Create the input and output nodes

        canvas.add(GraphEdge.edges);
        canvas.add(GraphNode.nodes);
        // // Create the middle nodes
        // for (int i = 0; i < totalNodes - numInputNodes - numOutputNodes; i++) {
        //     allNodes.add(new GraphNode(middleNodeX, middleNodeY, circle_radius, canvas));
        //     middleNodeY += circle_radius;
        //     middleNodeX += circle_radius;
        // }
    }

    /**
     * Visually connects two neurons in the network.
     * @param n1
     * @param n2
     */
    public void connect_two_nodes(GraphNode node1, GraphNode node2, double weight) {
        new GraphEdge(node1.getCenter(), node2.getCenter(), weight);
    }

    // /**
    //  * Uses Neural_Constants and sets up the initial layout 
    //  * of input neurons and output neurons
    //  * @param n
    //  */
    // private void setup() {
    //     double canvasHeightSegmentInput = canvas.getHeight()/(Neural_Constants.NUM_OF_INPUTS + 1);
    //     double canvasHeightSegmentOutput = canvas.getHeight()/(Neural_Constants.NUM_OF_OUTPUTS + 1);

    //     double x = GraphNode.circle_radius;
    //     double y = canvasHeightSegmentInput;

    //     for (int inputNodeCount = 0; inputNodeCount < Neural_Constants.NUM_OF_INPUTS; inputNodeCount++) {
    //         GraphNode inputNode = new GraphNode(x, y, GraphNode.circle_radius, canvas);
    //         allNodes.add(inputNode);
    //         y += canvasHeightSegmentInput;
    //     }

    //     x = canvas.getWidth() - GraphNode.circle_radius;
    //     y = canvasHeightSegmentOutput;
    //     for (int outputNodeCount = 0; outputNodeCount < Neural_Constants.NUM_OF_OUTPUTS; outputNodeCount++) {
    //         GraphNode outputNode = new GraphNode(x, y, GraphNode.circle_radius, canvas);
    //         allNodes.add(outputNode);
    //         y += canvasHeightSegmentOutput;
    //     }
    // }

    /**
     * Draws the connections between 2 nodes
     */
    public void connect_nodes() {
        setLineScale();

        for (int key : genes.keySet()) {
            System.out.println("This is my key: " + key);
            for (Gene gene : genes.get(key)) {
                new GraphEdge(allNodes.get(key).getCenter(), allNodes.get(gene.END_NODE).getCenter(), gene.weight * GraphEdge.SCALE);
            }
        }
    }

    public void update(HashMap<Integer, ArrayList<Gene>> genes) {
        this.genes = genes;
        allNodes.clear();

        GraphEdge.reset();
        GraphNode.reset();

        setup();
        connect_nodes();
    }

    private void layering_nodes() {
        ArrayDeque<Integer> nodeNumbers = new ArrayDeque<Integer>();
        HashMap<Integer, Integer> nodeToLayer= new HashMap<>();

        for (int i = 0; i < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; i++) {
            nodeToLayer.put(i, 0);
        }

        // Set up Queue
        for (int i = 0; i < Neural_Constants.NUM_OF_INPUTS; i++) {
            nodeNumbers.add(i);
        }

        // Check to see what a node is connected to
            // If the node is connected to something in its layer
                // Move it to the curr + 1 layer
            // If the node is connected to something in the previous layer
                // Move it to curr + 1 layer
        while (true) {
            nodeNumbers.pop();
        }




        // while(true) {
            // int[] lastLayer = layers.get(layers.size()-1).stream().mapToInt(x -> x).toArray();
            // HashSet<Integer> nextLayer = new HashSet<>();
            
            // nodeNumbers.pop();
            

            // for (int nodeIndex = 0; nodeIndex < lastLayer.length; nodeIndex++) {
            //     nextLayer.addAll(genes.get(nodeIndex).stream().map(x -> {
            //         layers.forEach(layer -> {
            //             layer.removeIf(y -> y == x.END_NODE);
            //         });
            //         return x.END_NODE;
            //     }).toList());
            // }

            // if (nextLayer.isEmpty()) break;
        // }
    }

    private void setLineScale() {
        double maxWeight = 0;

        for (Integer key : genes.keySet()) {
            maxWeight = Math.max(genes.get(key).stream().mapToDouble(x -> Math.abs(x.weight)).max().getAsDouble(), maxWeight);
        }

        GraphEdge.SCALE = GraphEdge.MAX_WIDTH/maxWeight;
    }

    public static void main(String[] args) {
        HashMap<Integer, ArrayList<Gene>> genes = new HashMap<Integer, ArrayList<Gene>>();
        // genes.put(0, new ArrayList<Gene>(Arrays.asList(new Gene(8, 1))));

        HashMap<Integer, ArrayList<Gene>> genes1 = new HashMap<Integer, ArrayList<Gene>>();
        genes1.put(0, new ArrayList<Gene>(Arrays.asList(new Gene(10, 50))));
        genes1.put(1, new ArrayList<Gene>(Arrays.asList(new Gene(13, -10))));
        genes1.put(2, new ArrayList<Gene>(Arrays.asList(new Gene(9, -10))));
        genes1.put(10, new ArrayList<Gene>(Arrays.asList(new Gene(11, 0))));
        genes1.put(11, new ArrayList<Gene>(Arrays.asList(new Gene(12, -10))));
        genes1.put(12, new ArrayList<Gene>(Arrays.asList(new Gene(13, -10))));
        genes1.put(13, new ArrayList<Gene>(Arrays.asList(new Gene(14, -10))));
        genes1.put(14, new ArrayList<Gene>(Arrays.asList(new Gene(8, -10))));

        CanvasWindow canvas = new CanvasWindow("Testing Graph", GraphVisual_Constants.CANVAS_WIDTH, GraphVisual_Constants.CANVAS_HEIGHT);
        

        GraphVisual gv = new GraphVisual(canvas);
        // gv.update(genes);
        gv.update(genes1);
    }
}