package NEAT;

public class Neuron {
    public double bias;
    private ActivationFunction function;
    public double output;
    public final int IDENTIFIER;

    /*
     * For input neurons, we only give them one input which we don't change at all. The only purpose of the input neuron is to connect to the other nuerons
     */
    public Neuron(int identifier, ActivationFunction function) {
        this.function = function;
        IDENTIFIER = identifier;
        bias = 0; // Start with an initial bias of 0;
        output = bias;
    }

    /*
     * Resets output and return the calculated output of that neuron
     */
    public double calculateOutput() {
        double to_return = function.Function(output); // Use activation function assigned to that neuron
        output = bias;
        return to_return;
    }

    @Override
    public String toString() {
        return "Neuron Info:\nIdentifier: " + Integer.toString(IDENTIFIER) + "\n";
    }
}
