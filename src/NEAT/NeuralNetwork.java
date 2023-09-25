package NEAT;


public class NeuralNetwork {
    private int fitness_score;

    // Inputs
    private int bird_y;
    private int bottom_pipe;


    // Weights
    private int bird_y_weight;
    private int bottom_pipe_weight;
    
    // Bias
    private final int BIAS_MIN = -400;
    private final int BIAS_MAX = 400;


    public int get_fitness_score() {
        return fitness_score;
    }

    public NeuralNetwork(int bird_y, int bottom_pipe) {
        this.bird_y = bird_y;
        this.bottom_pipe = bottom_pipe;
    }

    private double useActivationFunction() {
        // tanH(weightedSum)
        double weightedSum = (bird_y * bird_y_weight) + (bottom_pipe * bottom_pipe_weight);
        return Math.tanh(weightedSum);
    }
 

}
