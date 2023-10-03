package NEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * Contains list of all the changes made as well as methods to determine whether a change was made previously or not
 */
public class GeneHandler {
    
    private int neuron_number = Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; // Global number of all new neurons we've seen
    private HashMap<Gene, Integer> new_node_connections = new HashMap<>(); // KEY: Gene connection that was broken, VALUE: the number of the new node that goes in place of the broken connection
    private static final Random RAND = new Random();

    public GeneHandler() {
    }

    /*
     * Creates random connection between two unconnected nodes
     */
    public void addRandomConnection(ArrayList<Gene> neural_genes, ArrayList<Neuron> neural_nodes) {
        HashMap<Integer, ArrayList<Integer>> possible_connections = checkForPossibleConnections(neural_genes, neural_nodes);
        if (possible_connections.size() != 0) { // As long as we can add a node
            // Pick a random starting node, and then a random ending node from the starting node list
            int start_identifier = new ArrayList<>(possible_connections.keySet()).get(RAND.nextInt(possible_connections.size()));
            int end_indentifier = possible_connections.get(start_identifier).get(RAND.nextInt(possible_connections.get(start_identifier).size()));
            addConnection(neural_genes, start_identifier, end_indentifier, RAND.nextDouble(-Neural_Constants.DIFFERENTIAL / 2, Neural_Constants.DIFFERENTIAL / 2)); // Give an initial random weight
        }
    }

    /*
     * Breaks a connection and adds a new node, then reconnects around that node following the weights and end/start nodes of the broken connection
     */
    public void addRandomNode(ArrayList<Gene> neural_genes, ArrayList<Neuron> neural_nodes) {
        // First, pick a random connection to severe and deactivate it
        Gene to_break = neural_genes.get(RAND.nextInt(neural_genes.size()));
        to_break.enabled = false;

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

        // Then create new node and choose a random location to insert into the middle section of the list of nodes
        neural_nodes.add(RAND.nextInt(Neural_Constants.NUM_OF_INPUTS, neural_nodes.size() - Neural_Constants.NUM_OF_OUTPUTS + 1), new Neuron(node_number, Neural_Constants.MIDDLE_FUNCTION));

        // Create connections to connect in new neuron (IMPORTANT: DO NOT SWAP THE ORDER OF THESE BECAUSE OF HOW THEY ARE PLACED IN THE GENE SEQUENCE)
        // The weights are designed to maintain the original weight
        addConnection(neural_genes, node_number, to_break.END_NODE, to_break.weight);
        addConnection(neural_genes, to_break.INITIAL_NODE, node_number, 1);
    }

    /*
     * Adds new gene in correct location of neural_genes so that its output is calculated before we need it
     */
    public void addConnection(ArrayList<Gene> neural_genes, int start_identifier, int end_indentifier, double weight) {
        // Create a new gene
        Gene new_gene = new Gene(start_identifier, end_indentifier, weight);

        // Figure out where in the neural_genes the new gene fits. Essentially, we will add it in right before we need the gene
        boolean found_location = false;
        for (int gene_index = 0; gene_index < neural_genes.size(); gene_index++) {
            if ((neural_genes.get(gene_index).INITIAL_NODE == end_indentifier) && neural_genes.get(gene_index).enabled) { // If we find an enabled connection who needs the input, then this connection has to come before it
                neural_genes.add(gene_index, new_gene);
                found_location = true;
                break;
            }
        }

        // If we can't find a location, then add it to the end of the list because it has to be an output node
        if (!found_location) {
            neural_genes.add(new_gene);
        }
    }

    /*
     * Returns a hashmap of all possible connections still available
     * RULES: initial nodes can't connect to each other, and output nodes can't send their outputs to other nodes. Also,
     * the connections have to flow in the direction of the neural_network list to avoid cycles
     * KEY: IDENTIFIER of start_neuron
     * VALUES: IDENTIFIER of all non-connected neurons to start
     */
    public HashMap<Integer, ArrayList<Integer>> checkForPossibleConnections(ArrayList<Gene> neural_genes, ArrayList<Neuron> neural_nodes) {
        HashMap<Integer, ArrayList<Integer>> possible_connections = new HashMap<>();

        // For each neuron, we can only connect to neurons to the right of that neuron in the list (to maintain directional flow and have no cycles)
        // Also, we can't connect the output neurons to other neurons, so we don't include them in possible connections start_neuron
        for (int neuron_start_index = 0; neuron_start_index < neural_nodes.size() - Neural_Constants.NUM_OF_OUTPUTS; neuron_start_index++) {
            int start_neuron = neural_nodes.get(neuron_start_index).IDENTIFIER;

            // Now we get a list of all the IDENTIFIERS of the neurons this start_neuron is connected to
            ArrayList<Integer> already_connected = new ArrayList<>();
            neural_genes.forEach(x -> {
                if (x.INITIAL_NODE == start_neuron) {
                    already_connected.add(x.END_NODE);
                }
            });
            
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
            if (to_connect.size() != 0) possible_connections.put(start_neuron, to_connect); // If there are possible connections, we add to the list
        }

        return possible_connections;
    }

    public static void main(String[] args) {
        RAND.nextInt(3, 3 + 1);
        GeneHandler handler = new GeneHandler();
        ArrayList<Neuron> neural_nodes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            neural_nodes.add(new Neuron(i, x -> x));
        }
        ArrayList<Gene> genes = new ArrayList<>();
        handler.addRandomConnection(genes, neural_nodes);
        handler.addRandomNode(genes, neural_nodes);
        handler.addRandomNode(genes, neural_nodes);
        handler.addRandomNode(genes, neural_nodes);

        System.out.println(genes);
    }

}
