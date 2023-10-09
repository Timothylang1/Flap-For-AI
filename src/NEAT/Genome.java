package NEAT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Genome {
    public ArrayList<Gene> genes = new ArrayList<>();
    public ArrayList<Neuron> neurons = new ArrayList<>(); // Used to ensure no cycles in node connections
    private HashMap<Integer, Neuron> neurons_map = new HashMap<>(); // Map that contains the exact same information as the arraylist, except allows for faster lookup time
    private static final Random RAND = new Random();

    public Genome() {
        // Create the list of input/output neurons to begin with
        for (int n = 0; n < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; n++) {
            neurons.add(new Neuron(n, Neural_Constants.INPUT_OUTPUT_FUNCTION));
        }

        // Then create hashmap
        createHashMap();
    }

    public Genome(ArrayList<Gene> genes, ArrayList<Neuron> neurons) {
        this.genes = genes;
        this.neurons = neurons;

        // Create hashmap for neurons
        createHashMap();
    }

    private void createHashMap() {
        for (Neuron neuron : neurons) {
            neurons_map.put(neuron.IDENTIFIER, neuron);
        }
    }

    /*
     * Creates random connection between two unconnected nodes
     */
    public void addRandomConnection() {
        HashMap<Integer, ArrayList<Integer>> possible_connections = checkForPossibleConnections();
        if (possible_connections.size() != 0) { // As long as we can add a node
            // Pick a random starting node, and then a random ending node from the starting node list
            int start_identifier = new ArrayList<>(possible_connections.keySet()).get(RAND.nextInt(possible_connections.size()));
            int end_indentifier = possible_connections.get(start_identifier).get(RAND.nextInt(possible_connections.get(start_identifier).size()));
            addConnection(start_identifier, end_indentifier, RAND.nextDouble(-Neural_Constants.DIFFERENTIAL / 2, Neural_Constants.DIFFERENTIAL / 2)); // Give an initial random weight
        }
    }

    /*
     * Breaks a connection and adds a new node, then reconnects around that node following the weights and end/start nodes of the broken connection
     */
    public void addRandomNode() {
        // First, pick a random connection to severe and deactivate it if there is a genome in the list (as long as it's not an empty neural network)
        if (genes.isEmpty()) return;
        Gene to_break = genes.remove(RAND.nextInt(genes.size()));

        // Then create new node and choose a random location to insert into the middle section of the list of nodes between the original initial node and the end node of the connection
        int start_index;
        int end_index;
        for (start_index = 0; start_index < neurons.size(); start_index++) {
            if (neurons.get(start_index).IDENTIFIER == to_break.INITIAL_NODE) {
                start_index = Math.max(start_index + 1, Neural_Constants.NUM_OF_INPUTS); // If the starting index is a initial node, we can't place the new node inbetween the inital nodes, so we limit the min the start_index can be
                break;
            };
        }
        for (end_index = start_index; end_index < neurons.size(); end_index++) {
            if (neurons.get(end_index).IDENTIFIER == to_break.END_NODE) {
                end_index = Math.min(end_index, neurons.size() - Neural_Constants.NUM_OF_OUTPUTS) + 1; // We can't add the node after the end nodes, so we limit the max the end_index can be
                break;
            };
        }

        // The new node will be given a node_value equal to the number of nodes in the list, so each new node is unique
        int node_number = neurons.size();
        Neuron to_add = new Neuron(node_number, Neural_Constants.MIDDLE_FUNCTION);
        neurons.add(RAND.nextInt(start_index, end_index), to_add);

        // Update the hashmap to include the new node
        neurons_map.put(node_number, to_add);

        // Create connections to connect in new neuron (IMPORTANT: DO NOT SWAP THE ORDER OF THESE BECAUSE OF HOW THEY ARE PLACED IN THE GENE SEQUENCE)
        // The weights are designed to maintain the original weight
        addConnection(node_number, to_break.END_NODE, to_break.weight);
        addConnection(to_break.INITIAL_NODE, node_number, 1);
    }

    /*
     * Adds new gene in correct location of neural_genes so that its output is calculated before we need it
     */
    public void addConnection(int start_identifier, int end_indentifier, double weight) {
        // Create a new gene and add it into the list of enabled_genes
        genes.add(new Gene(start_identifier, end_indentifier, weight));

        // Then sort the genes based on the order of the neural_nodes so that the graph flows the correct direction and each node gets all its inputs before it needs them
        HashMap<Integer, List<Gene>> to_sort = new HashMap<>();
        neurons.forEach(neuron -> {
            to_sort.put(neuron.IDENTIFIER, genes.stream().filter(x -> x.INITIAL_NODE == neuron.IDENTIFIER).toList());
        });
        neurons.clear();
        neurons.forEach(neuron -> {
            genes.addAll(to_sort.get(neuron.IDENTIFIER));
        });
    }

    /*
     * Returns a hashmap of all possible connections still available
     * RULES: initial nodes can't connect to each other, and output nodes can't send their outputs to other nodes. Also,
     * the connections have to flow in the direction of the neural_network list to avoid cycles
     * KEY: IDENTIFIER of start_neuron
     * VALUES: IDENTIFIER of all non-connected neurons to start
     */
    public HashMap<Integer, ArrayList<Integer>> checkForPossibleConnections() {
        HashMap<Integer, ArrayList<Integer>> possible_connections = new HashMap<>();

        // For each neuron, we can only connect to neurons to the right of that neuron in the list (to maintain directional flow and have no cycles)
        // Also, we can't connect the output neurons to other neurons, so we don't include them in possible connections start_neuron
        for (int neuron_start_index = 0; neuron_start_index < neurons.size() - Neural_Constants.NUM_OF_OUTPUTS; neuron_start_index++) {
            int start_neuron = neurons.get(neuron_start_index).IDENTIFIER;

            // Now we get a list of all the IDENTIFIERS of the neurons this start_neuron is connected to
            ArrayList<Integer> already_connected = new ArrayList<>();
            for (Gene gene : genes) {
                if (gene.INITIAL_NODE == start_neuron) already_connected.add(gene.END_NODE);
            }

            // Now we get the list of neurons it isn't connected to
            ArrayList<Integer> to_connect = new ArrayList<>();

            // SPECIAL CASE: if we have an input neuron, we can't connect to another input neuron, so we start looking for neurons to connect to past the input neurons
            int end_of_connection_index = Math.max(neuron_start_index + 1, Neural_Constants.NUM_OF_INPUTS);

            // Now we check all of the possible connections by checking if that connection already exists in the geneomes
            for (int neuron_end_index = end_of_connection_index; neuron_end_index < neurons.size(); neuron_end_index++) {
                Neuron end_neuron = neurons.get(neuron_end_index);
                if (!already_connected.contains(end_neuron.IDENTIFIER)) {
                    to_connect.add(end_neuron.IDENTIFIER);
                }
            }
            if (to_connect.size() != 0) possible_connections.put(start_neuron, to_connect); // If there are possible conneticons, we add to the list
        }
        return possible_connections;
    }

    /*
     * Returns a hashset list of n mutations where n = number_of_mutations
     * KEY: list of genes
     * VALUE: list of neural nodes associated with those genes
     */
    public ArrayList<Genome> mutate(int number_of_mutations) {
        ArrayList<Genome> mutations = new ArrayList<>();

        // Calculate probabilities and execute mutations if nessecary
        while (mutations.size() < number_of_mutations) {

            // Create copy genome
            Genome copy = copy();

            // Calculate probabilities of mutation, then exectute mutation if the prob meets the threshold
            double mutate_add_connection = RAND.nextDouble();
            double mutate_add_node = RAND.nextDouble();
            double mutate_modify_weight = RAND.nextDouble();
            double mutate_modify_bias = RAND.nextDouble();
            if (mutate_add_connection < Neural_Constants.MUTATE_ADD_CONNECTION) {
                copy.addRandomConnection();
            }
            if (mutate_add_node < Neural_Constants.MUTATE_ADD_NODE) {
                copy.addRandomNode();
            }
            if (mutate_modify_weight < Neural_Constants.MUTATE_MODIFY_WEIGHT) {
                copy.mutateWeight();
            }
            if (mutate_modify_bias < Neural_Constants.MUTATE_MODIFY_BIAS) {
                copy.mutateBias();
            }
            mutations.add(copy);
        }
        return mutations;
    }

    /*
     * Pick a random weight from the genes, then mutate slightly
     */
    public void mutateWeight() {
        genes.get(RAND.nextInt(genes.size())).weight += RAND.nextDouble(-Neural_Constants.DIFFERENTIAL / 2, Neural_Constants.DIFFERENTIAL / 2);
    }

    /*
     * Pick a random bias from the nodes then mutate slightly (except for the starter nodes, their bias should always remain 0)
     */
    public void mutateBias() {
        neurons.get(RAND.nextInt(Neural_Constants.NUM_OF_INPUTS, neurons.size())).bias += RAND.nextDouble(-Neural_Constants.DIFFERENTIAL / 2, Neural_Constants.DIFFERENTIAL / 2);
    }

    /*
     * Creates copies of the neurons and genes, then creates a new genome with the copies, then returns the genome
     */
    public Genome copy() {
        ArrayList<Gene> copy_genes = new ArrayList<>();
        genes.forEach(x -> copy_genes.add(x.copy()));
        ArrayList<Neuron> copy_neurons = new ArrayList<>();
        neurons.forEach(x -> copy_neurons.add(x.copy()));
        return new Genome(copy_genes, copy_neurons);
    }

    /*
     * Return true if the two genomes should belong to the same species because they're similar enough
     */
    public static boolean difference(Genome g1, Genome g2) {
        double average_weight = 0;
        ArrayList<Gene> g1_genes = new ArrayList<>(g1.genes);
        ArrayList<Gene> g2_genes = new ArrayList<>(g2.genes);
        g1.genes.removeIf(x -> {
            if (g2_genes.contains(x)) {
                
                return true;
            }
            return false;
        });
    }

    /*
     * Calculates output of neural network by following along connections and passing output from one node to the next
     */
    public double[] output(double[] inputs) {        
        // First, add the inputs to the initial nodes
        for (int n = 0; n < Neural_Constants.NUM_OF_INPUTS; n++) {
            neurons.get(n).output = inputs[n];
        }

        // Then go through the list of neurons, calculate the outputs, and send them to the correct nodes
        int current_gene = 0; // Tracker to track which gene we are currently on
        for (Neuron neuron : neurons) {
            double output = neuron.calculateOutput();

            // Because we sorted the connections list in order of the neurons, we just need to keep iterating through the connections list
            // until the starting neurons doesn't match with the current neuron
            while (current_gene < genes.size()) {
                Gene gene = genes.get(current_gene);
                current_gene += 1;

                // If the neurons 
                if (gene.INITIAL_NODE != neuron.IDENTIFIER) break;
            }
        }

    }

    /*
     * 
     */
    public static Genome crossover(Genome g1, Genome g2) {
        return new Genome();
    }


    public static void main(String[] args) {
    }
}
