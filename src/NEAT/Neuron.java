package NEAT;

import java.util.ArrayList;
import java.util.Random;

public class Neuron {
    public ArrayList<Double> weights = new ArrayList<>();
    public double bias;
    private ActivationFunction function;
    private static Random rand = new Random();

    public Neuron(ActivationFunction function, int numOfWeights) {
        this.function = function;
        for (int i = 0; i < numOfWeights; i++) {
            weights.add(rand.nextDouble() * 2 - 1); // Generates a number between -1 and 1
        }
        bias = rand.nextDouble() * 2 - 1;

    }

    public double getOutput(ArrayList<Double> inputs) {
        double output = 0;
        for (int i = 0; i < inputs.size(); i++) {
            output += inputs.get(i) * weights.get(i);
        }
        return function.Function(output + bias); // Add bias, then run through activation function
    }
}
