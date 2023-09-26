

import java.util.ArrayList;

import Game.Bird;
import Game.PipeHandler;
import NEAT.ActivationFunction;
import NEAT.Layer;
import edu.macalester.graphics.CanvasWindow;

public class NEAT {

    private static final int TOTAL_BIRDS = 10;
    private ArrayList<NeuralNetwork> networks = new ArrayList<>();

    public NEAT(PipeHandler pipes, CanvasWindow canvas) {
        for (int bird = 0; bird < TOTAL_BIRDS; bird++) {
            networks.add(new NeuralNetwork(pipes, canvas));
        }
    }

    public boolean move() {
        boolean birds_still_alive = false;
        for (NeuralNetwork network : networks) {
            boolean still_alive = network.moveBird();
            if (!birds_still_alive && still_alive) birds_still_alive = true; 
        }
        if (!birds_still_alive) {
            reset();
            return false;
        }
        return true;
    }

    private void reset() {
        
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
