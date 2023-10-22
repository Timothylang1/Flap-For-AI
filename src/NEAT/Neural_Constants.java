package NEAT;

import java.util.Random;

/*
 * Constants class. Any variable used in our code for the neural network ends up here to allow ease of adjustment and because these variables
 * are shared across multiple classes.
 */
public class Neural_Constants {
    public static final Random RAND = new Random();
    public static final int NUM_OF_VARIABLES = 6; // Number of variables we can change during our search
    public static final double CHANGE_DIFFERENCE = 0.01;

    public static final int NUM_OF_INPUTS = 3; // Total number of inputs to the neural network
    public static final int NUM_OF_OUTPUTS = 2; // Total number of outputs (node 1 represents jump, node 2 represents not jump)

    public static final ActivationFunction MIDDLE_FUNCTION = x -> Math.max(0, x);
    public static final ActivationFunction INPUT_OUTPUT_FUNCTION = x -> x;

    // The threshold for one bird to be considered a different spiecies compared to the rest
    public static final double DIFFERENCE_THRESHOLD = 1;

    // Starting population
    public static final int POPULATION = 100;
   
    // Max score
    public static final int MAX_SCORE = 500;

    
    // Chances of mutations 
    public double MUTATE_ADD_CONNECTION = 0.075;
    public double MUTATE_ADD_NODE = 0.005;
    public double MUTATE_MODIFY_WEIGHT = 0.075;

    // Difference calculator
    public double DIFFERENTIAL = 1.0; // The range at which the neural network weights + biases can change during a mutation
    public double AVERAGE_WEIGHT_COEFFICIENT = 0.5;
    public double EXCESS_DISJOINT_COEFFICIENT = 0.5;

    public void randomize() {
        DIFFERENTIAL = RAND.nextDouble();
        MUTATE_ADD_CONNECTION = RAND.nextDouble();
        MUTATE_ADD_NODE = RAND.nextDouble();
        MUTATE_MODIFY_WEIGHT = RAND.nextDouble();
        AVERAGE_WEIGHT_COEFFICIENT = RAND.nextDouble();
        EXCESS_DISJOINT_COEFFICIENT = RAND.nextDouble();
    }

    public Neural_Constants copy() {
        Neural_Constants copy = new Neural_Constants();
        copy.MUTATE_ADD_CONNECTION = MUTATE_ADD_CONNECTION;
        copy.MUTATE_ADD_NODE = MUTATE_ADD_NODE;
        copy.MUTATE_MODIFY_WEIGHT = MUTATE_MODIFY_WEIGHT;
        copy.AVERAGE_WEIGHT_COEFFICIENT = AVERAGE_WEIGHT_COEFFICIENT;
        copy.EXCESS_DISJOINT_COEFFICIENT = EXCESS_DISJOINT_COEFFICIENT;
        return copy;
    }

    public void modify(int to_change, boolean positive) {
        switch (to_change) {
            case 0: DIFFERENTIAL += (positive ? CHANGE_DIFFERENCE: -CHANGE_DIFFERENCE); break;
            case 1: MUTATE_ADD_CONNECTION += (positive ? CHANGE_DIFFERENCE: -CHANGE_DIFFERENCE); break;
            case 2: MUTATE_ADD_NODE += (positive ? CHANGE_DIFFERENCE: -CHANGE_DIFFERENCE); break;
            case 3: MUTATE_MODIFY_WEIGHT += (positive ? CHANGE_DIFFERENCE: -CHANGE_DIFFERENCE); break;
            case 4: AVERAGE_WEIGHT_COEFFICIENT += (positive ? CHANGE_DIFFERENCE: -CHANGE_DIFFERENCE); break;
            case 5: EXCESS_DISJOINT_COEFFICIENT += (positive ? CHANGE_DIFFERENCE: -CHANGE_DIFFERENCE); break;
        }
    }
}
