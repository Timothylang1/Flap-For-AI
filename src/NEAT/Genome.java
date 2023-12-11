package NEAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/*
 * Handles interactions between genomes as well as store information about each gene within the genome
 */
public class Genome {
    public HashMap<Integer, ArrayList<Gene>> genes = new HashMap<>();
    private ArrayList<Neuron> neurons = new ArrayList<>(); // Used to ensure no cycles in node connections
    public int score = 0; // Tells how well the genome does
    public double adjusted_score = 0;
    public Neural_Constants constants;
    private final Random RAND = new Random();

    /*
     * Creates basic neural network with all the input nodes and output nodes, but no connections
     */
    public Genome(Neural_Constants constants) {
        this.constants = constants;
        // Create the list of input/output neurons to begin with
        for (int n = 0; n < Neural_Constants.NUM_OF_INPUTS + Neural_Constants.NUM_OF_OUTPUTS; n++) {
            neurons.add(new Neuron(n, Neural_Constants.INPUT_OUTPUT_FUNCTION));
        }
    }

    /*
     * Creates a genome, usually a copy of another genome
     */
    public Genome(HashMap<Integer, ArrayList<Gene>> genes, ArrayList<Neuron> neurons, Neural_Constants constants) {
        this.genes = genes;
        this.neurons = neurons;
        this.constants = constants;
    }

    /*
     * Creates random connection between two unconnected nodes
     */
    private void addRandomConnection() {
        HashMap<Integer, ArrayList<Integer>> possible_connections = checkForPossibleConnections();
        if (possible_connections.size() != 0) { // As long as we can add a connection
            // Pick a random starting node, and then a random ending node from the starting node list
            int start_identifier = new ArrayList<>(possible_connections.keySet()).get(RAND.nextInt(possible_connections.size()));
            int end_indentifier = possible_connections.get(start_identifier).get(RAND.nextInt(possible_connections.get(start_identifier).size()));
            addConnection(start_identifier, end_indentifier, RAND.nextDouble(-constants.DIFFERENTIAL / 2, constants.DIFFERENTIAL / 2)); // Give an initial random weight
        }
    }

    /*
     * Breaks a connection and adds a new node, then reconnects around that node following the weights and end/start nodes of the broken connection
     */
    private void addRandomNode() {
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
                start_index = Math.max(start_index + 1, Neural_Constants.NUM_OF_INPUTS); // If the starting index is a input layer node, we can't place the new node inbetween the inital nodes, so we limit the min the start_index can be
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

        // Create connections to connect in new neuron
        // The weights are designed to maintain the original weight
        addConnection(node_number, to_break.END_NODE, to_break.weight);
        addConnection(start_identifier, node_number, 1);
    }

    /*
     * Create a new gene and add it into the dictionary of enabled_genes, or create a new entry in the dictionary if nessecary
     */
    private void addConnection(int start_identifier, int end_indentifier, double weight) {
        if (genes.keySet().contains(start_identifier)) genes.get(start_identifier).add(new Gene(end_indentifier, weight));
        else genes.put(start_identifier, new ArrayList<>(Arrays.asList(new Gene(end_indentifier, weight))));
    }

