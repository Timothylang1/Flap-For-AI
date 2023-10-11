package GraphVisualization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import GraphVisualization.GraphVisual;

/*
 * Contains list of all the changes made as well as methods to determine whether a change was made previously or not
 */
public class VisualTester {
    
    private static int neuron_number = Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; // Global number of all new neurons we've seen
    private static HashMap<Gene, Integer> new_node_connections = new HashMap<>(); // KEY: Gene connection that was broken, VALUE: the number of the new node that goes in place of the broken connection
    private static final Random RAND = new Random();


    /*
     * Creates random connection between two unconnected nodes
     */
    public static void addRandomConnection(ArrayList<Gene> enabled_genes, ArrayList<Neuron> neural_nodes) {
        HashMap<Integer, ArrayList<Integer>> possible_connections = checkForPossibleConnections(enabled_genes, neural_nodes);
        if (possible_connections.size() != 0) { // As long as we can add a node
            // Pick a random starting node, and then a random ending node from the starting node list
            int start_identifier = new ArrayList<>(possible_connections.keySet()).get(RAND.nextInt(possible_connections.size()));
            int end_indentifier = possible_connections.get(start_identifier).get(RAND.nextInt(possible_connections.get(start_identifier).size()));
            addConnection(enabled_genes, neural_nodes, start_identifier, end_indentifier, RAND.nextDouble(-Neural_Constants.DIFFERENTIAL / 2, Neural_Constants.DIFFERENTIAL / 2)); // Give an initial random weight
        }
    }

    /*
     * Breaks a connection and adds a new node, then reconnects around that node following the weights and end/start nodes of the broken connection
     */
    public static void addRandomNode(ArrayList<Gene> enabled_genes, ArrayList<Neuron> neural_nodes) {
        // First, pick a random connection to severe and deactivate it
        Gene to_break = enabled_genes.remove(RAND.nextInt(enabled_genes.size()));

        // Then figure out if that connection existed before, and if it did, assign the node the correct number
        boolean node_already_exists = false;
        int node_number = neuron_number; // In case it's a new mutation with a new neuron, we assign it a new neuron_number
        for (Gene gene : new_node_connections.keySet()) {
            if (to_break.equals(gene)) {
                node_number = new_node_connections.get(gene);
                node_already_exists = true;
                break;
            }
        }

        // If this connection hasn't been broken before, then it is a new mutation we haven't seen, so we record it
        if (!node_already_exists) {
            new_node_connections.put(to_break, node_number); // Add the gene that broke and the node number that was created so we store this new mutation
            neuron_number += 1; // Increase the node_number so the next new "unique" node we add will be different than all other nodes
        }

        // Then create new node and choose a random location to insert into the middle section of the list of nodes between the original initial node and the end node of the connection
        int start_index;
        int end_index;
        for (start_index = 0; start_index < neural_nodes.size(); start_index++) {
            if (neural_nodes.get(start_index).IDENTIFIER == to_break.INITIAL_NODE) {
                start_index = Math.max(start_index + 1, Neural_Constants.NUM_OF_INPUTS); // If the starting index is a initial node, we can't place the new node inbetween the inital nodes, so we limit the min the start_index can be
                break;
            };
        }
        for (end_index = start_index; end_index < neural_nodes.size(); end_index++) {
            if (neural_nodes.get(end_index).IDENTIFIER == to_break.END_NODE) {
                end_index = Math.min(end_index, neural_nodes.size() - Neural_Constants.NUM_OF_OUTPUTS) + 1; // We can't add the node after the end nodes, so we limit the max the end_index can be
                break;
            };
        }
        neural_nodes.add(RAND.nextInt(start_index, end_index), new Neuron(node_number, Neural_Constants.MIDDLE_FUNCTION));

        // Create connections to connect in new neuron (IMPORTANT: DO NOT SWAP THE ORDER OF THESE BECAUSE OF HOW THEY ARE PLACED IN THE GENE SEQUENCE)
        // The weights are designed to maintain the original weight
        addConnection(enabled_genes, neural_nodes, node_number, to_break.END_NODE, to_break.weight);
        addConnection(enabled_genes, neural_nodes, to_break.INITIAL_NODE, node_number, 1);
    }

    /*
     * Adds new gene in correct location of neural_genes so that its output is calculated before we need it
     */
    public static void addConnection(ArrayList<Gene> enabled_genes, ArrayList<Neuron> neural_nodes, int start_identifier, int end_indentifier, double weight) {
        // Create a new gene and add it into the list of enabled_genes
        enabled_genes.add(new Gene(start_identifier, end_indentifier, weight));

        // Then sort the genes based on the order of the neural_nodes so that the graph flows the correct direction and each node gets all its inputs before it needs them
        HashMap<Integer, List<Gene>> to_sort = new HashMap<>();
        neural_nodes.forEach(neuron -> {
            to_sort.put(neuron.IDENTIFIER, enabled_genes.stream().filter(x -> x.INITIAL_NODE == neuron.IDENTIFIER).toList());
        });
        enabled_genes.clear();
        neural_nodes.forEach(neuron -> {
            enabled_genes.addAll(to_sort.get(neuron.IDENTIFIER));
        });
    }

