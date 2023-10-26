package NEAT;

import java.text.DecimalFormat;
import java.util.Random;

/*
 * Constants class. Any variable used in our code for the neural network ends up here to allow ease of adjustment and because these variables
 * are shared across multiple classes.
 * IMPORATANT: NONE OF THE COEFFICIENTS CAN BE 0
 */
public class Neural_Constants {

    // One rand to rule them all
    private static final Random RAND = new Random();

    public static final int NUM_OF_VARIABLES = 6; // Number of variables we can change during our search
    public static final double CHANGE_DIFFERENCE = 0.05; // How much those variables can change by each trial

    public static final int NUM_OF_INPUTS = 3; // Total number of inputs to the neural network
    public static final int NUM_OF_OUTPUTS = 2; // Total number of outputs (node 1 represents jump, node 2 represents not jump)

    public static final ActivationFunction MIDDLE_FUNCTION = x -> Math.max(0, x);
    public static final ActivationFunction INPUT_OUTPUT_FUNCTION = x -> x;

    // The threshold for one bird to be considered a different spiecies compared to the rest
    public static final int DIFFERENCE_THRESHOLD = 1;

    // Starting population
    public static final int POPULATION = 100;
   
    // Max score
    public static final int MAX_SCORE = 200;

    // Formatter
    public static final DecimalFormat format = new DecimalFormat(".###");
    
    // Chances of mutations 
    public double MUTATE_ADD_CONNECTION = 0.075;
    public double MUTATE_ADD_NODE = 0.002;
    public double MUTATE_MODIFY_WEIGHT = 0.92;

    // Difference calculator
    public double DIFFERENTIAL = 1.0; // The range at which the neural network weights can change during a mutation
    public double AVERAGE_WEIGHT_COEFFICIENT = 0.5;
    public double EXCESS_DISJOINT_COEFFICIENT = 0.5;

    public void randomize() {
        MUTATE_ADD_CONNECTION = calculateRandom();
        MUTATE_ADD_NODE = calculateRandom();
        MUTATE_MODIFY_WEIGHT = calculateRandom();
        DIFFERENTIAL = calculateRandom();
        AVERAGE_WEIGHT_COEFFICIENT = calculateRandom();
        EXCESS_DISJOINT_COEFFICIENT = calculateRandom();
    }

    /*
     * Creates a random value that's a scalar multiple of CHANGE_DIFFERENCE with the minimum being CHANGE_DIFFERENCE
     */
    private double calculateRandom() {
        return CHANGE_DIFFERENCE * RAND.nextInt(1, (int) (1 / CHANGE_DIFFERENCE));
    }

    public Neural_Constants copy() {
        Neural_Constants copy = new Neural_Constants();
        copy.MUTATE_ADD_CONNECTION = MUTATE_ADD_CONNECTION;
        copy.MUTATE_ADD_NODE = MUTATE_ADD_NODE;
        copy.MUTATE_MODIFY_WEIGHT = MUTATE_MODIFY_WEIGHT;
        copy.DIFFERENTIAL = DIFFERENTIAL;
        copy.AVERAGE_WEIGHT_COEFFICIENT = AVERAGE_WEIGHT_COEFFICIENT;
        copy.EXCESS_DISJOINT_COEFFICIENT = EXCESS_DISJOINT_COEFFICIENT;
        return copy;
    }