    /*
     * Returns a hashmap of all possible connections still available
     * RULES: Input Layer nodes can't connect to each other, and output nodes can't send their outputs to other nodes. Also,
     * the connections have to flow in the direction of the neural_network list to avoid cycles
     * KEY: IDENTIFIER of start_neuron
     * VALUES: IDENTIFIER of all non-connected neurons to start
     */
    private HashMap<Integer, ArrayList<Integer>> checkForPossibleConnections() {
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
            if (to_connect.size() != 0) possible_connections.put(start_neuron, to_connect); // If there are possible connections, we add to the list
        }
        return possible_connections;
    }

    /*
     * Creates a copy of this genome, then returns it but mutated slightly if chance or fate allows for it
     * NOTE: A total of 3 mutations are possible for one genome. 
     *      The genome can add connection, add node, and modify 
     *      weight all in one mutation.
     */
    public Genome mutate() {
        // Create copy genome
        Genome copy = copy();

        // Calculate probabilities of mutation, then exectute mutation if the prob meets the threshold
        double mutate_connection = RAND.nextDouble();
        double mutate_add_node = RAND.nextDouble();
        double mutate_modify_weight = RAND.nextDouble();
        if (mutate_connection < constants.MUTATE_ADD_CONNECTION) {
            copy.addRandomConnection();
        }
        if (mutate_add_node < constants.MUTATE_ADD_NODE) {
            copy.addRandomNode();
        }
        if (mutate_modify_weight < constants.MUTATE_MODIFY_WEIGHT) {
            copy.mutateWeight();
        }
        return copy;
    }

    /*
     * Pick a random weight from the genes, then mutate slightly (if there is a gene to modify the weight of)
     */
    private void mutateWeight() {
        if (genes.size() != 0) {
            int random_starting_node = new ArrayList<>(genes.keySet()).get(RAND.nextInt(genes.keySet().size()));
            genes.get(random_starting_node).get(RAND.nextInt(genes.get(random_starting_node).size())).weight += RAND.nextDouble(-constants.DIFFERENTIAL / 2, constants.DIFFERENTIAL / 2);
        }
    }

    /*
     * Creates copies of the neurons and genes, then creates a new genome with the copies, then returns the genome
     */
    public Genome copy() {
        ArrayList<Neuron> copy_neurons = new ArrayList<>(neurons.stream().map(x -> x.copy()).toList());
        return new Genome(copyGenes(), copy_neurons, constants);
    }

    /*
     * Creates copy of genes
     */
    private HashMap<Integer, ArrayList<Gene>> copyGenes() {
        HashMap<Integer, ArrayList<Gene>> copy_genes = new HashMap<>();
        genes.forEach((start_neuron, connected_neurons) -> {
            copy_genes.put(start_neuron, new ArrayList<>(connected_neurons.stream().map(x -> x.copy()).toList()));
        });
        return copy_genes;
    }

    /*
     * Return 1 if the two genomes should belong to the same species because they're similar enough
     * Returns 0 if the birds are not similar
     */
    public int similar(Genome g2) {
        // First part, we copy the g2 map so we can edit when iterating through
        HashMap<Integer, ArrayList<Gene>> g2_map_copy = g2.copyGenes();
        
        // Set up variables
        double sum_diff_weight = 0;
        int similar_genes = 0;
        int disjoint_excess_genes = 0;
        
        // Next, start figuring out how many genes the genomes share
        for (Integer start_node : genes.keySet()) {
            if (!g2_map_copy.keySet().contains(start_node)) disjoint_excess_genes += genes.get(start_node).size(); // If the start node doesn't exist in the other map, automatically all the genes are disjoint
            else {
                for (Gene g1_gene : genes.get(start_node)) {
                    if (g2_map_copy.get(start_node).contains(g1_gene)) { // If the g2 map also contains the gene, then we take the difference in weights
                        Gene g2_gene = g2_map_copy.get(start_node).get(g2_map_copy.get(start_node).indexOf(g1_gene)); // Get the corresponding gene
                        sum_diff_weight += Math.abs(g2_gene.weight - g1_gene.weight);
                        similar_genes += 1;
                        g2_map_copy.get(start_node).remove(g2_gene); // Then remove the gene from the copy so it's not counted towards the excess genes after this loop
                    }
                    else {
                        disjoint_excess_genes += 1; // Otherwise, this is a disjoint gene because it doesn't appear in the g2 set
                    }
                }
            }
        }

        // Whatever genes are left in the g2 set must be disjoint from the g1 set because they weren't removed during the above for loop
        for (ArrayList<Gene> genes : g2_map_copy.values()) {
            disjoint_excess_genes += genes.size();
        }

        // Calculate the average weight
        double average_weight_diff;
        if (similar_genes == 0) average_weight_diff = 0;
        else average_weight_diff = sum_diff_weight / similar_genes;

        // Then return if the score
        if (Neural_Constants.DIFFERENCE_THRESHOLD > disjoint_excess_genes * constants.EXCESS_DISJOINT_COEFFICIENT + average_weight_diff * constants.AVERAGE_WEIGHT_COEFFICIENT) return 1;
        else return 0; // Too different to belong to the same species

    }

    /*
     * Calculates output of neural network by following along connections and passing output from one node to the next
     */
    public double[] output(double[] initial_inputs) {    
        // First create temporary helper hashmap
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
     * Takes the most successful Genome and crossover with second genome. Crossover only looks at matching genes and picks randomly
     * between genes that match
     * IMPORTANT: THE GENOME CALLING THIS METHOD MUST HAVE A HIGHER SCORE THAN THE GENOME GETTING PASSED IN SO IT'S PERSERVED during the crossover
     */
    public Genome crossover(Genome g2) {
        HashMap<Integer, ArrayList<Gene>> new_genes = new HashMap<>();
        genes.forEach((start_node, connected_nodes) -> {
            // First, check to see if there are any connections in the g2 genome that start from this node
            if (g2.genes.keySet().contains(start_node)) {
                ArrayList<Gene> new_end_connect = new ArrayList<>();

                // Next, we check how many matching end genes we have starting from this specific node
                connected_nodes.forEach(end_node -> {
                    if (g2.genes.get(start_node).contains(end_node)) { // If we have a matching gene, then we pick randomly which one gets chosen
                        if (RAND.nextInt() == 0) {
                            new_end_connect.add(g2.genes.get(start_node).get(g2.genes.get(start_node).indexOf(end_node)).copy()); // Take from g2 gene
                        }
                        else {
                            new_end_connect.add(end_node.copy()); // Take from g1 gene
                        }
                    }
                    else {
                        new_end_connect.add(end_node.copy()); // If this gene doesn't have any matches in g2 (i.e disjoint gene), then we just take it and copy it in
                    }
                });
                new_genes.put(start_node, new_end_connect);
            }
            else {
                new_genes.put(start_node, new ArrayList<>(connected_nodes.stream().map(x -> x.copy()).toList()));
            }
        });

        // Since genome 1 is more fit than genome 2, disjoint genes 
        // belonging in genome 2 are not added to new_genes, thus we
        // also use genome 1's neuron list and neural constants.
        return new Genome(new_genes, neurons, constants);
    }
}