    /*
     * Returns a hashmap of all possible connections still available
     * RULES: initial nodes can't connect to each other, and output nodes can't send their outputs to other nodes. Also,
     * the connections have to flow in the direction of the neural_network list to avoid cycles
     * KEY: IDENTIFIER of start_neuron
     * VALUES: IDENTIFIER of all non-connected neurons to start
     */
    public static HashMap<Integer, ArrayList<Integer>> checkForPossibleConnections(ArrayList<Gene> enabled_genes, ArrayList<Neuron> neural_nodes) {
        HashMap<Integer, ArrayList<Integer>> possible_connections = new HashMap<>();

        // For each neuron, we can only connect to neurons to the right of that neuron in the list (to maintain directional flow and have no cycles)
        // Also, we can't connect the output neurons to other neurons, so we don't include them in possible connections start_neuron
        for (int neuron_start_index = 0; neuron_start_index < neural_nodes.size() - Neural_Constants.NUM_OF_OUTPUTS; neuron_start_index++) {
            int start_neuron = neural_nodes.get(neuron_start_index).IDENTIFIER;

            // Now we get a list of all the IDENTIFIERS of the neurons this start_neuron is connected to along it's path to an output node
            ArrayList<Integer> already_connected = new ArrayList<>();
            ArrayList<Integer> queue = new ArrayList<>();
            queue.add(start_neuron);
            while (!queue.isEmpty()) {
                int to_check = queue.remove(0);
                for (Gene gene : enabled_genes) {
                    if (gene.INITIAL_NODE == to_check) {
                        already_connected.add(gene.END_NODE);
                        queue.add(gene.END_NODE);
                    }
                }
            }

            // Now we get the list of neurons it isn't connected to
            ArrayList<Integer> to_connect = new ArrayList<>();

            // SPECIAL CASE: if we have an input neuron, we can't connect to another input neuron, so we start looking for neurons to connect to past the input neurons
            int end_of_connection_index = Math.max(neuron_start_index + 1, Neural_Constants.NUM_OF_INPUTS);

            // Now we check all of the possible connections by checking if that connection already exists in the geneomes
            for (int neuron_end_index = end_of_connection_index; neuron_end_index < neural_nodes.size(); neuron_end_index++) {
                Neuron end_neuron = neural_nodes.get(neuron_end_index);
                if (!already_connected.contains(end_neuron.IDENTIFIER)) {
                    to_connect.add(end_neuron.IDENTIFIER);
                }
            }
            if (to_connect.size() != 0) possible_connections.put(start_neuron, to_connect); // If there are possible conneticons, we add to the list
        }

        return possible_connections;
    }

    public static void main(String[] args) {
        Random rand = new Random();
        ArrayList<Neuron> neural_nodes = new ArrayList<>();
        ArrayList<Gene> active_genes = new ArrayList<>();
        // ArrayList<Gene> deactive_genes = new ArrayList<>();
        // ArrayList<Neuron> neural_nodes_2 = new ArrayList<>();
        // ArrayList<Gene> active_genes_2 = new ArrayList<>();
        // // ArrayList<Gene> deactive_genes_2 = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            neural_nodes.add(new Neuron(i, x -> x));
        }
        // for (int i = 0; i < 5; i++) {
        //     neural_nodes_2.add(new Neuron(i, x -> x));
        // }
        VisualTester.addRandomConnection(active_genes, neural_nodes);
        // GeneHandler.addRandomConnection(active_genes_2, neural_nodes_2);
        for (int i = 0; i < 10; i++) {
            if (rand.nextInt(2) == 0) {
                VisualTester.addRandomConnection(active_genes, neural_nodes);
            }
            else {
                VisualTester.addRandomNode(active_genes, neural_nodes);
            }
        }
        // for (int i = 0; i < 10; i++) {
        //     if (rand.nextInt(2) == 0) {
        //         GeneHandler.addRandomConnection(active_genes_2, neural_nodes_2);
        //     }
        //     else {
        //         GeneHandler.addRandomNode(active_genes_2, neural_nodes_2);
        //     }
        // }

        GraphVisual vis = new GraphVisual(active_genes, Neural_Constants.NUM_OF_INPUTS, Neural_Constants.NUM_OF_OUTPUTS);
        System.out.println("The total number of nodes is: " + vis.totalNodes);


        System.out.println("Bird 1 ------------------------- \n");
        System.out.println("Active genes: \n");
        System.out.println(active_genes);
        // System.out.println("Disabled genes: \n");
        // System.out.println(deactive_genes);
        System.out.println("Neural nodes: \n");
        System.out.println(neural_nodes);
        // System.out.println("Bird 2 ------------------------- \n");
        // System.out.println("Active genes: \n");
        // System.out.println(active_genes_2);
        // // System.out.println("Disabled genes: \n");
        // // System.out.println(deactive_genes_2);
        // System.out.println("Neural nodes: \n");
        // System.out.println(neural_nodes_2);
    }

}
