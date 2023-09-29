

import java.util.ArrayList;

import Game.PipeHandler;
import edu.macalester.graphics.CanvasWindow;

public class NEAT {

    private ArrayList<NeuralNetwork> networks = new ArrayList<>();
    private final PipeHandler pipes;
    private final CanvasWindow canvas;

    private static final int TOTAL_BIRDS = 100;

    public NEAT(PipeHandler pipes, CanvasWindow canvas) {
        this.pipes = pipes;
        this.canvas = canvas;
        for (int bird = 0; bird < TOTAL_BIRDS; bird++) {
            networks.add(new NeuralNetwork(pipes, canvas));
        }
    }

    public boolean move() {
        boolean birds_still_alive = false;
        for (NeuralNetwork network : networks) {
            boolean still_alive = network.moveBird();
            if (still_alive) birds_still_alive = true; // If one bird is still alive, then we don't reset until that bird dies
        }
        return birds_still_alive;
    }

    public void reset() {
        // First, get the best fitness from the neuralNetworks
        int best_fitness = 0;
        NeuralNetwork best_neural_network = networks.get(0);
        for (NeuralNetwork network : networks) {
            int fitness = network.reset();
            if (best_fitness <= fitness) {
                best_fitness = fitness;
                best_neural_network = network;
            }
        }

        // Remove all the networks and birds except the best fitness one
        for (NeuralNetwork network : networks) {
            if (!network.equals(best_neural_network)) network.removeBird(canvas);
        }
        networks.clear();
        networks.add(best_neural_network);

        // Then create mutations off of that neural network, but we will keep the original one in case the rest are worse than the original
        for (int i = 0; i < TOTAL_BIRDS - 1; i++) {
            networks.add(new NeuralNetwork(pipes, canvas, best_neural_network));
        }
    }





    // /**
    //  * Creates the initial population
    //  */
    // private void create_population() {
    //     int stuff = population_size + 1;
    // }

    // /**
    //  * Generate a random neural network
    //  */
    // private void generateNetwork() {

    // }

    // /** 
    //  * Assign a neural network to a bird 
    //  */
    // private void assignNetwork() {

    // }


    // /**
    //  * Evaluate and select the fitness scores of a group of neural networks
    //  */
    // private void evaluate(ArrayList<NeuralNetwork> networks, int n) {
    //     // Loop through list of NeuralNetwork

    //     // Select the top n fittest networks
    // }

    // /**
    //  * Set the fitness score of a neural network
    //  */
    // private void setScore() {

    // }

    // /**
    //  * Use the n fittest neural networks and breed `population_size` - `n` new neural networks
    //  */
    // private void breed(ArrayList<NeuralNetwork> fittestNetworks) {
    //     crossover(fittestNetworks.get(0), fittestNetworks.get(1));
    //     mutate();
    // }

    // /**
    //  * Mix the weights of neural network A with neural network B 
    //  */
    // private void crossover(NeuralNetwork a, NeuralNetwork b) {
        
    // }

    // private void mutate() {

    // }

}
