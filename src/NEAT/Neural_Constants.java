package NEAT;

/*
 * Constants class. Any variable used in our code for the neural network ends up here to allow ease of adjustment and because these variables
 * are shared across multiple classes.
 */
public class Neural_Constants {
    public static final int NUM_OF_INPUTS = 8; // Total number of inputs to the neural network
    public static final int NUM_OF_OUTPUTS = 2; // Total number of outputs (node 1 represents jump, node 2 represents not jump)
    public static final int NUM_OF_MAX_INPUTS = 8; // All possible inputs that the bird can give

    public static final double DIFFERENTIAL = 1.0; // The range at which the neural network weights + biases can change during a mutation
    public static final ActivationFunction MIDDLE_FUNCTION = x -> Math.max(0, x);
    public static final ActivationFunction INPUT_OUTPUT_FUNCTION = x -> x;

    // Chances of mutations
    public static final double MUTATE_ADD_CONNECTION = 0.075;
    public static final double MUTATE_ADD_NODE = 0.005;
    public static final double MUTATE_MODIFY_WEIGHT = 0.075;

    // Difference calculator
    public static final double DIFFERENCE_THRESHOLD = 6;
    public static final double AVERAGE_WEIGHT_COEFFICIENT = 1;
    public static final double EXCESS_DISJOINT_COEFFICIENT = 1;

    // Starting population
    public static final int POPULATION = 100;
}
