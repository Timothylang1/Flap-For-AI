package NEAT;

import java.util.ArrayList;

public class NEAT {
    private int population_size;
    
    public NEAT(int population_size) {
        this.population_size = population_size;
    }


    /**
     * Creates the initial population
     */
    private void create_population() {
        int stuff = population_size + 1;
    }

    /**
     * Generate a random neural network
     */
    private void generateNetwork() {

    }

    /** 
     * Assign a neural network to a bird 
     */
    private void assignNetwork() {

    }


    /**
     * Evaluate and select the fitness scores of a group of neural networks
     */
    private void evaluate(ArrayList<NeuralNetwork> networks, int n) {
        // Loop through list of NeuralNetwork

        // Select the top n fittest networks
    }

    /**
     * Set the fitness score of a neural network
     */
    private void setScore() {

    }

    /**
     * Use the n fittest neural networks and breed `population_size` - `n` new neural networks
     */
    private void breed(ArrayList<NeuralNetwork> fittestNetworks) {
        crossover(fittestNetworks.get(0), fittestNetworks.get(1));
        mutate();
    }

    /**
     * Mix the weights of neural network A with neural network B 
     */
    private void crossover(NeuralNetwork a, NeuralNetwork b) {
        
    }

    private void mutate() {

    }

}
