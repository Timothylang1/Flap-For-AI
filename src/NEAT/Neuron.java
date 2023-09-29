package NEAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Neuron {
    public ArrayList<Double> weights = new ArrayList<>();
    public double bias;
    private ActivationFunction function;
    private static Random rand = new Random();
    public ArrayList<Double> inputs = new ArrayList<>();
    public ArrayList<Integer> connected_neurons = new ArrayList<>(); // Holds the index of connected neurons in all_neurons list
    private ArrayList<Neuron> all_neurons;
    public double output;

    private static final double DIFFERENTIAL = 1; // The range at which the neural network changes

    /*
     * For input neurons, we only give them one input which we don't change at all. The only purpose of the input neuron is to connect to the other nuerons
     */
    public Neuron(ArrayList<Neuron> all_neurons) {
        this.all_neurons = all_neurons;
        weights = new ArrayList<>(Arrays.asList(1.0));
        bias = 0;
        function = x -> x;
    }

    /*
     * Initalization of new neuron for middle
     */
    public Neuron(int num_of_weights, ActivationFunction function, ArrayList<Neuron> all_neurons) {
        this.all_neurons = all_neurons;
        this.function = function;
        for (int i = 0; i < num_of_weights; i++) {
            weights.add(rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL / 2); // Slightly change and vary the weights
        }
        bias = bias + rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL / 2;
    }

    /*
     * Copies in a neuron key values, then mutates slightly
     */
    public void mutate(Neuron neuron) {
        // Copies in weights
        weights = new ArrayList<>(neuron.weights);
        weights.replaceAll(x -> {
            return x + rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL / 2;
        });

        // Copies in bias
        bias = neuron.bias;
        bias = bias + rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL / 2;
    }

    public void calculateOutput() {
        output = 0;
        for (int i = 0; i < inputs.size(); i++) {
            output += inputs.get(i) * weights.get(i);
        }
        output = function.Function(output + bias);
        inputs.clear(); // Clear inputs after each calculation
        
        // Adds output to all connected neurons
        for (Integer neuron : connected_neurons) {
            all_neurons.get(neuron).inputs.add(output);
        }
    }
}
