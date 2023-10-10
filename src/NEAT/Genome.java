package NEAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Genome {
    // public ArrayList<Gene> genes = new ArrayList<>();
    private HashMap<Integer, ArrayList<Gene>> genes = new HashMap<>();
    public ArrayList<Neuron> neurons = new ArrayList<>(); // Used to ensure no cycles in node connections
    private static final Random RAND = new Random();

    public Genome() {
        // Create the list of input/output neurons to begin with
        for (int n = 0; n < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; n++) {
            neurons.add(new Neuron(n, Neural_Constants.INPUT_OUTPUT_FUNCTION));
        }
    }

    public Genome(HashMap<Integer, ArrayList<Gene>> genes, ArrayList<Neuron> neurons) {
        this.genes = genes;
        this.neurons = neurons;
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
        // If there are no connections, then we stop here
        if (genes.isEmpty()) return;

        // If there are connections, pick a random one and break it
        int start_identifier = new ArrayList<>(genes.keySet()).get(RAND.nextInt(genes.size()));
        Gene to_break = genes.get(start_identifier).remove(RAND.nextInt(genes.get(start_identifier).size()));

        // Then create new node and choose a random location to insert into the middle section of the list of nodes between the original initial node and the end node of the connection
        int start_index;
        int end_index;
        for (start_index = 0; start_index < neurons.size(); start_index++) {
            if (neurons.get(start_index).IDENTIFIER == start_identifier) {
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
        neurons.add(RAND.nextInt(start_index, end_index), new Neuron(node_number, Neural_Constants.MIDDLE_FUNCTION));

        // Create connections to connect in new neuron (IMPORTANT: DO NOT SWAP THE ORDER OF THESE BECAUSE OF HOW THEY ARE PLACED IN THE GENE SEQUENCE)
        // The weights are designed to maintain the original weight
        addConnection(node_number, to_break.END_NODE, to_break.weight);
        addConnection(start_identifier, node_number, 1);
    }

    /*
     * Create a new gene and add it into the dictionary of enabled_genes, or create a new entry in the dictionary if nessecary
     */
    public void addConnection(int start_identifier, int end_indentifier, double weight) {
        if (genes.keySet().contains(start_identifier)) genes.get(start_identifier).add(new Gene(end_indentifier, weight));
        else genes.put(start_identifier, new ArrayList<>(Arrays.asList(new Gene(end_indentifier, weight))));
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
            
            // Then we get the list of neurons this neuron is connected to. If there is no entry for the connections list, then the neuron isn't connected to anything
            List<Integer> already_connected = new ArrayList<>();
            if (genes.keySet().contains(start_neuron)) already_connected = genes.get(start_neuron).stream().map(x -> x.END_NODE).toList();

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
     * Pick a random weight from the genes, then mutate slightly (if there is a gene to modify the weight of)
     */
    public void mutateWeight() {
        if (genes.size() != 0) {
            int random_starting_node = new ArrayList<>(genes.keySet()).get(RAND.nextInt(genes.keySet().size()));
            genes.get(random_starting_node).get(RAND.nextInt(genes.get(random_starting_node).size())).weight += RAND.nextDouble(-Neural_Constants.DIFFERENTIAL / 2, Neural_Constants.DIFFERENTIAL / 2);
        }
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
        HashMap<Integer, ArrayList<Gene>> copy_genes = new HashMap<>();
        genes.forEach((start_neuron, connected_neurons) -> {
            copy_genes.put(start_neuron, new ArrayList<>(connected_neurons.stream().map(x -> x.copy()).toList()));
        });
        ArrayList<Neuron> copy_neurons = new ArrayList<>(neurons.stream().map(x -> x.copy()).toList());
        return new Genome(copy_genes, copy_neurons);
    }

    /*
     * Return true if the two genomes should belong to the same species because they're similar enough
     */
    public static boolean difference(Genome g1, Genome g2) {
        // First part focuses on converting the information to an easier format as well as figuring out which gene is the bigger
        HashMap<Integer, List<Integer>> g1Map = new HashMap<>();
        g1.genes.forEach((x, y) -> g1Map.put(x, y.stream().map(gene -> gene.END_NODE).toList())); // Converts genes to just a list of output nodes
        HashMap<Integer, ArrayList<Integer>> g2Map = new HashMap<>();
        g2.genes.forEach((x, y) -> g2Map.put(x, new ArrayList<>(y.stream().map(gene -> gene.END_NODE).toList()))); // We are going to be removing elements from the second hashmap, so this needs to be an arraylist
        
        // Set up variables
        double sum_diff_weight = 0;
        int similar_genes = 0;
        int disjoint_excess_genes = 0;
        
        // Next, start figuring out how many genes the genomes share
        for (Integer start_node : g1Map.keySet()) {
            if (!g2Map.keySet().contains(start_node)) disjoint_excess_genes += g1Map.get(start_node).size(); // If the start node doesn't exist in the other map, automatically all the genes are disjoint
            else {
                for (Integer end_node : g1Map.get(start_node)) {
                    if (g2Map.get(
                        
                    ))
                }
            }
        }

    }

    /*
     * Calculates output of neural network by following along connections and passing output from one node to the next
     */
    public double[] output(double[] initial_inputs) {        
        // First create helper temporary hashmap
        HashMap<Integer, Double> inputs = new HashMap<>(); // KEY: the IDENTIFIER of the neuron, VALUE: the input for that neuron when the time is right

        // Second, add the inputs to the initial nodes
        for (int n = 0; n < Neural_Constants.NUM_OF_INPUTS; n++) {
            inputs.put(n, initial_inputs[n]);
        }

        // Then insert into the map the rest of the nodes
        for (int n = Neural_Constants.NUM_OF_INPUTS; n < neurons.size(); n++) {
            inputs.put(n, 0.0);
        }

        // Then go through the list of neurons, calculate the outputs, and send them to the correct nodes
        for (Neuron neuron : neurons) {

            // If this neuron is connected to another neuron, then we can calculate it's output and send off
            if (genes.containsKey(neuron.IDENTIFIER)) {

                // Calculate output from that neuron
                double output_from_neuron = neuron.calculateOutput(inputs.get(neuron.IDENTIFIER));

                // Add that output to the inputs of all the neurons
                genes.get(neuron.IDENTIFIER).forEach(x -> {
                    inputs.put(x.END_NODE, inputs.get(x.END_NODE) + output_from_neuron * x.weight);
                });
            }
        }

        // At the end, take the last neuron (the output neurons) and add them to an array, then send the array off
        double[] to_return = new double[Neural_Constants.NUM_OF_OUTPUTS];
        for (int i = 0; i < Neural_Constants.NUM_OF_OUTPUTS; i++) {
            to_return[i] = neurons.get(neurons.size() - Neural_Constants.NUM_OF_OUTPUTS + i).calculateOutput(inputs.get(Neural_Constants.NUM_OF_INPUTS + i));
        }

        return to_return;
    }


    /*
     * 
     */
    public static Genome crossover(Genome g1, Genome g2) {
        return new Genome();
    }


    public static void main(String[] args) {
        Genome genome = new Genome();
        double[] input = new double[]{1, 4, 9};
        double[] output = new double[Neural_Constants.NUM_OF_OUTPUTS];
        // genome.addRandomConnection();
        // output = genome.output(input);
        // Genome.printOutput(output);
        // genome.addRandomNode();
        // genome.addRandomNode();
        for (int i = 0; i < 1000; i++) {
            genome = genome.mutate(1).get(0);
        }
        output = genome.output(input);
        Genome.printOutput(output);
    }

    public static void printOutput(double[] output) {
        System.out.println("Output:");
        for (int i = 0; i < Neural_Constants.NUM_OF_OUTPUTS; i++) {
            System.out.println(output[i]);
        }
    }
}