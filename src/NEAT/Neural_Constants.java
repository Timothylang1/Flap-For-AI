package NEAT;

public class Neural_Constants {
    public static final int NUM_OF_INPUTS = 3; // Total number of inputs to the neural network
    public static final int NUM_OF_OUTPUTS = 2; // Total number of outputs (node 1 represents jump, node 2 represents not jump)

    public static final double DIFFERENTIAL = 1.0; // The range at which the neural network weights + biases can change during a mutation
    public static final ActivationFunction MIDDLE_FUNCTION = x -> Math.max(0, x);
    public static final ActivationFunction INPUT_OUTPUT_FUNCTION = x -> x;

    // Chances of mutations
    public static final double MUTATE_ADD_CONNECTION = 0.075;
    public static final double MUTATE_ADD_NODE = 0.075;
    public static final double MUTATE_MODIFY_WEIGHT = 0.075;
    public static final double MUTATE_MODIFY_BIAS = 0.075;

    public static final int MAX_CONNECTIONS = 100000;
}