    /*
     * Returns true if successfully modified, false if it can't be modified
     * Rules for successful modification:
     * 1. A parameter can't drop below 0
     * 2. Any mutation has to remain percentage, so it can't go above 1
     */
    public boolean modify(int to_change, int positive) {
        switch (to_change) {
            case 0: if (MUTATE_ADD_CONNECTION < 1.0 && positive == 1) MUTATE_ADD_CONNECTION += CHANGE_DIFFERENCE;
                    else if (MUTATE_ADD_CONNECTION >= CHANGE_DIFFERENCE * 2 && positive == 0) MUTATE_ADD_CONNECTION -= CHANGE_DIFFERENCE;
                    else return false;
                    break;
            case 1: if (MUTATE_ADD_NODE < 1.0 && positive == 1) MUTATE_ADD_NODE += CHANGE_DIFFERENCE;
                    else if (MUTATE_ADD_NODE >= CHANGE_DIFFERENCE * 2 && positive == 0) MUTATE_ADD_NODE -= CHANGE_DIFFERENCE;
                    else return false;
                    break;
            case 2: if (MUTATE_MODIFY_WEIGHT < 1.0 && positive == 1) MUTATE_MODIFY_WEIGHT += CHANGE_DIFFERENCE; 
                    else if (MUTATE_MODIFY_WEIGHT >= CHANGE_DIFFERENCE * 2 && positive == 0) MUTATE_MODIFY_WEIGHT -= CHANGE_DIFFERENCE;
                    else return false;
                    break;
            case 3: if (positive == 1) DIFFERENTIAL += CHANGE_DIFFERENCE;
                    else if (DIFFERENTIAL >= CHANGE_DIFFERENCE * 2) DIFFERENTIAL -= CHANGE_DIFFERENCE;
                    else return false;
                    break;
            case 4: if (positive == 1) AVERAGE_WEIGHT_COEFFICIENT += CHANGE_DIFFERENCE;
                    else if (AVERAGE_WEIGHT_COEFFICIENT >= CHANGE_DIFFERENCE * 2) AVERAGE_WEIGHT_COEFFICIENT -= CHANGE_DIFFERENCE;
                    else return false;
                    break;
            case 5: if (EXCESS_DISJOINT_COEFFICIENT < 1.0 && positive == 1) EXCESS_DISJOINT_COEFFICIENT += CHANGE_DIFFERENCE;
                    else if (EXCESS_DISJOINT_COEFFICIENT >= CHANGE_DIFFERENCE * 2 && positive == 0) EXCESS_DISJOINT_COEFFICIENT -= CHANGE_DIFFERENCE;
                    else return false;
                    break;
        }
        return true;
    }

    public boolean equals(Neural_Constants constants) {
        return Math.abs(constants.AVERAGE_WEIGHT_COEFFICIENT - AVERAGE_WEIGHT_COEFFICIENT) < CHANGE_DIFFERENCE / 100 && 
        Math.abs(constants.DIFFERENTIAL - DIFFERENTIAL) < CHANGE_DIFFERENCE / 100 && 
        Math.abs(constants.EXCESS_DISJOINT_COEFFICIENT - EXCESS_DISJOINT_COEFFICIENT) < CHANGE_DIFFERENCE / 100 &&
        Math.abs(constants.MUTATE_ADD_CONNECTION - MUTATE_ADD_CONNECTION) < CHANGE_DIFFERENCE / 100 && 
        Math.abs(constants.MUTATE_ADD_NODE - MUTATE_ADD_NODE) < CHANGE_DIFFERENCE / 100 && 
        Math.abs(constants.MUTATE_MODIFY_WEIGHT - MUTATE_MODIFY_WEIGHT) < CHANGE_DIFFERENCE / 100;
    }

    @Override
    public String toString() {
        return
        format.format(MUTATE_ADD_CONNECTION) + "\t" +
        format.format(MUTATE_ADD_NODE) + "\t" + 
        format.format(MUTATE_MODIFY_WEIGHT) + "\t" + 
        format.format(DIFFERENTIAL) + "\t" + 
        format.format(AVERAGE_WEIGHT_COEFFICIENT) + "\t" +  
        format.format(EXCESS_DISJOINT_COEFFICIENT) + "\t";
    }

    public static void main(String[] args) {
        Neural_Constants constants = new Neural_Constants();
        constants.randomize();
        System.out.println("Add C\tAdd N\tMod Wgt\tDiff\tAvg. W\tExcess");
        System.out.println(constants);
    }
}
