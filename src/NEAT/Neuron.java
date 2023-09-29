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
    public ArrayList<Neuron> connected_neurons = new ArrayList<>();
    public double output;

    private static final double DIFFERENTIAL = 1; // The range at which the neural network changes

    /*
     * For input neurons, we only give them one input which we don't change at all. The only purpose of the input neuron is to connect to the other nuerons
     */
    public Neuron() {
        weights = new ArrayList<>(Arrays.asList(1.0));
        bias = 0;
        function = x -> x;
    }

    /*
     * Initalization of new neuron for middle
     */
    public Neuron(int num_of_weights, ActivationFunction function) {
        this.function = function;
        for (int i = 0; i < num_of_weights; i++) {
            weights.add(rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL / 2); // Slightly change and vary the weights
        }
        bias = bias + rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL / 2;
    }

    /*
     * Modifying input slightly (evolving the neuron randomly)
     */
    public Neuron(ArrayList<Double> input_weights, double bias, ActivationFunction function) {
        this.function = function;
        for (int i = 0; i < input_weights.size(); i++) {
            weights.add(input_weights.get(i) + rand.nextDouble() * DIFFERENTIAL - DIFFERENTIAL); // Slightly change and vary the weights
        }
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
        for (Neuron neuron : connected_neurons) {
            neuron.inputs.add(output);
        }
    }
}
