package NEAT;

/*
 * Holds the information about each neuron in the neural network, mainly which activation function to use and its current bias
 */
public class Neuron {
    public ActivationFunction function;
    public final int IDENTIFIER;

    /*
     * For input neurons, we only give them one input which we don't change at all. The only purpose of the input neuron is to connect to the other nuerons
     */
    public Neuron(int identifier, ActivationFunction function) {
        this.function = function;
        IDENTIFIER = identifier;
    }

    /*
     * Calculates output using the activation function and bias
     */
    public double calculateOutput(double input) {
        return function.function(input); // Use activation function assigned to that neuron
    }

    /*
     * Creates a copy of the neuron by sending back a different object
     */
    public Neuron copy() {
        return new Neuron(IDENTIFIER, function);
    }

    @Override
    public int hashCode() {
        return IDENTIFIER;
    }

    @Override
    public boolean equals(Object o) {
        return ((Neuron) o).IDENTIFIER == IDENTIFIER;
    }

    @Override
    public String toString() {
        return "Neuron Info:\nIdentifier: " + Integer.toString(IDENTIFIER) + "\n";
    }
}
