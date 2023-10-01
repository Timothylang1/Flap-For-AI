package NEAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Neuron {
    // public ArrayList<Double> weights = new ArrayList<>();
    public HashMap<Neuron, Double> weights = new HashMap<>(); // Key refers to which input neuron gave the value, value represents the weight from that neuron
    public double bias;
    private ActivationFunction function;
    private static Random rand = new Random();
    // public ArrayList<Double> inputs = new ArrayList<>();
    public HashMap<Neuron, Double> inputs = new HashMap<>(); // 
    public ArrayList<Neuron> connected_neurons = new ArrayList<>(); // 
    public double output;

    /*
     * For input neurons, we only give them one input which we don't change at all. The only purpose of the input neuron is to connect to the other nuerons
     */
    public Neuron() {
        // Special case for the input neuron to allow calculateOutput to operate (because there is no inputs given to this neuron from other neurons, we just say that the input neuron gives it's inputs to itself)
        weights.put(this, 1.0);
        bias = 0;
        function = x -> x;
    }

    /*
     * Initalization of new neuron for middle
     */
    public Neuron(int num_of_weights, ActivationFunction function) {
        this.function = function;
        for (int i = 0; i < num_of_weights; i++) {
            weights.add(rand.nextDouble() * Neural_Constants.DIFFERENTIAL - Neural_Constants.DIFFERENTIAL / 2); // Slightly change and vary the weights
        }
        bias = bias + rand.nextDouble() * Neural_Constants.DIFFERENTIAL - Neural_Constants.DIFFERENTIAL / 2;
    }

    /*
     * Copies in a neuron key values, then mutates slightly
     */
    public void mutate(Neuron neuron) {
        // Copies in weights
        weights = new ArrayList<>(neuron.weights);
        weights.replaceAll(x -> {
            return x + rand.nextDouble() * Neural_Constants.DIFFERENTIAL - Neural_Constants.DIFFERENTIAL / 2;
        });

        // Copies in bias
        bias = neuron.bias;
        bias = bias + rand.nextDouble() * Neural_Constants.DIFFERENTIAL - Neural_Constants.DIFFERENTIAL / 2;
    }

    public void calculateOutput() {
        output = 0;
        for (Neuron neuron : weights.keySet()) {
            output += weights.get(neuron) * inputs.get(neuron);
        }
        output = function.Function(output + bias);
        inputs.clear(); // Clear inputs after each calculation
        
        // Adds output to all connected neurons
        for (Neuron neuron : connected_neurons) {
            neuron.inputs.put(this, output);
        }
    }
}
