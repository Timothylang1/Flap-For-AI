package GraphVisualization;

import edu.macalester.graphics.*;

import java.util.ArrayList;
import java.util.HashMap;

import NEAT.Gene;
import NEAT.Neural_Constants;


public class GraphVisual {
    private HashMap<Integer, GraphNode> allNodes;
    public int totalNodes;
    private HashMap<Integer, ArrayList<Gene>> genes;
    private final InfoText info;
    CanvasWindow canvas;

    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 800;    

    public GraphVisual(CanvasWindow canvas) {
        // Adds the GraphicsGroups GraphEdge and GraphNode onto the canvas
        // Sets up the list to hold all nodes
        this.canvas = canvas;
        info = new InfoText(canvas);
        info.setCenter(canvas.getWidth() - info.getWidth(), info.getHeight() / 2 + 5);
        allNodes = new HashMap<Integer, GraphNode>();

        canvas.add(GraphEdge.edges);
        canvas.add(GraphNode.nodes);
        canvas.add(info);

    }

    /**
     * Uses constants and sets up the initial layout 
     * of input neurons and output neurons
     * @param n
     */
    private void setup() {
        // Figure out how many layers there are
        int maxLayer = 0;
        HashMap<Integer, Integer> layerOrders = layering_nodes();

        for (Integer i : layerOrders.keySet()) {
            maxLayer = Math.max(maxLayer, layerOrders.get(i));
        }
        maxLayer += 1; // layers were 0-indexed

        // Figure out what nodes a layer contains
        HashMap<Integer, ArrayList<Integer>> layerNodes = new HashMap<>();
        for (int i = 0; i < maxLayer; i++) {
            layerNodes.put(i, new ArrayList<Integer>());
        }
        for (Integer nodeNumber : layerOrders.keySet()) {
            layerNodes.get(layerOrders.get(nodeNumber)).add(nodeNumber);
        }


        double canvasWidthSegment = canvas.getWidth()/(maxLayer*2);
        double x = canvasWidthSegment;
        for (int currLayer = 0; currLayer < maxLayer; currLayer++) {
            double canvasHeightSegment = canvas.getHeight()/(layerNodes.get(currLayer).size() + 1);

            // Put the nodes onto the canvas
            double y = canvasHeightSegment;
            for (int nodeNumber : layerNodes.get(currLayer)) {
                GraphNode node = new GraphNode(nodeNumber, x, y, GraphNode.circle_radius, canvas);
                allNodes.put(nodeNumber, node);
                // canvas.add(node.getText());
                y += canvasHeightSegment;
            }
            x += canvasWidthSegment * 2;
        }
    }

    /**
     * Draws the connections between 2 nodes
     */
    public void connect_nodes() {
        setLineScale();

        for (int key : genes.keySet()) {
            // System.out.println("This is my key: " + key);
            for (Gene gene : genes.get(key)) {
                new GraphEdge(allNodes.get(key).getCenter(), allNodes.get(gene.END_NODE).getCenter(), gene.weight);
            }
        }
    }

    public void reset(HashMap<Integer, ArrayList<Gene>> genes, int species, int score) {
        this.genes = genes;
        allNodes.clear();

        GraphEdge.reset();
        GraphNode.reset();

        setup();

        connect_nodes();

        info.update(species, score);
    }

    public HashMap<Integer, Integer> layering_nodes() {
        // Is a queue structure
        ArrayList<Integer> nodeNumbers = new ArrayList<Integer>();
        HashMap<Integer, Integer> nodeToLayer= new HashMap<>();

        // Adding in Input Layer
        for (int i = 0; i < Neural_Constants.NUM_OF_INPUTS; i++) {
            nodeToLayer.put(i, 0);
        }

        // Putting output nodes in layer 1
        for (int i = Neural_Constants.NUM_OF_INPUTS; i < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; i++) {
            nodeToLayer.put(i, 1);
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
    
        while (!nodeNumbers.isEmpty()) {
            Integer currentNode = nodeNumbers.remove(0);
            // System.out.println("This is the currentNode: " + currentNode);
            if (genes.containsKey(currentNode)) {
                genes.get(currentNode).forEach(x -> {
                    nodeToLayer.put(x.END_NODE, nodeToLayer.get(currentNode) + 1);
                    nodeNumbers.add(x.END_NODE);
                });
            }
        }

        int maxLayer = 1;
        for (int i = Neural_Constants.NUM_OF_INPUTS; i < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; i++) {
            maxLayer = Math.max(maxLayer, nodeToLayer.get(i));
        }

        for (int i = Neural_Constants.NUM_OF_INPUTS; i < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; i++) {
            nodeToLayer.put(i, maxLayer);
        }

        // System.out.println(nodeToLayer);
        return nodeToLayer;
    }

    private void setLineScale() {
        double maxWeight = 0;

        for (Integer key : genes.keySet()) {
            maxWeight = Math.max(genes.get(key).stream().mapToDouble(x -> Math.abs(x.weight)).max().getAsDouble(), maxWeight);
        }

        GraphEdge.SCALE_WIDTH = GraphEdge.MAX_WIDTH/maxWeight;
        GraphEdge.SCALE_OPACITY = GraphEdge.MAX_OPACITY/maxWeight;
    }
}